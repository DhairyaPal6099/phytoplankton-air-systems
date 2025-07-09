package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.content.Context;
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

        String[] settingTitles = getResources().getStringArray(R.array.settings_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                requireContext(),
                R.layout.list_item_setting,
                R.id.setting_title,
                settingTitles) {

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView title = view.findViewById(R.id.setting_title);
                Switch switchToggle = view.findViewById(R.id.setting_switch);
                //TextView sectionHeader = view.findViewById(R.id.setting_section_header);
                ImageView icon = view.findViewById(R.id.setting_icon);

                title.setText(getItem(position));

                // Set icons
                switch (position) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_potrairt);
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_darkmode);
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.profile);
                        break;
                    case 3:
                        icon.setImageResource(R.drawable.ic_color_blindness);
                        break;
                    case 4:
                        icon.setImageResource(R.drawable.ic_terms);
                        break;
                    case 5:
                        icon.setImageResource(R.drawable.ic_privacypolicy);
                        break;
                    case 6:
                        icon.setImageResource(R.drawable.ic_delete);
                        break;
                    default:
                        icon.setImageResource(R.drawable.ic_settings);
                }

                // SECTION HEADER
               // if (position == 0) {
                  //  sectionHeader.setVisibility(View.VISIBLE);
                 //   sectionHeader.setText("App Preferences");
                //} else {
                   // sectionHeader.setVisibility(View.GONE);
                //}

                SharedPreferences prefs = requireContext().getSharedPreferences(getString(R.string.settings_lowercase), Context.MODE_PRIVATE);

                if (position == 0) { // Lock screen to portrait
                    switchToggle.setVisibility(View.VISIBLE);
                    boolean locked = prefs.getBoolean(getString(R.string.lockportrait), false);
                    switchToggle.setChecked(locked);
                    switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        prefs.edit().putBoolean(getString(R.string.lockportrait), isChecked).apply();
                        requireActivity().setRequestedOrientation(
                                isChecked ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        );
                    });

                } else if (position == 1) { // Dark Mode
                    switchToggle.setVisibility(View.VISIBLE);
                    boolean darkMode = prefs.getBoolean(getString(R.string.dark_mode), false);
                    switchToggle.setChecked(darkMode);
                    switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        prefs.edit().putBoolean(getString(R.string.dark_mode), isChecked).apply();
                        AppCompatDelegate.setDefaultNightMode(
                                isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                        );
                    });

                } else {
                    switchToggle.setVisibility(View.GONE);
                }

                return view;
            }
        };

        binding.settingsList.setAdapter(adapter);

        // Handle navigation
        binding.settingsList.setOnItemClickListener((parent, view1, position, id) -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

            switch (position) {
                case 2:
                    navController.navigate(R.id.action_nav_settings_to_accountInfoFragment);
                    break;
                case 4:
                    navController.navigate(R.id.termsOfServiceFragment);
                    break;
                case 5:
                    navController.navigate(R.id.privacyPolicyFragment);
                    break;
                case 6:
                    navController.navigate(R.id.deleteAccountFragment);
                    break;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
