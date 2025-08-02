/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.MainController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.ShareDashboard;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.CO2Updater;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.DailyNotificationWorker;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private ActivityMainBinding binding;
    private MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        controller = new MainController(this);

        // Orientation lock based on user preference
        if (getSharedPreferences(getString(R.string.settings_lowercase), MODE_PRIVATE)
                .getBoolean(getString(R.string.lockportrait), false)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_about, R.id.nav_accountInfo, R.id.nav_feedback, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                controller.handleLogout(binding.drawerLayout);
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return handled;
            }
        });

        controller.populateUserHeader(binding.navView);
        requestNotificationPermissionIfNeeded();
        scheduleDailyNotificationWorkerOnce();
        scheduleRecurringCO2Worker();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    controller.showExitDialog();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        View actionView = item.getActionView();
        if (actionView != null) {
            actionView.setOnClickListener(v -> ShareDashboard.prepareAndShareDashboard(this));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void scheduleDailyNotificationWorkerOnce() {
        if (!getSharedPreferences("AppPrefs", MODE_PRIVATE).getBoolean("triggered_worker", false)) {
            WorkManager.getInstance(this).enqueue(new OneTimeWorkRequest.Builder(DailyNotificationWorker.class).build());
            getSharedPreferences("AppPrefs", MODE_PRIVATE).edit().putBoolean("triggered_worker", true).apply();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long currentTime = System.currentTimeMillis();
        long targetTime = calendar.getTimeInMillis();

        long delay = targetTime > currentTime
                ? targetTime - currentTime
                : TimeUnit.DAYS.toMillis(1) - (currentTime - targetTime);

        Data inputData = new Data.Builder().putBoolean("weekly_only", true).build();

        PeriodicWorkRequest dailyRequest = new PeriodicWorkRequest.Builder(DailyNotificationWorker.class, 24, TimeUnit.HOURS)
                .setInputData(inputData)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "daily_algae_check",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                dailyRequest
        );
    }

    private void scheduleRecurringCO2Worker() {
        PeriodicWorkRequest co2SyncRequest = new PeriodicWorkRequest.Builder(CO2Updater.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "CO2SyncWork",
                ExistingPeriodicWorkPolicy.KEEP,
                co2SyncRequest
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            String msg = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ? getString(R.string.notification_permission_granted)
                    : getString(R.string.notification_permission_denied);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}