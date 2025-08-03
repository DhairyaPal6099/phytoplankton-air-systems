/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.feedback;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.FeedbackController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.FeedbackModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class FeedbackFragment extends Fragment implements FeedbackView  {

    private FeedbackController controller;

    private EditText etName, etPhone, etEmail, etComment;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private ProgressBar btnProgress;
    private TextView countdownText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etComment = view.findViewById(R.id.etComment);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnProgress = view.findViewById(R.id.btnProgress);
        countdownText = view.findViewById(R.id.countdownText);

        controller = new FeedbackController(this, requireContext());

        if (!controller.isUserSignedIn()) {
            showToast("Please sign in to submit feedback.");
            btnSubmit.setEnabled(false);
        } else {
            controller.prefillUserData();
            controller.checkCooldownAndStartCountdown();
        }

        btnSubmit.setOnClickListener(v -> {
            // Network check
            if (!NetworkUtils.isConnected(requireContext())) {
                ((MainActivity) requireActivity()).showOfflineSnackbar();
                return; // stop here if no internet
            }

            controller.handleSubmitFeedback(
                    etName.getText().toString().trim(),
                    etPhone.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etComment.getText().toString().trim(),
                    ratingBar.getRating()
            );
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar(boolean visible) {
        btnProgress.setVisibility(visible ? View.VISIBLE : View.GONE);
        btnSubmit.setEnabled(!visible);
    }

    @Override
    public void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etComment.setText("");
        ratingBar.setRating(0f);
    }

    @Override
    public void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.feedback_submitted))
                .setMessage(R.string.thank_you_for_your_feedback)
                .setPositiveButton(getString(R.string.ok), null)
                .show();
    }

    @Override
    public void setNameField(String name) {
        etName.setText(name);
    }

    @Override
    public void setEmailField(String email) {
        etEmail.setText(email);
    }

    @Override
    public void updateCountdownText(String text) {
        countdownText.setText(text);
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        btnSubmit.setEnabled(enabled);
    }

    @Override
    public void setNameError(String error) {
        etName.setError(error);
    }

    @Override
    public void setPhoneError(String error) {
        etPhone.setError(error);
    }

    @Override
    public void setEmailError(String error) {
        etEmail.setError(error);
    }

    @Override
    public void setCommentError(String error) {
        etComment.setError(error);
    }

}