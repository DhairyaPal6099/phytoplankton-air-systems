package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class RegistrationEmailFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String email;
    private Button registrationEmail_continueButton;
    private EditText registrationEmail_editText;

    private Snackbar verificationSnackbar;

    private static final String TEMP_PASSWORD = "Absjdbcsibeskbd52654";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_email, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        registrationEmail_editText = view.findViewById(R.id.registrationEmail_editText);
        registrationEmail_continueButton = view.findViewById(R.id.registrationEmail_continueButton);

        registrationEmail_continueButton.setOnClickListener(v -> {
            email = registrationEmail_editText.getText().toString().trim();

            if (email.isEmpty()) {
                registrationEmail_editText.setError(getString(R.string.email_is_required));
                registrationEmail_editText.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                registrationEmail_editText.setError(getString(R.string.invalid_email_format));
                registrationEmail_editText.requestFocus();
                return;
            }

            createUserAndSendVerification(email);
        });

        return view;
    }

    private void createUserAndSendVerification(String email) {
        registrationEmail_continueButton.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(email, TEMP_PASSWORD)
                .addOnCompleteListener(requireActivity(), task -> {
                    registrationEmail_continueButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            showVerificationSnackbar();
                                            checkEmailVerification(); // Start checking loop
                                        } else {
                                            Toast.makeText(getContext(), R.string.failed_to_send_verification_email, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Exception e = task.getException();
                        if (e != null && e.getMessage() != null &&
                                e.getMessage().toLowerCase().contains(getString(R.string.email_address_is_already_in_use))) {
                            Toast.makeText(getContext(), R.string.this_email_is_already_registered_please_use_a_different_email_or_login, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.registration_failed) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showVerificationSnackbar() {
        if (verificationSnackbar == null || !verificationSnackbar.isShown()) {
            verificationSnackbar = Snackbar.make(requireView(),
                            R.string.verification_link_check_text,
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, snackbarView -> verificationSnackbar.dismiss());
            verificationSnackbar.show();
        }
    }

    private void checkEmailVerification() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (user.isEmailVerified()) {
                        if (verificationSnackbar != null && verificationSnackbar.isShown()) {
                            verificationSnackbar.dismiss();
                        }
                        Toast.makeText(getContext(), R.string.email_verified_you_can_continue, Toast.LENGTH_SHORT).show();
                        nextFragment();
                    } else {
                        // Try again in 3 seconds
                        registrationEmail_editText.postDelayed(this::checkEmailVerification, 3000);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.failed_to_reload_user, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void nextFragment() {
        // Passing email forward to next fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        RegistrationNameFragment nameFragment = new RegistrationNameFragment();
        nameFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.registration_fragment_container, nameFragment)
                .addToBackStack(null)
                .commit();
    }
}