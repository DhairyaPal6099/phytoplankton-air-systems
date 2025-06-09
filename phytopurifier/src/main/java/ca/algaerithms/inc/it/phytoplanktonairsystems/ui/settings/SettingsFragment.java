package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView title = view.findViewById(R.id.setting_title);
                Switch switchToggle = view.findViewById(R.id.setting_switch);

                if (position == 0) { // Lock screen to portrait
                    switchToggle.setVisibility(View.VISIBLE);

                    SharedPreferences prefs = requireContext().getSharedPreferences(getString(R.string.settings_lowercase), Context.MODE_PRIVATE);
                    boolean locked = prefs.getBoolean(getString(R.string.lockportrait), false);
                    switchToggle.setChecked(locked);

                    switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        prefs.edit().putBoolean(getString(R.string.lockportrait), isChecked).apply();
                        if (isChecked) {
                            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        } else {
                            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        }
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
            if (position == 1) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_settings_to_accountInfoFragment);
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
