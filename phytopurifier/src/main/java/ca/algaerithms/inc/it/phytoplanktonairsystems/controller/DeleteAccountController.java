package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.AccountDeletionManager;

public class DeleteAccountController {
    private Context context;
    private final AccountDeletionManager model;

    public interface DeleteAccountView {
        void showPasswordDialog(FirebaseUser user);
        void showLoginScreen();
        void showMessage(String msg);
    }

    private final DeleteAccountView view;

    public DeleteAccountController(DeleteAccountView view) {
        this.view = view;
        this.model = new AccountDeletionManager();
    }

    public void onDeleteRequested(Context context) {
        FirebaseUser user = model.getCurrentUser();
        if (user == null) {
            view.showMessage("No user is logged in.");
            return;
        }

        if (model.isGoogleUser(user)) {
            model.deleteUserAccount(user, getDeleteCallback());
        } else {
            view.showPasswordDialog(user);
        }
    }

    public void reauthenticateAndDelete(FirebaseUser user, String password) {
        model.reauthenticateUser(user, password, new AccountDeletionManager.ReauthCallback() {
            @Override
            public void onSuccess() {
                model.deleteUserAccount(user, getDeleteCallback());
            }

            @Override
            public void onFailure(String message) {
                view.showMessage(message);
            }
        });
    }

    private AccountDeletionManager.DeleteCallback getDeleteCallback() {
        return new AccountDeletionManager.DeleteCallback() {
            @Override
            public void onSuccess() {
                view.showMessage("Account deleted.");
                view.showLoginScreen();
            }

            @Override
            public void onFailure(String message) {
                view.showMessage(message);
            }
        };
    }
}