package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class UserSettingsManager {

    private final SharedPreferences prefs;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final Context context;

    public interface OnUserDataFetched {
        void onUserData(UserModel user);
        void onError(String error);
    }

    public UserSettingsManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(
                context.getString(R.string.settings_lowercase), Context.MODE_PRIVATE);
    }

    public void fetchUserData(OnUserDataFetched callback) {
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        UserModel user = new UserModel();
                        user.setName(doc.getString("name"));
                        user.setEmail(doc.getString("email"));
                        user.setPhone(doc.getString("phone"));
                        callback.onUserData(user);
                    } else {
                        callback.onError("User not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(context.getString(R.string.dark_mode), false);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.dark_mode), enabled).apply();
    }

    public boolean isLockPortrait() {
        return prefs.getBoolean(context.getString(R.string.lockportrait), false);
    }

    public void setLockPortrait(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.lockportrait), enabled).apply();
    }

    public boolean isReduceMotion() {
        return prefs.getBoolean(context.getString(R.string.reduce_motion_key), false);
    }

    public void setReduceMotion(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.reduce_motion_key), enabled).apply();
    }
}