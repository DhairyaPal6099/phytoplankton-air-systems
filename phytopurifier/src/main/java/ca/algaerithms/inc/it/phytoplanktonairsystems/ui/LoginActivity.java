package ca.algaerithms.inc.it.phytoplanktonairsystems.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;
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

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    private TextView errorTextView;
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginSubmitButton, forgotPasswordButton, createAccountButton;
    private SignInButton googleSignInButton;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    // Launcher for Google Sign-In Intent result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
                    if (account != null && account.getIdToken() != null) {
                        firebaseAuthWithGoogle(account.getIdToken());
                    } else {
                        Toast.makeText(this, R.string.google_sign_in_failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.google_sign_in_cancelled, Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // SharedPreferences init
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        errorTextView = findViewById(R.id.login_errorTextView);
        rememberMeCheckBox = findViewById(R.id.login_rememberMeCheckBox);
        loginSubmitButton = findViewById(R.id.login_button);
        createAccountButton = findViewById(R.id.login_createAccountButton);
        forgotPasswordButton = findViewById(R.id.login_forgotPassword);
        googleSignInButton = findViewById(R.id.btn_google_sign_in);
        emailEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_Password);

        // Always start with checkbox cleared
        rememberMeCheckBox.setChecked(false);

        setupPasswordToggle();
        //Login button logic
        loginButtonClick();

        // Forgot password
        forgotPasswordButton.setOnClickListener(v -> showForgotPasswordDialog());

        // Create an account
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))  // your OAuth 2.0 client ID from Firebase console
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(v -> {
            // Sign out first to trigger account chooser
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                signInLauncher.launch(signInIntent);
            });
        });

        // Optional: Remove text if you just want the "G" icon
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setText(R.string.sign_in_with_google); // remove the "Sign in" text
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            boolean rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false);
            boolean isGoogleUser = GoogleSignIn.getLastSignedInAccount(this) != null;

            if (isGoogleUser || rememberMe) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                mAuth.signOut(); // Sign out email/password users who didn't check Remember Me
            }
        }
    }

    // Forgot password button login
    private void showForgotPasswordDialog(){
        EditText emailInput = new EditText(this);
        emailInput.setHint(R.string.enter_your_email);
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setPadding(50, 40, 50, 40);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.forgot_password)
                .setMessage(R.string.enter_your_registered_email_to_receive_a_reset_link)
                .setView(emailInput)
                .setPositiveButton(getString(R.string.send), null)
                .setNegativeButton(getString(R.string.cancel), (d, w) -> d.dismiss())
                .create();

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnShowListener(dlg -> {
            Button sendButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            sendButton.setOnClickListener(v -> {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    emailInput.setError(getString(R.string.email_cannot_be_empty));
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError(getString(R.string.please_enter_a_valid_email));
                    return;
                }

                // Directly send reset email
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(unused -> {
                            Snackbar.make(findViewById(android.R.id.content),
                                    getString(R.string.password_reset_link_sent_to) + email,
                                    Snackbar.LENGTH_LONG).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            // For example: invalid-email format or network failure
                            emailInput.setError(getString(R.string.failed_to_send_reset_link) + e.getMessage());
                        });
            });
        });

        dialog.show();
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
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

                            // Create user info map
                            Map<String, Object> userMap = new HashMap<>();
                            if (name != null) userMap.put("name", name);
                            if (email != null) userMap.put("email", email);
                            userMap.put("birthdate", "N/A");
                            userMap.put("phone", phone != null ? phone : "N/A"); //checks if birthday is available in Google account
                            userMap.put("achievements", achievements);
                            userMap.put("notifications", notifications);
                            userMap.put("lifetime_co2_converted", lifetime_co2_converted);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, getString(R.string.welcome) + name, Toast.LENGTH_SHORT).show();
                                        // Navigate to MainActivity after saving
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, getString(R.string.failed_to_save_user_info) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.firebase_auth_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginButtonClick() {

        // Login button click
        loginSubmitButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            errorTextView.setVisibility(View.GONE);
            emailEditText.setError(null);
            passwordEditText.setError(null);

            if (email.isEmpty()) {
                emailEditText.setError(getString(R.string.email_is_required));
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError(getString(R.string.password_is_required));
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Save Remember Me choice
                            prefs.edit().putBoolean(KEY_REMEMBER_ME, rememberMeCheckBox.isChecked()).apply();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidCredentialsException ||
                                    exception instanceof FirebaseAuthInvalidUserException) {
                                errorTextView.setText(R.string.invalid_email_or_password);
                                errorTextView.setVisibility(View.VISIBLE);
                            } else {
                                errorTextView.setText(R.string.login_failed_please_try_again);
                                errorTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    });

            View.OnFocusChangeListener clearErrorOnFocus = (v, hasFocus) -> {
                if (hasFocus && errorTextView.getVisibility() == View.VISIBLE) {
                    errorTextView.setVisibility(View.GONE);
                }
            };

            emailEditText.setOnFocusChangeListener(clearErrorOnFocus);
            passwordEditText.setOnFocusChangeListener(clearErrorOnFocus);

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordToggle() {
        EditText passwordEditText = findViewById(R.id.login_Password);

        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                final int DRAWABLE_END = 2;
                if (passwordEditText.getCompoundDrawables()[DRAWABLE_END] != null) {
                    int drawableWidth = passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width();
                    int touchAreaStart = passwordEditText.getRight() - passwordEditText.getPaddingRight() - drawableWidth;

                    if (event.getRawX() >= touchAreaStart) {
                        togglePasswordVisibility(passwordEditText);
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility(EditText passwordEditText) {
        int inputType = passwordEditText.getInputType();
        if ((inputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_on, 0);
        } else {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}