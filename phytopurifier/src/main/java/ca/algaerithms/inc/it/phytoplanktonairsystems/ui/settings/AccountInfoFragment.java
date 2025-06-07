package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;

public class AccountInfoFragment extends Fragment {

    private FragmentAccountInfoBinding binding;
    private SharedPreferences prefs;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(requireContext(), "Gallery access permission denied.", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    binding.profileImage.setImageURI(selectedImageUri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireContext().getSharedPreferences("account_info", Context.MODE_PRIVATE);

        // Load saved values
        binding.usernameInput.setText(prefs.getString("username", ""));
        binding.emailInput.setText(prefs.getString("email", ""));
        binding.phoneInput.setText(prefs.getString("phone", ""));
        binding.birthdayInput.setText(prefs.getString("birthday", ""));

        // Date Picker
        binding.birthdayInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, day) -> {
                        String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                        binding.birthdayInput.setText(formattedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });

        // Save Button
        binding.saveButton.setOnClickListener(v -> {
            prefs.edit()
                    .putString("username", binding.usernameInput.getText().toString())
                    .putString("email", binding.emailInput.getText().toString())
                    .putString("phone", binding.phoneInput.getText().toString())
                    .putString("birthday", binding.birthdayInput.getText().toString())
                    .apply();

            Toast.makeText(getContext(), "Details saved locally", Toast.LENGTH_SHORT).show();
        });

        // Profile Image Click → Request Permission
        binding.profileImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
