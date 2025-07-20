package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.RegistrationController;

public class RegistrationActivity extends AppCompatActivity {

    private final RegistrationController registrationController = new RegistrationController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = findViewById(R.id.registration_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.registration_fragment_container);
        NavController navController = navHostFragment.getNavController();

        // Add back pressed callback once here
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                registrationController.deleteTempUser(() -> {
                    runOnUiThread(() -> {
                        finish();  // Close activity after deletion
                    });
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Handle the back arrow click
            onBackPressed(); // Calls default, which triggers OnBackPressedDispatcher
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}