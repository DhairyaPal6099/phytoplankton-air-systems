package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.MainModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.LoginActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class MainController {
    private final MainActivity view;
    private final MainModel model;

    public MainController(MainActivity view) {
        this.view = view;
        this.model = new MainModel(view);
    }

    public void populateUserHeader(NavigationView navigationView) {
        if (model.getCurrentUser() == null) return;

        String email = model.getCurrentUser().getEmail();
        String uid = model.getCurrentUser().getUid();

        View header = navigationView.getHeaderView(0);
        TextView nameView = header.findViewById(R.id.nav_header_username);
        TextView emailView = header.findViewById(R.id.nav_header_userEmail);

        emailView.setText(email);

        model.fetchUserName(uid, name -> nameView.setText(name != null ? name : "User"));
    }

    public void handleLogout(DrawerLayout drawerLayout) {
        new AlertDialog.Builder(view)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure_you_want_to_logout)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    model.logout(() -> {
                        Intent intent = new Intent(view, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        view.startActivity(intent);
                        view.finish();
                    });
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public void shareDashboard(FragmentManager fm) {
        Fragment navHost = fm.findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHost == null) return;

        List<Fragment> children = navHost.getChildFragmentManager().getFragments();
        if (children.isEmpty()) return;

        View dashboardView = children.get(0).getView();
        if (dashboardView == null) return;

        dashboardView.post(() -> {
            Uri uri = model.captureAndSaveDashboard(dashboardView);
            if (uri == null) return;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/png");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            view.startActivity(Intent.createChooser(shareIntent, "Share dashboard via"));
        });
    }

    public void showExitDialog() {
        new AlertDialog.Builder(view)
                .setIcon(R.drawable.exit)
                .setTitle(R.string.app_name)
                .setMessage(R.string.do_you_want_to_exit_the_app)
                .setPositiveButton(R.string.yes, (dialog, id) -> view.finishAffinity())
                .setNegativeButton(R.string.no, null)
                .setCancelable(false)
                .show();
    }
}
