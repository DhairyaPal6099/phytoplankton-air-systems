/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
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
            String username = binding.usernameInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String phone = binding.phoneInput.getText().toString().trim();
            String birthday = binding.birthdayInput.getText().toString().trim();

            boolean hasError = false;

            // Clear old errors
            binding.usernameInput.setError(null);
            binding.emailInput.setError(null);
            binding.phoneInput.setError(null);
            binding.birthdayInput.setError(null);

            // 1. Required field check
            if (username.isEmpty()) {
                binding.usernameInput.setError("Name is required");
                hasError = true;
            }

            // 2. Valid email check
            if (email.isEmpty()) {
                binding.emailInput.setError("Email is required");
                hasError = true;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInput.setError("Invalid email format");
                hasError = true;
            }

            // 3. Phone number must be at least 9 digits
            if (phone.isEmpty()) {
                binding.phoneInput.setError("Phone number is required");
                hasError = true;
            } else if (phone.length() < 9) {
                binding.phoneInput.setError("Phone must be at least 9 digits");
                hasError = true;
            }

            // 4. Birthday required
            if (birthday.isEmpty()) {
                binding.birthdayInput.setError("Birthday is required");
                hasError = true;
            }

            if (hasError) return;

            // Save to SharedPreferences
            prefs.edit()
                    .putString("username", username)
                    .putString("email", email)
                    .putString("phone", phone)
                    .putString("birthday", birthday)
                    .apply();

            Toast.makeText(getContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();

        });

        binding.clearButton.setOnClickListener(v -> {
            binding.usernameInput.setText("");
            binding.emailInput.setText("");
            binding.phoneInput.setText("");
            binding.birthdayInput.setText("");

            binding.usernameInput.setError(null);
            binding.emailInput.setError(null);
            binding.phoneInput.setError(null);
            binding.birthdayInput.setError(null);

            prefs.edit().clear().apply();//clear data from shared pref

            Toast.makeText(getContext(), "Fields cleared", Toast.LENGTH_SHORT).show();
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
