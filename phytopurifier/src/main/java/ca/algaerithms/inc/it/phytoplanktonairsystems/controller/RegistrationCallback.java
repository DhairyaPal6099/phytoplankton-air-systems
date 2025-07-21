package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

public interface RegistrationCallback {
    void onVerificationEmailSent();
    void onFailure(String message);
}