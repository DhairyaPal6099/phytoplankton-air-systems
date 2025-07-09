/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.accountInfo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;

public class AccountInfoFragment extends Fragment {

    private FragmentAccountInfoBinding binding;
    private SharedPreferences prefs;

    private int permissionRequestCount = 0;
    private static final int MAX_PERMISSION_ATTEMPTS = 2;

    // Launcher for requesting permission to read media images from storage
    // Must be declared at class level to be lifecycle-aware and reusable across methods
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    permissionRequestCount = 0;
                    Snackbar.make(binding.getRoot(), getString(R.string.permission_granted), Snackbar.LENGTH_SHORT).show();
                    openGallery();
                } else {
                    permissionRequestCount++;
                    if (permissionRequestCount > MAX_PERMISSION_ATTEMPTS) {
                        Snackbar.make(binding.getRoot(), getString(R.string.permission_denied_open_settings_to_allow), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.settings), v -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                                .show();
                    } else {
                        Snackbar.make(binding.getRoot(), getString(R.string.gallery_permission_denied), Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

    // Launcher for handling the result of image picking from gallery
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

        prefs = requireContext().getSharedPreferences(getString(R.string.account_info_title), Context.MODE_PRIVATE);

        // Load saved values
        binding.usernameInput.setText(prefs.getString(getString(R.string.account_info_username), getString(R.string.empty_string)));
        binding.emailInput.setText(prefs.getString(getString(R.string.account_info_email), getString(R.string.empty_string)));
        binding.phoneInput.setText(prefs.getString(getString(R.string.account_info_phone), getString(R.string.empty_string)));
        binding.birthdayInput.setText(prefs.getString(getString(R.string.account_info_birthday), getString(R.string.empty_string)));

        // Date Picker
        binding.birthdayInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, day) -> {
                        String formattedDate = String.format(getString(R.string.birthdate_format), year, month + 1, day);
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
                binding.usernameInput.setError(getString(R.string.name_required));
                hasError = true;
            }

            // 2. Valid email check
            if (email.isEmpty()) {
                binding.emailInput.setError(getString(R.string.email_required));
                hasError = true;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInput.setError(getString(R.string.invalid_email));
                hasError = true;
            }

            // 3. Phone number must be at least 9 digits
            if (phone.isEmpty()) {
                binding.phoneInput.setError(getString(R.string.phone_required));
                hasError = true;
            } else if (phone.length() < 9) {
                binding.phoneInput.setError(getString(R.string.phone_must_be_at_least_9_digits));
                hasError = true;
            }

            // 4. Birthday required
            if (birthday.isEmpty()) {
                binding.birthdayInput.setError(getString(R.string.birthday_required));
                hasError = true;
            }

            if (hasError) return;

            // Save to SharedPreferences
            prefs.edit()
                    .putString(getString(R.string.username), username)
                    .putString(getString(R.string.email_lowercase), email)
                    .putString(getString(R.string.phone_lowercase), phone)
                    .putString(getString(R.string.birthday_lowercase), birthday)
                    .apply();

            Toast.makeText(getContext(), getString(R.string.details_saved), Toast.LENGTH_SHORT).show();
        });

        // Clear Button
        binding.clearButton.setOnClickListener(v -> {
            binding.usernameInput.setText(getString(R.string.empty_string));
            binding.emailInput.setText(getString(R.string.empty_string));
            binding.phoneInput.setText(getString(R.string.empty_string));
            binding.birthdayInput.setText(getString(R.string.empty_string));

            binding.usernameInput.setError(null);
            binding.emailInput.setError(null);
            binding.phoneInput.setError(null);
            binding.birthdayInput.setError(null);

            // Reset profile image to default
            binding.profileImage.setImageResource(R.drawable.profile);

            prefs.edit().clear().apply();

            Toast.makeText(getContext(), getString(R.string.fields_cleared), Toast.LENGTH_SHORT).show();
        });

        // Profile Image Click
        binding.profileImage.setOnClickListener(v -> requestGalleryPermission());
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(binding.getRoot(), getString(R.string.permission_granted), Snackbar.LENGTH_SHORT).show();
            openGallery();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(getString(R.string.image_forwardslash_asterisk));
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}