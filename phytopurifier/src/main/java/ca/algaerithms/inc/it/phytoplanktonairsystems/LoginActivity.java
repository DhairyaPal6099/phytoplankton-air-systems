package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView errorTextView;
    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginSubmitButton, createAccountButton;
    private SignInButton googleSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        errorTextView = findViewById(R.id.login_errorTextView);
        rememberMeCheckBox = findViewById(R.id.login_rememberMeCheckBox);
        loginSubmitButton = findViewById(R.id.login_button);
        createAccountButton = findViewById(R.id.login_createAccountButton);
        googleSignInButton = findViewById(R.id.btn_google_sign_in);
        emailEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_Password);


        // Login button click
        loginSubmitButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            errorTextView.setVisibility(View.GONE);
            emailEditText.setError(null);
            passwordEditText.setError(null);

            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthInvalidCredentialsException ||
                                    exception instanceof FirebaseAuthInvalidUserException) {
                                errorTextView.setText(" Invalid email or password");
                                errorTextView.setVisibility(View.VISIBLE);
                            } else {
                                errorTextView.setText("Login failed. Please try again.");
                                errorTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        });



        createAccountButton = findViewById(R.id.login_createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        googleSignInButton = findViewById(R.id.btn_google_sign_in);

// Optional: Remove text if you just want the "G" icon
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setText(R.string.sign_in_with_google); // remove the "Sign in" text
            }
        }

        setupPasswordToggle();
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