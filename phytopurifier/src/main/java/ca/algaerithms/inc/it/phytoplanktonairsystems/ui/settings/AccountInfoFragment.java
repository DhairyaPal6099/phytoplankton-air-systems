package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;

public class AccountInfoFragment extends Fragment {

    private FragmentAccountInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireContext().getSharedPreferences("account_info", Context.MODE_PRIVATE);

        // Load saved values or leave empty
        binding.usernameInput.setText(prefs.getString("username", ""));
        binding.emailInput.setText(prefs.getString("email", ""));
        binding.phoneInput.setText(prefs.getString("phone", ""));
        binding.birthdayInput.setText(prefs.getString("birthday", ""));

        // Handle date selection
        binding.birthdayInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // 0-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        binding.birthdayInput.setText(formattedDate);
                    },
                    year, month, day
            );
            picker.show();
        });

        // Save button functionality
        binding.saveButton.setOnClickListener(v -> {
            prefs.edit()
                    .putString("username", binding.usernameInput.getText().toString())
                    .putString("email", binding.emailInput.getText().toString())
                    .putString("phone", binding.phoneInput.getText().toString())
                    .putString("birthday", binding.birthdayInput.getText().toString())
                    .apply();

            Toast.makeText(getContext(), "Details saved locally", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
