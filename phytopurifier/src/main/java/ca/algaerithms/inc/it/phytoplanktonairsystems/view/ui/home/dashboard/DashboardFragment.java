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

    private TextView aqiTextView;
    private ProgressBar progressBar;
    private DashboardController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        aqiTextView = view.findViewById(R.id.text_aqi_value);
        progressBar = view.findViewById(R.id.aqi_progress_bar);

        // Set up notifications card
        CardView notifCard = view.findViewById(R.id.notification_card);
        TextView notifHeading = view.findViewById(R.id.textView8);
        TextView notifText = view.findViewById(R.id.textView9);
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
                int aqi = data.calculateAqi();
                showAqi(aqi);
            } else {
                showError("No sensor data available");
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