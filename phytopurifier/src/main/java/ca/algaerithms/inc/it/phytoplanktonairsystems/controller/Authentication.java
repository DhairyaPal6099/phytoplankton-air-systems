/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class Authentication {
    private final Context context;
    private final FirebaseAuth mAuth;
    private final GoogleSignInClient googleSignInClient;
    private final SharedPreferences prefs;

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    public Authentication(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public boolean shouldAutoLogin() {
        boolean rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false);
        boolean isGoogleUser = GoogleSignIn.getLastSignedInAccount(context) != null;
        return getCurrentUser() != null && (isGoogleUser || rememberMe);
    }

    public void signOutIfNeeded() {
        if (!shouldAutoLogin()) {
            mAuth.signOut();
        }
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public void handleGoogleSignIn(String idToken, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String phone = user.getPhoneNumber(); // Often null

                            // Initialize empty lists and int
                            List<Map<String, Object>> achievements = new ArrayList<>();
                            List<Map<String, Object>> notifications = new ArrayList<>();
                            int lifetime_co2_converted = 0;
                            long feedbackDisabledTime = -1;

                            // Create user info map
                            Map<String, Object> userMap = new HashMap<>();
                            if (name != null) userMap.put(activity.getString(R.string.name), name);
                            if (email != null) userMap.put(activity.getString(R.string.email), email);
                            userMap.put(activity.getString(R.string.birthdate), activity.getString(R.string.n_a));
                            userMap.put(activity.getString(R.string.phone), phone != null ? phone : activity.getString(R.string.n_a)); //checks if birthday is available in Google account
                            userMap.put(activity.getString(R.string.achievements), achievements);
                            userMap.put(activity.getString(R.string.notifications_smallcase), notifications);
                            userMap.put(activity.getString(R.string.lifetime_co2_converted), lifetime_co2_converted);
                            userMap.put(activity.getString(R.string.feedback_disabled_time), feedbackDisabledTime);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(activity, activity.getString(R.string.welcome) + name, Toast.LENGTH_SHORT).show();
                                        // Navigate to MainActivity after saving
                                        activity.startActivity(new Intent(activity, MainActivity.class));
                                        activity.finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(activity, activity.getString(R.string.failed_to_save_user_info) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.firebase_auth_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sendPasswordReset(String email, Consumer<String> onSuccess, Consumer<String> onFailure) {
        if (email.isEmpty()) {
            onFailure.accept("Email cannot be empty.");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onFailure.accept("Please enter a valid email.");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> onSuccess.accept(email))
                .addOnFailureListener(e -> onFailure.accept("Failed to send reset link: " + e.getMessage()));
    }

    public void signInWithEmailPassword(String email, String password, boolean rememberMe, Activity activity, Consumer<Boolean> onResult, Consumer<String> onError) {
        if (email.isEmpty()) {
            onError.accept(activity.getString(R.string.email_is_required));
            return;
        }

        if (password.isEmpty()) {
            onError.accept(activity.getString(R.string.password_is_required));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prefs.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply();
                        onResult.accept(true);
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException || exception instanceof FirebaseAuthInvalidUserException) {
                            onError.accept(activity.getString(R.string.invalid_email_or_password));
                        } else {
                            onError.accept(activity.getString(R.string.login_failed_please_try_again));
                        }
                        onResult.accept(false);
                    }
                });
    }
}
