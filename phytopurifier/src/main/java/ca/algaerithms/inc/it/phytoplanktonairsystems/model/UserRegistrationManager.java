package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRegistrationManager {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Callback interface to notify result of email registration & verification
    public interface EmailCallback {
        void onSuccess();                    // Verification email sent
        void onFailure(String errorMessage); // Error during registration or sending email
    }

    // Callback interface to check email verification status
    public interface VerificationCallback {
        void onVerificationChecked(boolean isVerified);
    }

    // Callback interface for password update and Firestore save result
    public interface PasswordUpdateCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface EmailCheckCallback {
        void onResult(boolean exists);
        void onFailure(String errorMessage);
    }

    public interface UserCreationCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    // Check if email exists in Firebase Auth
    public void checkIfEmailExists(@NonNull String email, @NonNull EmailCheckCallback callback) {
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean exists = !task.getResult().getSignInMethods().isEmpty();
                        callback.onResult(exists);
                    } else {
                        callback.onFailure("Failed to check email: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    // Create user with email and temp password and send verification
    public void createUserWithEmail(@NonNull String email, @NonNull String password, @NonNull UserCreationCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnSuccessListener(unused -> callback.onSuccess())
                                    .addOnFailureListener(e -> callback.onFailure("Failed to send verification email: " + e.getMessage()));
                        } else {
                            callback.onFailure("User registration succeeded but user is null.");
                        }
                    } else {
                        callback.onFailure("Registration failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    // Checks if the currently logged-in user's email is verified
    public void checkEmailVerification(@NonNull VerificationCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.reload()
                    .addOnSuccessListener(unused -> callback.onVerificationChecked(user.isEmailVerified()))
                    .addOnFailureListener(e -> callback.onVerificationChecked(false));
        } else {
            callback.onVerificationChecked(false);
        }
    }

    // Updates password and saves user profile data to Firestore
    public void updatePasswordAndSaveUserData(@NonNull String email,
                                              @NonNull String name,
                                              @NonNull String birthdate,
                                              @NonNull String phone,
                                              int lifetime_co2_converted,
                                              @NonNull String password,
                                              @NonNull PasswordUpdateCallback callback) {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null && user.getEmail() != null && user.getEmail().equals(email)) {
            user.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = user.getUid();

                    // Default empty lists for achievements and notifications
                    List<Map<String, Object>> achievements = new ArrayList<>();
                    List<Map<String, Object>> notifications = new ArrayList<>();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", email);
                    userData.put("name", name);
                    userData.put("birthdate", birthdate);
                    userData.put("phone", phone);
                    userData.put("uid", uid);
                    userData.put("achievements", achievements);
                    userData.put("notifications", notifications);
                    userData.put("lifetime_co2_converted", lifetime_co2_converted);

                    db.collection("users").document(uid)
                            .set(userData)
                            .addOnSuccessListener(aVoid -> callback.onSuccess())
                            .addOnFailureListener(e -> callback.onFailure("Failed to save user data: " + e.getMessage()));
                } else {
                    callback.onFailure("Failed to update password: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                }
            });
        } else {
            callback.onFailure("User not authenticated or email mismatch.");
        }
    }

    // Deleting the user from the Registration Activity
    public void deleteTempUserIfExists(Runnable onDeleted) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> onDeleted.run());
        } else {
            onDeleted.run();
        }
    }
}