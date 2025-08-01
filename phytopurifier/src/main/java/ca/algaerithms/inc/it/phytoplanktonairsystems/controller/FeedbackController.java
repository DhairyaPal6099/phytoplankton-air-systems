package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Patterns;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.FeedbackModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.feedback.FeedbackView;

public class FeedbackController {

    private static final long COOLDOWN_MILLIS = 24 * 60 * 60 * 1000L;

    private final FeedbackView view;
    private final FeedbackModel model;
    private final Context context;

    private CountDownTimer countDownTimer;

    public FeedbackController(FeedbackView view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new FeedbackModel();
    }

    public boolean isUserSignedIn() {
        return model.isUserSignedIn();
    }

    public void prefillUserData() {
        if (!isUserSignedIn()) return;

        String email = model.getUserEmail();
        if (email != null) {
            view.setEmailField(email);
        }

        model.getUserName(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                String name = task.getResult().getString("name");
                if (name != null) {
                    view.setNameField(name);
                }
            }
        });
    }

    public void handleSubmitFeedback(String name, String phone, String email, String comment, float rating) {
        boolean hasError = false;

        if (name.isEmpty()) {
            view.setNameError(context.getString(R.string.name_is_required));
            hasError = true;
        }

        if (phone.isEmpty()) {
            view.setPhoneError(context.getString(R.string.phone_number_is_required));
            hasError = true;
        } else if (!phone.matches("\\d{10}")) {
            view.setPhoneError(context.getString(R.string.enter_a_valid_10_digit_phone_number));
            hasError = true;
        }

        if (email.isEmpty()) {
            view.setEmailError(context.getString(R.string.email_is_required));
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.setEmailError(context.getString(R.string.invalid_email_address));
            hasError = true;
        }

        if (comment.isEmpty()) {
            view.setCommentError(context.getString(R.string.comment_is_required));
            hasError = true;
        }

        if (rating == 0) {
            view.showToast(context.getString(R.string.please_select_a_rating));
            hasError = true;
        }

        if (hasError) return;

        view.showProgressBar(true);

        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("name", name);
        feedbackData.put("phone", phone);
        feedbackData.put("email", email);
        feedbackData.put("comment", comment);
        feedbackData.put("rating", rating);
        feedbackData.put("deviceModel", model.getDeviceModel());

        model.getLastFeedbackTime(task -> {
            if (!task.isSuccessful() || !task.getResult().exists()) {
                submitAndUpdate(feedbackData);
                return;
            }
            Long lastTime = task.getResult().getLong("feedback_disabled_time");
            long now = System.currentTimeMillis();
            if (lastTime != null && now - lastTime < COOLDOWN_MILLIS) {
                view.showProgressBar(false);
                view.showToast(context.getString(R.string.you_can_submit_feedback_only_once_every_24_hours));
                startCountdownTimer(COOLDOWN_MILLIS - (now - lastTime));
                return;
            }
            submitAndUpdate(feedbackData);
        });
    }

    private void submitAndUpdate(Map<String, Object> feedbackData) {
        // Delay 5 seconds to simulate progress bar showing
        new Handler().postDelayed(() -> {
            model.submitFeedback(feedbackData, task -> {
                view.showProgressBar(false);
                if (task.isSuccessful()) {
                    view.clearFields();
                    long now = System.currentTimeMillis();
                    startCountdownTimer(COOLDOWN_MILLIS);
                    view.showConfirmationDialog();
                } else {
                    view.showToast(context.getString(R.string.error_submitting_feedback) + task.getException().getMessage());
                }
            });
        }, 5000);
    }

    public void checkCooldownAndStartCountdown() {
        model.getLastFeedbackTime(task -> {
            if (!task.isSuccessful() || !task.getResult().exists()) {
                view.setSubmitButtonEnabled(true);
                view.updateCountdownText("");
                return;
            }
            Long lastTime = task.getResult().getLong("feedback_disabled_time");
            long now = System.currentTimeMillis();
            if (lastTime != null && now - lastTime < COOLDOWN_MILLIS) {
                view.setSubmitButtonEnabled(false);
                long remaining = COOLDOWN_MILLIS - (now - lastTime);
                startCountdownTimer(remaining);
            } else {
                view.setSubmitButtonEnabled(true);
                view.updateCountdownText("");
            }
        });
    }

    private void startCountdownTimer(long durationMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(durationMillis, 60 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / (1000 * 60 * 60);
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                String countdownText = String.format(Locale.getDefault(), "Available in %d hrs %d mins", hours, minutes);
                view.updateCountdownText(countdownText);
            }

            @Override
            public void onFinish() {
                view.setSubmitButtonEnabled(true);
                view.updateCountdownText("");
            }
        };
        countDownTimer.start();
    }
}