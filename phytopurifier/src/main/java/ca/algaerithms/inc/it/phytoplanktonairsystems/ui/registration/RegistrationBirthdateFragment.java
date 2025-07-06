package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.registration;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class RegistrationBirthdateFragment extends Fragment {

    private DatePicker birthdatePicker;
    private Button continueButton;
    private String email, name;
    private String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_birthdate, container, false);

        birthdatePicker = view.findViewById(R.id.registrationBirthdate_datePicker);
        continueButton = view.findViewById(R.id.registrationBirthdate_continueButton);
        // Get data from previous fragment
        if (getArguments() != null) {
            email = getArguments().getString("email");
            name = getArguments().getString("name");
        }

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

        return view;
    }
}