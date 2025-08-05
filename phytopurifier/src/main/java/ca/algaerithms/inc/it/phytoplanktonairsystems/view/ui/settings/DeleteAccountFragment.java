/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.DeleteAccountController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.LoginActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class DeleteAccountFragment extends Fragment implements DeleteAccountController.DeleteAccountView {

    private DeleteAccountController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_account, container, false);
        controller = new DeleteAccountController(this);

        Button deleteButton = view.findViewById(R.id.btn_delete_account);
        deleteButton.setOnClickListener(v -> {
            // Network check
            if (!NetworkUtils.isConnected(requireContext())) {
                ((MainActivity) requireActivity()).showOfflineSnackbar();
                return; // stop here if no internet
            }

            showConfirmDialog();
        });

        return view;
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_deletion)
                .setMessage(R.string.are_you_sure_you_want_to_permanently_delete_your_account)
                .setPositiveButton(getString(R.string.delete), (dialog, which) ->
                        controller.onDeleteRequested(requireContext()))
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void showPasswordDialog(FirebaseUser user) {
        EditText passwordInput = new EditText(requireContext());
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setHint(R.string.enter_your_password);
        passwordInput.setPadding(40, 40, 40, 40);
        passwordInput.setBackgroundResource(R.drawable.semirounded_corners);

        ImageView toggleIcon = new ImageView(requireContext());
        toggleIcon.setImageResource(R.drawable.visibility_off);
        toggleIcon.setPadding(40, 40, 40, 40);
        toggleIcon.setOnClickListener(v -> {
            if ((passwordInput.getInputType() & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleIcon.setImageResource(R.drawable.visibility_off);
            } else {
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleIcon.setImageResource(R.drawable.visibility_on);
            }
            passwordInput.setSelection(passwordInput.length());
        });

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(40, 10, 40, 10);
        layout.addView(passwordInput, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        layout.addView(toggleIcon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.re_authenticate)
                .setMessage(R.string.please_enter_your_password_to_delete_your_account)
                .setView(layout)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    String password = passwordInput.getText().toString().trim();
                    if (password.isEmpty()) {
                        showMessage(getString(R.string.password_required));
                    } else {
                        controller.reauthenticateAndDelete(user, password);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}