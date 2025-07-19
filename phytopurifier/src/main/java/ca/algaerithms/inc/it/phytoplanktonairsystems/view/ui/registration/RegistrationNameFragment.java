package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.ValidationUtils;

public class RegistrationNameFragment extends Fragment {

    private EditText firstNameEditText, lastNameEditText;
    private Button continueButton;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_name, container, false);

        firstNameEditText = view.findViewById(R.id.registration_diplayFirstName_editText);
        lastNameEditText = view.findViewById(R.id.registration_displayLastName_editText);
        continueButton = view.findViewById(R.id.registration_display_continueButton);

        // Get email from previous fragment
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        continueButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();

            //Method call
            if (!ValidationUtils.isValidName(firstName)) {
                firstNameEditText.setError(getString(R.string.first_name_is_required));
                firstNameEditText.requestFocus();
                return;
            }

            if (!ValidationUtils.isValidName(lastName)) {
                lastNameEditText.setError(getString(R.string.last_name_is_required));
                lastNameEditText.requestFocus();
                return;
            }

            String fullName = firstName + " " + lastName;

            // Passing email and full name to next fragment
            RegistrationController.goToBirthdate(requireActivity(), email, fullName);
        });

        return view;
    }
}