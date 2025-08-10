/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.UserRegistrationManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.ValidationUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class RegistrationPasswordFragment extends Fragment {

    private final RegistrationController registrationController = new RegistrationController();

    private EditText passwordEditText, confirmPasswordEditText;
    private Button submitButton;

    private String email, name, phone;
    private int lifetime_co2_converted = 0;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_password, container, false);

        passwordEditText = view.findViewById(R.id.registrationPassword_editText);
        confirmPasswordEditText = view.findViewById(R.id.registrationConfirmPassword_editText);
        submitButton = view.findViewById(R.id.registrationPassword_button);

        if (getArguments() != null) {
            email = getArguments().getString("email");
            name = getArguments().getString("name");
            phone = getArguments().getString("phone");
        }

        setupPasswordToggle(passwordEditText, true);
        setupPasswordToggle(confirmPasswordEditText, false);

        handleSubmitButton();

        return view;
    }

    private void handleSubmitButton() {
        submitButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (!ValidationUtils.isValidPassword(password)) {
                passwordEditText.setError(getString(R.string.password_must_include_upper_lower_case_and_number_or_special_character));
                return;
            }

            if (!ValidationUtils.isValidConfirmPassword(password, confirmPassword)) {
                confirmPasswordEditText.setError(getString(R.string.passwords_do_not_match));
                return;
            }

            saveAuthenticatedUserData(password);
        });
    }

private void saveAuthenticatedUserData(String password) {
    // Delegate to controller for password update and user data save
    registrationController.updatePasswordAndSaveUser(email, name, phone, lifetime_co2_converted, password,
            new UserRegistrationManager.PasswordUpdateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), getString(R.string.registration_successful), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
}

@SuppressLint("ClickableViewAccessibility")
private void setupPasswordToggle(EditText editText, boolean isMainField) {
    editText.setOnTouchListener((v, event) -> {
        if (event.getAction() == MotionEvent.ACTION_UP &&
                event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {

            boolean isVisible = isMainField ? isPasswordVisible : isConfirmPasswordVisible;

            editText.setInputType(isVisible
                    ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setSelection(editText.getText().length());

            int iconRes = isVisible ? R.drawable.visibility_on : R.drawable.visibility_off;
            Drawable icon = ContextCompat.getDrawable(requireContext(), iconRes);
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);

            if (isMainField) {
                isPasswordVisible = !isPasswordVisible;
            } else {
                isConfirmPasswordVisible = !isConfirmPasswordVisible;
            }

            return true;
        }
        return false;
    });
}
}