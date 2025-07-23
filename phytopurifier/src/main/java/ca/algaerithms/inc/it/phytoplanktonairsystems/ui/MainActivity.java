/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseApp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ca.algaerithms.inc.it.phytoplanktonairsystems.DailyNotificationWorker;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView nameTextView, emailTextView;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.settings_lowercase), MODE_PRIVATE);
        if (prefs.getBoolean(getString(R.string.lockportrait), false)) {
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
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                logout();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return handled;
            }
        });

        populateUserHeader();

        // 🔔 Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Schedule the daily notification worker
        scheduleDailyNotificationWorker();

        // Run DailyNotificationWorker manually once (only if not triggered yet)
        SharedPreferences triggerPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean alreadyTriggered = triggerPrefs.getBoolean("triggered_worker", false);

        if (!alreadyTriggered) {
            WorkManager.getInstance(this)
                    .enqueue(new OneTimeWorkRequest.Builder(DailyNotificationWorker.class).build());

            triggerPrefs.edit().putBoolean("triggered_worker", true).apply();
        }


        //  TEMP: Trigger manually
       // WorkManager.getInstance(this)
               // .enqueue(new OneTimeWorkRequest.Builder(DailyNotificationWorker.class).build());

        //  Display the AlertDialog on back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    showExit();
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
            actionView.setOnClickListener(view -> shareDashboard());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController);
    }

    private void populateUserHeader() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            NavigationView navigationView = binding.navView;
            View headerView = navigationView.getHeaderView(0);
            nameTextView = headerView.findViewById(R.id.nav_header_username);
            emailTextView = headerView.findViewById(R.id.nav_header_userEmail);
            emailTextView.setText(email);

            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String name = documentSnapshot.getString("name");
                        nameTextView.setText(name != null ? name : "User");
                    })
                    .addOnFailureListener(e -> nameTextView.setText("User"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void logout() {
        //Reset trigger flag on logout so next user run triggers fresh notification
        getSharedPreferences("AppPrefs", MODE_PRIVATE);
                prefs.edit().remove("triggered_worker").apply();

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure_you_want_to_logout)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.exit)
                .setTitle(R.string.app_name)
                .setMessage(R.string.do_you_want_to_exit_the_app)
                .setPositiveButton(R.string.yes, (dialog, id) -> finishAffinity())
                .setNegativeButton(R.string.no, null)
                .setCancelable(false)
                .show();
    }

    private void shareDashboard() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment == null) return;

        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        if (fragments.isEmpty()) return;

        Fragment visibleFragment = fragments.get(0);
        View dashboardView = visibleFragment.getView();
        if (dashboardView == null) return;

        dashboardView.post(() -> {
            Bitmap bitmap = Bitmap.createBitmap(dashboardView.getWidth(), dashboardView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            dashboardView.draw(canvas);

            try {
                File cachePath = new File(getCacheDir(), "images");
                cachePath.mkdirs();
                File file = new File(cachePath, "dashboard_screenshot.png");
                FileOutputStream stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

                Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                if (contentUri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.setType("image/png");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share dashboard via"));
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error sharing screenshot", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleDailyNotificationWorker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 29);
        calendar.set(Calendar.SECOND, 50);

        long currentTime = System.currentTimeMillis();
        long targetTime = calendar.getTimeInMillis();

        long delay = targetTime > currentTime
                ? targetTime - currentTime
                : TimeUnit.DAYS.toMillis(1) - (currentTime - targetTime);

        // 🔁 Pass weekly_only = true
        Data inputData = new Data.Builder()
                .putBoolean("weekly_only", true)
                .build();

        PeriodicWorkRequest dailyRequest =
                new PeriodicWorkRequest.Builder(DailyNotificationWorker.class, 24, TimeUnit.HOURS)
                        .setInputData(inputData)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "daily_algae_check",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                dailyRequest
        );
    }



    // 🔁 Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
