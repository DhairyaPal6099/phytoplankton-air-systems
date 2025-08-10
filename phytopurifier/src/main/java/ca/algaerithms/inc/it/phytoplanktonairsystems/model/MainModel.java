package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainModel {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private final Context context;

    public MainModel(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void fetchUserName(String uid, OnNameLoaded callback) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    String name = document.getString("name");
                    callback.onNameLoaded(name);
                })
                .addOnFailureListener(e -> callback.onNameLoaded(null));
    }

    public void logout(OnLogoutCallback callback) {
        auth.signOut();
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("triggered_worker").apply();
        callback.onLogoutComplete();
    }

    public interface OnNameLoaded {
        void onNameLoaded(String name);
    }

    public interface OnLogoutCallback {
        void onLogoutComplete();
    }
}
