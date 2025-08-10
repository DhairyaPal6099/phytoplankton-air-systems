/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.settings;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.SettingsController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment implements SettingsController.ViewCallback{

    private FragmentSettingsBinding binding;
    private SettingsController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        controller = new SettingsController(requireContext(), this);

        binding.lockScreenModeSwitch.setChecked(controller.getLockPortrait());
        binding.darkModeSwitch.setChecked(controller.getDarkMode());
        applyDarkMode(controller.getDarkMode());
        binding.reduceMotion.setChecked(controller.getReduceMotion());

        binding.lockScreenModeSwitch.setOnCheckedChangeListener((b, checked) -> controller.toggleLockPortrait(checked));
        binding.darkModeSwitch.setOnCheckedChangeListener((b, checked) -> controller.toggleDarkMode(checked));
        binding.reduceMotion.setOnCheckedChangeListener((b, checked) -> controller.toggleReduceMotion(checked));

        binding.logoutText.setOnClickListener(v -> controller.handleNavigation(R.id.deleteAccountFragment));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller.loadUserInfo();
    }

    @Override
    public void updateUserInfo(String name, String email) {
        if (binding != null) {
            binding.usernameTextView.setText(name);
            binding.emailTextView.setText(email);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateTo(int destinationId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(destinationId);
    }

    @Override
    public void applyDarkMode(boolean enabled) {
        AppCompatDelegate.setDefaultNightMode(
                enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    @Override
    public void applyScreenOrientation(boolean locked) {
        requireActivity().setRequestedOrientation(
                locked ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
