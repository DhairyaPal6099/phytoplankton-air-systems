/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.function.Consumer;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.LoginModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class LoginController {
    private final Context context;
    private final SharedPreferences prefs;
    private final LoginModel model;
    private final GoogleSignInClient googleSignInClient;

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    public LoginController(Context context) {
        this.context = context;
        this.model = new LoginModel(context);
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }

    public boolean shouldAutoLogin() {
        boolean rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false);
        boolean isGoogleUser = GoogleSignIn.getLastSignedInAccount(context) != null;
        return model.getCurrentUser() != null && (rememberMe || isGoogleUser);
    }

    public void signOutIfNeeded() {
        if (!shouldAutoLogin()) {
            boolean isGoogleUser = model.isGoogleSignedInUser();
            model.signOut(isGoogleUser);
        }
    }

    public void signInWithEmail(String email, String password, boolean rememberMe,
                                Activity activity,
                                Consumer<String> onSuccessWithName,
                                Consumer<String> onError) {
        model.signInWithEmailPassword(email, password, userName -> {
            // Save the rememberMe value here:
            prefs.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply();

            onSuccessWithName.accept(userName);
        }, onError);
    }


    public void sendPasswordReset(String email, Consumer<String> onSuccess, Consumer<String> onError) {
        model.sendPasswordReset(email, onSuccess, onError);
    }

    public void handleGoogleSignIn(String idToken, Activity activity,
                                   Runnable onSuccess,
                                   Consumer<String> onError) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        model.signInWithGoogleCredential(credential, (success, user) -> {
            if (success && user != null) {
                model.saveGoogleUserToFirestore(user,
                        user.getDisplayName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        () -> {
                            onSuccess.run();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        },
                        errorMsg -> onError.accept(context.getString(R.string.firestore_error) + errorMsg));
            } else {
                onError.accept(context.getString(R.string.authentication_failed));
            }
        }, onError);
    }
}