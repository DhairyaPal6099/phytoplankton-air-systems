package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

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

            if (TextUtils.isEmpty(firstName)) {
                firstNameEditText.setError("First name is required");
                return;
            }

            if (TextUtils.isEmpty(lastName)) {
                lastNameEditText.setError("Last name is required");
                return;
            }

            String fullName = firstName + " " + lastName;

            // Passing email and full name to next fragment
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("name", fullName);

            RegistrationBirthdateFragment birthdateFragment = new RegistrationBirthdateFragment();
            birthdateFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, birthdateFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}