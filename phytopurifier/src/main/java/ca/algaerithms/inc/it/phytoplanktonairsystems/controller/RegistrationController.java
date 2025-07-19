package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationController {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void registerEmail(String email, String tempPassword, RegistrationCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, tempPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnSuccessListener(unused -> callback.onVerificationEmailSent())
                                    .addOnFailureListener(e -> callback.onFailure("Failed to send verification email."));
                        }
                    } else {
                        String msg = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        callback.onFailure(msg);
                    }
                });
    }

    public void checkEmailVerification(VerificationStatusCallback callback) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnSuccessListener(unused -> callback.onChecked(user.isEmailVerified()))
                    .addOnFailureListener(e -> callback.onChecked(false));
        } else {
            callback.onChecked(false);
        }
    }

    public interface VerificationStatusCallback {
        void onChecked(boolean isVerified);
    }
}