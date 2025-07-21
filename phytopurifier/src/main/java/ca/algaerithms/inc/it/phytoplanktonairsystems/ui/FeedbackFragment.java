package ca.algaerithms.inc.it.phytoplanktonairsystems.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class FeedbackFragment extends Fragment {

    private EditText etName, etPhone, etEmail, etComment;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private String deviceModel;
    private ProgressBar btnProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etComment = view.findViewById(R.id.etComment);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnProgress = view.findViewById(R.id.btnProgress);

        // Get device model
        deviceModel = android.os.Build.MODEL;

        prefillUserData();

        btnSubmit.setOnClickListener(v -> submitFeedback());

        return view;
    }

    private void prefillUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            etEmail.setText(user.getEmail());

            // Fetch name from Firestore if stored in /users/{uid}/name
            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            String name = snapshot.getString("name");
                            if (name != null) {
                                etName.setText(name);
                            }
                        }
                    });
        }
    }

    private void submitFeedback() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        float rating = ratingBar.getRating();

        boolean hasError = false;

        if (name.isEmpty()) {
            etName.setError(getString(R.string.name_is_required));
            hasError = true;
        }

        if (phone.isEmpty()) {
            etPhone.setError(getString(R.string.phone_number_is_required));
            hasError = true;
        } else if (!phone.matches("\\d{10}")) {
            etPhone.setError(getString(R.string.enter_a_valid_10_digit_phone_number));
            hasError = true;
        }

        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.email_is_required));
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.invalid_email_address));
            hasError = true;
        }

        if (comment.isEmpty()) {
            etComment.setError(getString(R.string.comment_is_required));
            hasError = true;
        }

        if (rating == 0) {
            Toast.makeText(getContext(), R.string.please_select_a_rating, Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (hasError) return;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), R.string.you_must_be_signed_in_to_submit_feedback, Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button and show spinner inside it
        btnSubmit.setEnabled(false);
        btnSubmit.setText("");
        btnProgress.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("name", name);
        feedback.put("phone", phone);
        feedback.put("email", email);
        feedback.put("comment", comment);
        feedback.put("rating", rating);
        feedback.put("deviceModel", deviceModel);
        feedback.put("userId", currentUser.getUid());

        FirebaseFirestore.getInstance().collection("feedback")
                .add(feedback)
                .addOnSuccessListener(doc -> {
                    clearFields();
                    showConfirmationDialog();
                    resetButton();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), getString(R.string.error) + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        }, 5000); // 5 second delay
    }


    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etComment.setText("");
        ratingBar.setRating(0);
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.feedback_submitted))
                .setMessage(R.string.thank_you_for_your_feedback)
                .setPositiveButton(getString(R.string.ok), null)
                .show();
    }

    private void resetButton() {
        btnProgress.setVisibility(View.GONE);
        btnSubmit.setText(getString(R.string.submit_feedback));
        btnSubmit.setEnabled(true);
    }
}