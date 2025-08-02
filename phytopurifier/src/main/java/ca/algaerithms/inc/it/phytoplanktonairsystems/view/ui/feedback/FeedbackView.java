package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.feedback;

import android.content.Context;

public interface FeedbackView {
    void setNameField(String name);
    void setEmailField(String email);
    void clearFields();
    void showToast(String message);
    void showProgressBar(boolean show);
    void setSubmitButtonEnabled(boolean enabled);
    void updateCountdownText(String text);
    Context getContext();
    void showConfirmationDialog();
    void setNameError(String error);
    void setPhoneError(String error);
    void setEmailError(String error);
    void setCommentError(String error);
}