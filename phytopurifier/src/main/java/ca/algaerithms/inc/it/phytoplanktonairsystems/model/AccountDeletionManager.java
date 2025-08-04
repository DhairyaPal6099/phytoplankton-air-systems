package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountDeletionManager {
    public interface DeleteCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public interface ReauthCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean isGoogleUser(FirebaseUser user) {
        return user.getProviderData().get(1).getProviderId().equals("google.com");
    }

    public void reauthenticateUser(FirebaseUser user, String password, ReauthCallback callback) {
        if (user == null || user.getEmail() == null) {
            callback.onFailure("Invalid user or email.");
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) callback.onSuccess();
                    else callback.onFailure("Reauthentication failed.");
                });
    }

    public void deleteUserAccount(FirebaseUser user, DeleteCallback callback) {
        String uid = user.getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid).delete()
                .addOnCompleteListener(task1 -> FirebaseDatabase.getInstance().getReference("sensor_readings").child(uid).removeValue()
                        .addOnCompleteListener(task2 -> user.delete()
                                .addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) callback.onSuccess();
                                    else callback.onFailure("Failed to delete account.");
                                })));
    }
}