/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;


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

        TextView notifTextView = view.findViewById(R.id.textView9);

        NotificationManagerPhytopurifier.getInstance(requireContext()).getAllNotifications(notifications -> {
            if (notifications != null && !notifications.isEmpty()) {
                // Sort by timestamp descending if needed (optional)
                notifications.sort((n1, n2) -> n2.getTimestamp().compareTo(n1.getTimestamp()));

                NotificationModel latestNotif = notifications.get(0); // latest is now at the top
                notifTextView.setText(latestNotif.getMessage()); // or getTitle()
            } else {
                notifTextView.setText("No recent notifications.");
            }
        });



        return view;
    }
}