/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseError;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.controller.DashboardController;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentDashboardBinding;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.supportActionBarFragments.NotificationsFragment;


public class DashboardFragment extends Fragment implements DashboardView{

    private TextView aqiTextView, notifHeading, notifText, lightTextView, proximityTextView, turbidityTextView, waterTextView;
    private ProgressBar progressBar;
    CardView notifCard;
    private DashboardController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        aqiTextView = view.findViewById(R.id.text_aqi_value);
        progressBar = view.findViewById(R.id.aqi_progress_bar);
        notifHeading = view.findViewById(R.id.notification_title);
        notifText = view.findViewById(R.id.notification_content);
        notifCard = view.findViewById(R.id.notification_card);
        lightTextView = view.findViewById(R.id.text_light_value);
        proximityTextView = view.findViewById(R.id.text_proximity_value);
        turbidityTextView = view.findViewById(R.id.text_turbidity_value);
        waterTextView = view.findViewById(R.id.text_waterLevel_value);

        // Set up notifications card
        NotificationManagerPhytopurifier.getInstance(getContext())
                .getAllNotifications(notifications -> {
                    if (!notifications.isEmpty()) {
                        NotificationModel latest = notifications.get(0);
                        notifHeading.setText(latest.getTitle());
                        notifText.setText(latest.getMessage());
                        notifCard.setOnClickListener(v -> {
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                            navController.navigate(R.id.action_notifications);
                        });
                    } else {
                        notifHeading.setText(R.string.no_notifications_yet);
                        notifText.setText(R.string.no_notifications_yet);
                        notifCard.setClickable(false);
                    }
                });

        // Live update AQI via controller
        controller = new DashboardController(this, (LifecycleOwner) getViewLifecycleOwner());
        controller.startListeningToSensorData();
        return view;
    }

    // Callback wrapper to integrate with controller’s SensorDataCallback
    private final SensorDataHandler handler = new SensorDataHandler();

    private class SensorDataHandler implements ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager.SensorDataCallback {
        @Override
        public void onDataFetched(ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData data) {
            if (data != null) {
                // AQI
                int aqi = data.calculateAqi();
                showAqi(aqi);

                // Light
                showLight(data.getLight());

                // Turbidity
                showTurbidity(data.getTurbidity());

                // Proximity
                showProximity(data.isProximity());

                // Add Water Level here

            } else {
                showError(getString(R.string.no_sensor_data_available));
            }
        }

        @Override
        public void onError(com.google.firebase.database.DatabaseError error) {
            showError(error.getMessage());
        }
    }

    @Override
    public void showAqi(int aqi) {
        Log.d("DashboardFragment", "Updating AQI UI: " + aqi);
        aqiTextView.setText(String.valueOf(aqi));
        progressBar.setProgress(aqi);
    }

    @Override
    public void showLight(double light) {
        lightTextView.setText(String.format("%.1f lx", light));
    }

    @Override
    public void showTurbidity(double turbidity) {
        turbidityTextView.setText(String.format("%.2f NTU", turbidity));
    }

    @Override
    public void showProximity(boolean proximity) {
        proximityTextView.setText(proximity ? "Motion Detected" : "No Motion");
    }

    @Override
    public void showError(String message) {
        Log.e("DashboardFragment", "Error: " + message);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!NetworkUtils.isConnected(requireContext())) {
            ((MainActivity) requireActivity()).showSensorDataSnackbar();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) requireActivity()).dismissSensorDataSnackbar();
        if (controller != null) controller.stopListeningToSensorData();
    }
}