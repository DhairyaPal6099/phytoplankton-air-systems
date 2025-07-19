package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationBirthdateFragment;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationNameFragment;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationPasswordFragment;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.registration.RegistrationPhoneFragment;

public class RegistrationController {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void registerEmail(String email, String tempPassword, RegistrationCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, tempPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnSuccessListener(unused -> callback.onVerificationEmailSent())
                                    .addOnFailureListener(e -> callback.onFailure("Failed to send verification email."));
                        }
                    } else {
                        String msg = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                        callback.onFailure(msg);
                    }
                });
    }

    public void checkEmailVerification(VerificationStatusCallback callback) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnSuccessListener(unused -> callback.onChecked(user.isEmailVerified()))
                    .addOnFailureListener(e -> callback.onChecked(false));
        } else {
            callback.onChecked(false);
        }
    }

    public interface VerificationStatusCallback {
        void onChecked(boolean isVerified);
    }

    // ====== NAVIGATION HELPERS ======

    public static void goToName(FragmentActivity fragment, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        navigate(fragment, new RegistrationNameFragment(), bundle);
    }

    public static void goToBirthdate(FragmentActivity fragment, String email, String fullName) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", fullName);
        navigate(fragment, new RegistrationBirthdateFragment(), bundle);
    }

    public static void goToPhone(FragmentActivity fragment, String email, String name, String birthdate) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        bundle.putString("birthdate", birthdate);
        navigate(fragment, new RegistrationPhoneFragment(), bundle);
    }

    public static void goToPassword(FragmentActivity fragment, String email, String name, String birthdate, String phone) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        bundle.putString("birthdate", birthdate);
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