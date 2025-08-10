/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.UserRegistrationManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationNameFragment;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationPasswordFragment;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationPhoneFragment;

public class RegistrationController {

    private final UserRegistrationManager userManager = new UserRegistrationManager();

    public void deleteTempUser(Runnable onDeleted) {
        userManager.deleteTempUserIfExists(onDeleted);
    }

    public interface VerificationStatusCallback {
        void onChecked(boolean isVerified);
    }

    // Register user & send verification email by delegating to model
    public void registerEmail(String email, String tempPassword, RegistrationCallback callback) {
        userManager.createUserWithEmail(email, tempPassword, new UserRegistrationManager.UserCreationCallback() {
            @Override
            public void onSuccess() {
                callback.onVerificationEmailSent();
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }

    // Check email verification status via model
    public void checkEmailVerification(VerificationStatusCallback callback) {
        userManager.checkEmailVerification(isVerified -> callback.onChecked(isVerified));
    }

    public void updatePasswordAndSaveUser(String email,
                                          String name,
                                          String phone,
                                          int lifetimeCO2,
                                          String password,
                                          UserRegistrationManager.PasswordUpdateCallback callback) {
        userManager.updatePasswordAndSaveUserData(email, name, phone, lifetimeCO2, password,
                new UserRegistrationManager.PasswordUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(String message) {
                        callback.onFailure(message);
                    }
                });
    }

    // ====== NAVIGATION HELPERS ======

    public static void goToName(FragmentActivity fragment, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        navigate(fragment, new RegistrationNameFragment(), bundle);
    }

    public static void goToPhone(FragmentActivity fragment, String email, String name) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        navigate(fragment, new RegistrationPhoneFragment(), bundle);
    }

    public static void goToPassword(FragmentActivity fragment, String email, String name, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        bundle.putString("phone", phone);
        navigate(fragment, new RegistrationPasswordFragment(), bundle);
    }

    private static void navigate(FragmentActivity activity, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(ca.algaerithms.inc.it.phytoplanktonairsystems.R.id.registration_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}