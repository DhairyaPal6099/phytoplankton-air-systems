package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Patterns;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.UserProfileManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.LoginActivity;

public class AccountInfoController {
    private final FragmentAccountInfoBinding binding;
    private final Context context;
    private final Activity activity;
    private final PermissionCallback permissionCallback;
    private final UserProfileManager model;
    private String originalEmail;
    private int permissionRequestCount = 0;
    private static final int MAX_PERMISSION_ATTEMPTS = 2;

    public interface PermissionCallback {
        void requestPermission(String permission);
        void launchGalleryIntent(Intent intent);
    }

    public AccountInfoController(FragmentAccountInfoBinding binding, Context context, Activity activity, PermissionCallback callback) {
        this.binding = binding;
        this.context = context;
        this.activity = activity;
        this.permissionCallback = callback;
        this.model = new UserProfileManager();
    }

    public void loadUserInfo() {
        model.getUserProfile(new UserProfileManager.OnUserDataLoadedListener() {
            @Override
            public void onSuccess(Map<String, Object> data, String authEmail) {
                binding.usernameInput.setText((String) data.get("name"));
                binding.emailInput.setText((String) data.get("email"));
                binding.phoneInput.setText((String) data.get("phone"));
                originalEmail = (String) data.get("email");

                if (authEmail != null && !authEmail.equals(originalEmail)) {
                    Map<String, Object> update = new HashMap<>();
                    update.put("email", authEmail);
                    model.updateUserProfile(update, new UserProfileManager.OnUserUpdateListener() {
                        @Override
                        public void onSuccess() {
                            originalEmail = authEmail;
                            Snackbar.make(binding.getRoot(), R.string.email_verified, Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            showToast(R.string.failed_to_update, e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToast(R.string.failed_to_load_user_data, null);
            }
        });
    }

    public void saveUserInfo() {
        String name = binding.usernameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String phone = binding.phoneInput.getText().toString().trim();

        boolean hasError = false;
        clearErrors();

        if (name.isEmpty()) {
            binding.usernameInput.setError(context.getString(R.string.name_required));
            hasError = true;
        }

        if (email.isEmpty()) {
            binding.emailInput.setError(context.getString(R.string.email_required));
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.setError(context.getString(R.string.invalid_email));
            hasError = true;
        }

        if (phone.isEmpty()) {
            binding.phoneInput.setError(context.getString(R.string.phone_required));
            hasError = true;
        } else if (phone.length() < 9) {
            binding.phoneInput.setError(context.getString(R.string.phone_must_be_at_least_10_digits));
            hasError = true;
        }

        if (hasError) return;

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);

        if (!email.equals(originalEmail)) {
            model.requestEmailUpdate(email, new UserProfileManager.OnEmailVerificationListener() {
                @Override
                public void onSuccess() {
                    Snackbar.make(binding.getRoot(), context.getString(R.string.verification_sent_please_log_out_and_log_back_in_to_update_your_email), Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.log_out, v -> {
                                model.signOut();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            })
                            .show();
                }

                @Override
                public void onFailure(Exception e) {
                    showToast(R.string.failed_to_send_verification, e.getMessage());
                }
            });
            return;
        }

        model.updateUserProfile(updates, new UserProfileManager.OnUserUpdateListener() {
            @Override
            public void onSuccess() {
                showToast(R.string.details_saved, null);
            }

            @Override
            public void onFailure(Exception e) {
                showToast(R.string.error_saving_user_data, null);
            }
        });
    }

    public void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            permissionRequestCount++;
            if (permissionRequestCount > MAX_PERMISSION_ATTEMPTS) {
                Snackbar.make(binding.getRoot(), context.getString(R.string.permission_denied_open_settings_to_allow), Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.settings), v -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        })
                        .show();
            } else {
                permissionCallback.requestPermission(android.Manifest.permission.READ_MEDIA_IMAGES);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(context.getString(R.string.image_forwardslash_asterisk));
        permissionCallback.launchGalleryIntent(intent);
    }

    private void clearErrors() {
        binding.usernameInput.setError(null);
        binding.emailInput.setError(null);
        binding.phoneInput.setError(null);
    }

    private void showToast(int messageResId, String errorDetail) {
        String message = context.getString(messageResId);
        if (errorDetail != null) {
            message += ": " + errorDetail;
        }
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
