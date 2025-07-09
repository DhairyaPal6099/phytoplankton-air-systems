package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences prefs = requireContext().getSharedPreferences(getString(R.string.settings_lowercase), Context.MODE_PRIVATE);

        // Lock screen to portrait
        Switch lockScreenSwitch = view.findViewById(R.id.lockScreenModeSwitch);
        boolean isLocked = prefs.getBoolean(getString(R.string.lockportrait), false);
        lockScreenSwitch.setChecked(isLocked);
        lockScreenSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(getString(R.string.lockportrait), isChecked).apply();
            requireActivity().setRequestedOrientation(
                    isChecked ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            );
        });

        // Dark mode
        Switch darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        boolean darkMode = prefs.getBoolean(getString(R.string.dark_mode), false);
        darkModeSwitch.setChecked(darkMode);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(getString(R.string.dark_mode), isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Reduce motion
        Switch reduceMotionSwitch = view.findViewById(R.id.reduceMotion);
        boolean reduceMotion = prefs.getBoolean(getString(R.string.reduce_motion_key), false);
        reduceMotionSwitch.setChecked(reduceMotion);
        reduceMotionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(getString(R.string.reduce_motion_key), isChecked).apply();
            Toast.makeText(getContext(), isChecked ? "Animations Reduced" : "Animations Restored", Toast.LENGTH_SHORT).show();
        });

        TextView privacyPolicy = view.findViewById(R.id.privacyPolicyText);
        privacyPolicy.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.privacyPolicyFragment);
        });

        TextView termsText = view.findViewById(R.id.termsOfServiceText);
        termsText.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.termsOfServiceFragment);
        });

        TextView deleteAccount = view.findViewById(R.id.logoutText);
        deleteAccount.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.deleteAccountFragment);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
