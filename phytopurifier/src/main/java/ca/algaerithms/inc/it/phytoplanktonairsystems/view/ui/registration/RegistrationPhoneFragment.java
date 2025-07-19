package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class RegistrationPhoneFragment extends Fragment {

    private CountryCodePicker ccp;
    private EditText phoneEditText;
    private Button continueButton;

    private String email, name, birthdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_phone, container, false);

        // Get references
        ccp = view.findViewById(R.id.registration_ccp);
        phoneEditText = view.findViewById(R.id.registrationPhone_editText);
        continueButton = view.findViewById(R.id.registrationPhone_button);

        // Receive previous model
        if (getArguments() != null) {
            email = getArguments().getString("email");
            name = getArguments().getString("name");
            birthdate = getArguments().getString("birthdate");
        }

        continueButton.setOnClickListener(v -> {
            String rawPhone = phoneEditText.getText().toString().trim();

            if (TextUtils.isEmpty(rawPhone)) {
                phoneEditText.setError(getString(R.string.phone_number_is_required));
                return;
            }

            String fullPhone = "+" + ccp.getSelectedCountryCode() + rawPhone;

            if (!isValidPhoneNumber(fullPhone)) {
                phoneEditText.setError(getString(R.string.invalid_phone_number));
                return;
            }

            // Proceed to next fragment (PasswordFragment)
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("name", name);
            bundle.putString("birthdate", birthdate);
            bundle.putString("phone", fullPhone);

            RegistrationPasswordFragment passwordFragment = new RegistrationPasswordFragment();
            passwordFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, passwordFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private boolean isValidPhoneNumber(String number) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneUtil.parse(number, null);
            return phoneUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}