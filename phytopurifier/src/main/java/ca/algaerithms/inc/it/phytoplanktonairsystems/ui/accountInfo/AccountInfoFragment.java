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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;

public class AccountInfoFragment extends Fragment {

    private FragmentAccountInfoBinding binding;
    private FirebaseUser currentUser;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String originalEmail;
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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), R.string.user_not_authenticated, Toast.LENGTH_SHORT).show();
            return;
        }

        loadUserInfo();

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

        binding.saveButton.setOnClickListener(v -> saveUserInfo());
        binding.clearButton.setOnClickListener(v -> loadUserInfo());
        binding.profileImage.setOnClickListener(v -> requestGalleryPermission());
    }

    private void loadUserInfo() {
        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        binding.usernameInput.setText(snapshot.getString("name"));
                        binding.emailInput.setText(snapshot.getString("email"));
                        binding.phoneInput.setText(snapshot.getString("phone"));
                        binding.birthdayInput.setText(snapshot.getString("birthdate"));
                        originalEmail = snapshot.getString("email");
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show());
    }

    private void saveUserInfo() {
        String name = binding.usernameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String phone = binding.phoneInput.getText().toString().trim();
        String birthday = binding.birthdayInput.getText().toString().trim();

        boolean hasError = false;

        binding.usernameInput.setError(null);
        binding.emailInput.setError(null);
        binding.phoneInput.setError(null);
        binding.birthdayInput.setError(null);

        if (name.isEmpty()) {
            binding.usernameInput.setError(getString(R.string.name_required));
            hasError = true;
        }

        if (email.isEmpty()) {
            binding.emailInput.setError(getString(R.string.email_required));
            hasError = true;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError(getString(R.string.invalid_email));
            hasError = true;
        }

        if (phone.isEmpty()) {
            binding.phoneInput.setError(getString(R.string.phone_required));
            hasError = true;
        } else if (phone.length() < 9) {
            binding.phoneInput.setError(getString(R.string.phone_must_be_at_least_9_digits));
            hasError = true;
        }

        if (birthday.isEmpty()) {
            binding.birthdayInput.setError(getString(R.string.birthday_required));
            hasError = true;
        }

        if (hasError || currentUser == null) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        updates.put("birthdate", birthday);

        if (!email.equals(originalEmail)) {
            currentUser.verifyBeforeUpdateEmail(email)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(),
                                "A verification email has been sent to " + email +
                                        ". After verifying, please reopen this screen to apply changes.",
                                Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Failed to send verification: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .update(updates)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(), R.string.details_saved, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), R.string.error_saving_user_data, Toast.LENGTH_SHORT).show());
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
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