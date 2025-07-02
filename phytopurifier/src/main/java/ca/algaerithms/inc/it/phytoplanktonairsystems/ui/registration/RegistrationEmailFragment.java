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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class RegistrationEmailFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private String email;
    private Button registrationEmail_continueButton, registrationVerifyOtp_button;
    private EditText registrationEmail_editText;

    private static final String TEMP_PASSWORD = "Absjdbcsibeskbd52654";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_email, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        registrationEmail_editText = view.findViewById(R.id.registrationEmail_editText);
        registrationEmail_continueButton = view.findViewById(R.id.registrationEmail_continueButton);
        registrationVerifyOtp_button = view.findViewById(R.id.registrationVerifyOtp_button);

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

        registrationVerifyOtp_button.setOnClickListener(v -> checkEmailVerification());

        return view;
    }

    private void createUserAndSendVerification(String email) {
        registrationEmail_continueButton.setEnabled(false);

        firebaseAuth.createUserWithEmailAndPassword(email, TEMP_PASSWORD)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Toast.makeText(getContext(), R.string.verification_email_sent, Toast.LENGTH_SHORT).show();
                                            registrationVerifyOtp_button.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(getContext(), R.string.failed_to_send_verification_email, Toast.LENGTH_SHORT).show();
                                        }
                                        registrationEmail_continueButton.setEnabled(true);
                                    });
                        }
                    } else {
                        registrationEmail_continueButton.setEnabled(true);
                        Exception e = task.getException();
                        if (e != null && e.getMessage() != null &&
                                e.getMessage().toLowerCase().contains(getString(R.string.email_address_is_already_in_use))) {
                            Toast.makeText(getContext(), R.string.this_email_is_already_registered_please_use_a_different_email_or_login, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), R.string.registration_failed + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (user.isEmailVerified()) {
                    Toast.makeText(getContext(), R.string.email_verified_you_can_continue, Toast.LENGTH_SHORT).show();
                    nextFragment();
                } else {
                    Toast.makeText(getContext(), R.string.email_not_verified_yet_please_check_your_inbox, Toast.LENGTH_SHORT).show();
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