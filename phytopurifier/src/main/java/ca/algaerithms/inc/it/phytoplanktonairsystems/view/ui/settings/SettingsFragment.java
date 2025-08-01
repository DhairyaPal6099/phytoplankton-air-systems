/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        FirebaseApp.initializeApp(requireContext());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = auth.getCurrentUser().getUid();
            TextView usernameTextView = view.findViewById(R.id.usernameTextView);
            TextView emailTextView = view.findViewById(R.id.emailTextView);

            // Fetch user document from Firestore
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("name"); // or "username"
                            String email = documentSnapshot.getString("email");

                            if (username != null && !username.isEmpty()) {
                                usernameTextView.setText(username);
                            }

                            if (email != null && !email.isEmpty()) {
                                emailTextView.setText(email);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), R.string.failed_to_load_user_info, Toast.LENGTH_SHORT).show();
                    });
        }

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
            try {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.privacyPolicyFragment);
            } catch (IllegalArgumentException e) {
                //Ignored
            }
        });

        TextView termsText = view.findViewById(R.id.termsOfServiceText);
        termsText.setOnClickListener(v -> {
            try {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.termsOfServiceFragment);
            } catch (IllegalArgumentException e) {
                //Ignored
            }
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
