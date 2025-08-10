/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.accountInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.AccountInfoController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAccountInfoBinding;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class AccountInfoFragment extends Fragment {

    private FragmentAccountInfoBinding binding;
    private AccountInfoController controller;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    controllerPermissionGranted();
                } else {
                    Toast.makeText(requireContext(), R.string.gallery_permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    binding.profileImage.setImageURI(selectedImageUri);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = new AccountInfoController(binding, requireContext(), requireActivity(), new AccountInfoController.PermissionCallback() {
            @Override
            public void requestPermission(String permission) {
                permissionLauncher.launch(permission);
            }

            @Override
            public void launchGalleryIntent(Intent intent) {
                imagePickerLauncher.launch(intent);
            }
        });

        controller.loadUserInfo();

//        binding.birthdayInput.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            controller.handleBirthdayPicker(calendar, (view1, year, month, day) -> {
//                String formattedDate = String.format(getString(R.string.birthdate_format), year, month + 1, day);
//                binding.birthdayInput.setText(formattedDate);
//            });
//        });

        binding.updateButton.setOnClickListener(v -> {
            // Network check
            if (!NetworkUtils.isConnected(requireContext())) {
                ((MainActivity) requireActivity()).showOfflineSnackbar();
                return; // stop here if no internet
            }

            controller.saveUserInfo();
        });
        binding.profileImage.setOnClickListener(v -> controller.requestGalleryPermission());
    }

    private void controllerPermissionGranted() {
        // Called when permission is granted through launcher
        controller.requestGalleryPermission(); // Controller will detect that permission is granted and open gallery
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}