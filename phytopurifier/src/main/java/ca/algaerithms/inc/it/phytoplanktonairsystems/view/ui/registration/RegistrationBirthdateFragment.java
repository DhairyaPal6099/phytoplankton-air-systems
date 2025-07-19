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
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationController;


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

            // Navigate using controller
            RegistrationController.goToPhone(requireActivity(), email, name, formattedDate);
        });

        skipButton.setOnClickListener(v -> {
            RegistrationController.goToPhone(requireActivity(), email, name, "N/A");
        });

        return view;
    }
}