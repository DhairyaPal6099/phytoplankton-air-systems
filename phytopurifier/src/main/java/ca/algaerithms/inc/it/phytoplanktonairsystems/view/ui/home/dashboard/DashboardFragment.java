/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentDashboardBinding;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.supportActionBarFragments.NotificationsFragment;


public class DashboardFragment extends Fragment {

    private View dashboardView;
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardView = view.findViewById(R.id.dashboard_view);

        TextView aqiTextView = view.findViewById(R.id.textView8);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        /*SensorDataManager.getInstance().getSensorLatestData(new SensorDataManager.SensorDataCallback() {
            @Override
            public void onDataFetched(SensorData data) {
                if (data != null) {
                    int aqi = data.getAqi(); // or similar value
                    aqiTextView.setText(String.valueOf(aqi));
                    progressBar.setProgress(aqi);
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Dashboard", "Error fetching AQI: " + error.getMessage());
            }
        });*/

        CardView notifCard = view.findViewById(R.id.notification_card);
        TextView notifText = view.findViewById(R.id.textView9);

        NotificationManagerPhytopurifier.getInstance(getContext()).getAllNotifications(notifications -> {
            if (!notifications.isEmpty()) {
                NotificationModel latestNotif = notifications.get(0);
                notifText.setText(latestNotif.getMessage());

                notifCard.setOnClickListener(v -> {
                    // Use NavController instead of FragmentTransaction
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.action_notifications); // <-- use the actual ID from nav_graph.xml
                });

            } else {
                notifText.setText(R.string.no_notifications_yet);
                notifCard.setClickable(false);
            }
        });








        return view;
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
    }
}