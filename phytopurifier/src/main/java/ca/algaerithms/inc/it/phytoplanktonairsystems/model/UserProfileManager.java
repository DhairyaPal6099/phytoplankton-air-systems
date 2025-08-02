package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class UserProfileManager {
    private Context context;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getUserProfile(OnUserDataLoadedListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            listener.onFailure(new Exception(context.getString(R.string.user_not_authenticated)));
            return;
        }

        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        listener.onSuccess(snapshot.getData(), user.getEmail());
                    } else {
                        listener.onFailure(new Exception(context.getString(R.string.user_not_found)));
                    }
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void updateUserProfile(Map<String, Object> updates, OnUserUpdateListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            listener.onFailure(new Exception((context.getString(R.string.user_not_authenticated))));
            return;
        }

        db.collection("users").document(user.getUid()).update(updates)
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(listener::onFailure);
    }

    public void requestEmailUpdate(String newEmail, OnEmailVerificationListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            listener.onFailure(new Exception((context.getString(R.string.user_not_authenticated))));
            return;
        }

        user.verifyBeforeUpdateEmail(newEmail)
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(listener::onFailure);
    }

    public void signOut() {
        auth.signOut();
    }

    public interface OnUserDataLoadedListener {
        void onSuccess(Map<String, Object> data, String authEmail);
        void onFailure(Exception e);
    }

    public interface OnUserUpdateListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnEmailVerificationListener {
        void onSuccess();
        void onFailure(Exception e);
    }
}