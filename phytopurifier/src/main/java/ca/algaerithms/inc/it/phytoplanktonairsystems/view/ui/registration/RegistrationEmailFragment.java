/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationCallback;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.ValidationUtils;

public class RegistrationEmailFragment extends Fragment {

    private RegistrationController controller;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registrationEmail_editText = view.findViewById(R.id.registrationEmail_editText);
        registrationEmail_continueButton = view.findViewById(R.id.registrationEmail_continueButton);
        controller = new RegistrationController();

        registrationEmail_continueButton.setOnClickListener(v -> {
            if (!isConnectedToInternet()) {
                registrationEmail_editText.setError(getString(R.string.no_internet_error));
                registrationEmail_editText.requestFocus();
                return;
            }
            email = registrationEmail_editText.getText().toString().trim();

            //Method call
            if (ValidationUtils.isEmailEmpty(email)) {
                registrationEmail_editText.setError(getString(R.string.email_is_required));
                registrationEmail_editText.requestFocus();
                return;
            }

            if (!ValidationUtils.isEmailFormatValid(email)) {
                registrationEmail_editText.setError(getString(R.string.invalid_email_format));
                registrationEmail_editText.requestFocus();
                return;
            }

            registrationEmail_continueButton.setEnabled(false);

            controller.registerEmail(email, TEMP_PASSWORD, new RegistrationCallback() {
                @Override
                public void onVerificationEmailSent() {
                    showVerificationSnackbar();
                    checkEmailVerificationLoop();
                }

                @Override
                public void onFailure(String message) {
                    registrationEmail_continueButton.setEnabled(true);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void checkEmailVerificationLoop() {
        controller.checkEmailVerification(isVerified -> {
            if (isVerified) {
                if (verificationSnackbar != null && verificationSnackbar.isShown()) {
                    verificationSnackbar.dismiss();
                }
                Toast.makeText(getContext(), R.string.email_verified_you_can_continue, Toast.LENGTH_SHORT).show();
                nextFragment();
            } else {
                registrationEmail_editText.postDelayed(this::checkEmailVerificationLoop, 3000);
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

    // Internet Check
    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void nextFragment() {
        RegistrationController.goToName(requireActivity(), email);
    }
}