package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.content.Context;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.UserModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.UserSettingsManager;

public class SettingsController {

    public interface ViewCallback {
        void updateUserInfo(String name, String email);
        void showToast(String message);
        void navigateTo(int destinationId);
        void applyDarkMode(boolean enabled);
        void applyScreenOrientation(boolean locked);
    }

    private final ViewCallback view;
    private final UserSettingsManager settingsModel;

    public SettingsController(Context context, ViewCallback view) {
        this.view = view;
        this.settingsModel = new UserSettingsManager(context);
    }

    public void loadUserInfo() {
        settingsModel.fetchUserData(new UserSettingsManager.OnUserDataFetched() {
            @Override
            public void onUserData(UserModel user) {
                view.updateUserInfo(user.getName(), user.getEmail());
            }

            @Override
            public void onError(String error) {
                view.showToast("Failed to load user info.");
            }
        });
    }

    public boolean getDarkMode() {
        return settingsModel.isDarkMode();
    }

    public void toggleDarkMode(boolean enabled) {
        settingsModel.setDarkMode(enabled);
        view.applyDarkMode(enabled);
    }

    public boolean getLockPortrait() {
        return settingsModel.isLockPortrait();
    }

    public void toggleLockPortrait(boolean locked) {
        settingsModel.setLockPortrait(locked);
        view.applyScreenOrientation(locked);
    }

    public boolean getReduceMotion() {
        return settingsModel.isReduceMotion();
    }

    public void toggleReduceMotion(boolean reduced) {
        settingsModel.setReduceMotion(reduced);
        view.showToast(reduced ? "Animations Reduced" : "Animations Restored");
    }

    public void handleNavigation(int destinationId) {
        view.navigateTo(destinationId);
    }
}
