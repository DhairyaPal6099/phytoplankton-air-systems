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

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.LoginActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class DeleteAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

        Button deleteButton = view.findViewById(R.id.btn_delete_account);
        deleteButton.setOnClickListener(v -> confirmAndDeleteAccount());

        return view;
    }

    private void confirmAndDeleteAccount() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_deletion)
                .setMessage(R.string.are_you_sure_you_want_to_permanently_delete_your_account)
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        if (user.getProviderData().get(1).getProviderId().equals("google.com")) {
                            deleteAccount(user); // Google user – skip re-auth
                        } else {
                            showReauthDialog(user); // Email/password user
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.no_user_is_logged_in, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void showReauthDialog(FirebaseUser user) {
        EditText passwordInput = new EditText(requireContext());
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setHint(R.string.enter_your_password);
        passwordInput.setPadding(40, 40, 40, 40);
        passwordInput.setBackgroundResource(R.drawable.semirounded_corners); // Optional custom style

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
                        Toast.makeText(getContext(), R.string.password_required, Toast.LENGTH_SHORT).show();
                    } else {
                        reauthenticateAndDelete(user, password);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void reauthenticateAndDelete(FirebaseUser user, String password) {
        String email = user.getEmail();
        if (email == null) {
            Toast.makeText(getContext(), R.string.email_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        deleteAccount(user);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.reauthentication_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteAccount(FirebaseUser user) {
        String uid = user.getUid();

        FirebaseFirestore.getInstance().collection("users").document(uid).delete()
                .addOnCompleteListener(task1 -> FirebaseDatabase.getInstance().getReference("sensor_readings").child(uid).removeValue()
                        .addOnCompleteListener(task2 -> user.delete().addOnCompleteListener(task3 -> {
                            if (task3.isSuccessful()) {
                                Toast.makeText(getContext(), getString(R.string.account_deleted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), getString(R.string.failed_to_delete_account), Toast.LENGTH_SHORT).show();
                            }
                        })));
    }
}