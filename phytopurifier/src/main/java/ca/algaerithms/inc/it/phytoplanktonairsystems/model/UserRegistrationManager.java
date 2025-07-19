package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import com.google.firebase.auth.FirebaseAuth;

public class UserRegistrationManager {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public interface EmailCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void sendVerificationEmail(String email, String password, EmailCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().sendEmailVerification()
                                .addOnSuccessListener(unused -> callback.onSuccess())
                                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }
}

