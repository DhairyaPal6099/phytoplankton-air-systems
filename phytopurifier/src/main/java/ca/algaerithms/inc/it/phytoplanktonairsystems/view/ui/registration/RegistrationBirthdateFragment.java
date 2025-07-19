package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class RegistrationBirthdateFragment extends Fragment {

    private DatePicker birthdatePicker;
    private Button continueButton, skipButton;
    private String email, name;
    private String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_birthdate, container, false);

        // UI
        birthdatePicker = view.findViewById(R.id.registrationBirthdate_datePicker);
        continueButton = view.findViewById(R.id.registrationBirthdate_continueButton);
        skipButton = view.findViewById(R.id.registrationBirthdate_skipButton);

        // Get model from previous fragment
        if (getArguments() != null) {
            email = getArguments().getString("email");
            name = getArguments().getString("name");
        }

        // Continue button logic
        continueButton.setOnClickListener(v -> {
            int day = birthdatePicker.getDayOfMonth();
            int month = birthdatePicker.getMonth();
            int year = birthdatePicker.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.getTime());

            // Pass email, name, birthdate to next fragment
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("name", name);
            bundle.putString("birthdate", formattedDate);

            RegistrationPhoneFragment phoneFragment = new RegistrationPhoneFragment();
            phoneFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, phoneFragment)
                    .addToBackStack(null)
                    .commit();
        });

        //Skip button logic
        skipButton.setOnClickListener(v -> {
            // Pass "N/A" as birthdate
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            bundle.putString("name", name);
            bundle.putString("birthdate", "N/A");

            RegistrationPhoneFragment phoneFragment = new RegistrationPhoneFragment();
            phoneFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, phoneFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}