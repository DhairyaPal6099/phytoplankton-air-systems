/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.LoginController;

public class LoginActivity extends AppCompatActivity {

    private LoginController loginController;

    private TextView errorTextView;
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginSubmitButton, forgotPasswordButton, createAccountButton;
    private SignInButton googleSignInButton;

    // Launcher for Google Sign-In Intent result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
                    if (account != null && account.getIdToken() != null) {
                        signInWithGoogleToken(account.getIdToken());
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

        loginController = new LoginController(this);

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

        googleSignInButton.setOnClickListener(v -> {

            if (!isConnectedToInternet()) {
                errorTextView.setText(R.string.no_internet_error); // Add this string in strings.xml
                errorTextView.setVisibility(View.VISIBLE);
                return;
            }

            // Sign out first to trigger account chooser
            loginController.getGoogleSignInClient().signOut().addOnCompleteListener(task -> {
                Intent signInIntent = loginController.getGoogleSignInClient().getSignInIntent();
                signInLauncher.launch(signInIntent);
            });
        });

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

        if (loginController.shouldAutoLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            loginController.signOutIfNeeded();
        }
    }

    private void signInWithGoogleToken(String idToken) {
        loginController.handleGoogleSignIn(
                idToken,
                this,
                () -> {
                    // On success, navigate to main screen
                    navigateToMain();
                },
                error -> {
                    // On error, show a toast
                    Toast.makeText(this, getString(R.string.google_sign_in_failed) + ": " + error, Toast.LENGTH_LONG).show();
                }
        );
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
                if (!isConnectedToInternet()) {
                    Toast.makeText(this,getString(R.string.no_internet_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = emailInput.getText().toString().trim();
                loginController.sendPasswordReset(email, sentEmail -> {
                    Snackbar.make(findViewById(android.R.id.content),
                            getString(R.string.password_reset_link_sent_to) + email,
                            Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }, emailInput::setError);
            });
        });

        dialog.show();
    }

    private void loginButtonClick() {
        loginSubmitButton.setOnClickListener(view -> {

            if (!isConnectedToInternet()) {
                errorTextView.setText(R.string.no_internet_error); // Add this string in strings.xml
                errorTextView.setVisibility(View.VISIBLE);
                return;
            }

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            errorTextView.setVisibility(View.GONE);
            emailEditText.setError(null);
            passwordEditText.setError(null);

            loginController.signInWithEmail(email, password, rememberMeCheckBox.isChecked(),
                    this,
                    userName -> {
                        Toast.makeText(this, getString(R.string.welcome) + ", " + userName + "!", Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    },
                    error -> {
                        errorTextView.setText(error);
                        errorTextView.setVisibility(View.VISIBLE);
                    });

        });

        View.OnFocusChangeListener clearErrorOnFocus = (v, hasFocus) -> {
            if (hasFocus && errorTextView.getVisibility() == View.VISIBLE) {
                errorTextView.setVisibility(View.GONE);
            }
        };

        emailEditText.setOnFocusChangeListener(clearErrorOnFocus);
        passwordEditText.setOnFocusChangeListener(clearErrorOnFocus);
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

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return false;
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}