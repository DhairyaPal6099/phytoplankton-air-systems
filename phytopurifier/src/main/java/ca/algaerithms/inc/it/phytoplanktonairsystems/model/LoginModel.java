package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;
import android.util.Patterns;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class LoginModel {
    private final Context context;  // make it final for clarity
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LoginModel(Context context) {
        this.context = context.getApplicationContext();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signInWithEmailPassword(String email, String password,
                                        Consumer<String> onSuccessWithName,
                                        Consumer<String> onError) {
        if (email.isEmpty()) {
            onError.accept(context.getString(R.string.email_is_required));
            return;
        }

        if (password.isEmpty()) {
            onError.accept(context.getString(R.string.password_is_required));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(snapshot -> {
                                        if (snapshot.exists()) {
                                            String name = snapshot.getString("name");
                                            onSuccessWithName.accept(name != null ? name : "User");
                                        } else {
                                            onSuccessWithName.accept("User");
                                        }
                                    })
                                    .addOnFailureListener(e -> onSuccessWithName.accept("User"));
                        } else {
                            onSuccessWithName.accept("User");
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidCredentialsException
                                || exception instanceof FirebaseAuthInvalidUserException) {
                            onError.accept(context.getString(R.string.invalid_email_or_password));
                        } else {
                            onError.accept(context.getString(R.string.login_failed_please_try_again));
                        }
                    }
                });
    }


    public void sendPasswordReset(String email,
                                  Consumer<String> onSuccess,
                                  Consumer<String> onError) {
        if (email.isEmpty()) {
            onError.accept(context.getString(R.string.email_cannot_be_empty));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError.accept(context.getString(R.string.please_enter_a_valid_email));
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> onSuccess.accept(email))
                .addOnFailureListener(e -> onError.accept("Failed to send reset link: " + e.getMessage()));
    }

    public void signInWithGoogleCredential(@NonNull AuthCredential credential,
                                           BiConsumer<Boolean, FirebaseUser> onComplete,
                                           Consumer<String> onError) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        onComplete.accept(true, user);
                    } else {
                        onError.accept(task.getException() != null ? task.getException().getMessage() : "Google sign-in failed.");
                        onComplete.accept(false, null);
                    }
                });
    }

    public void saveGoogleUserToFirestore(FirebaseUser user,
                                          String name,
                                          String email,
                                          String phone,
                                          Runnable onSuccess,
                                          Consumer<String> onError) {

        String uid = user.getUid();
        List<Map<String, Object>> achievements = new ArrayList<>();
        List<Map<String, Object>> notifications = new ArrayList<>();
        int lifetime_co2_converted = 0;

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("phone", phone != null ? phone : "N/A");
        userMap.put("birthdate", "N/A");
        userMap.put("achievements", achievements);
        userMap.put("notifications", notifications);
        userMap.put("lifetime_co2_converted", lifetime_co2_converted);

        db.collection("users")
                .document(uid)
                .set(userMap)
                .addOnSuccessListener(unused -> onSuccess.run())
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }

    public void signOut(boolean signOutFromGoogle) {
        FirebaseAuth.getInstance().signOut();
        if (signOutFromGoogle) {
            GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        }
    }

    public boolean isGoogleSignedInUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            for (UserInfo info : user.getProviderData()) {
                if (GoogleAuthProvider.PROVIDER_ID.equals(info.getProviderId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
