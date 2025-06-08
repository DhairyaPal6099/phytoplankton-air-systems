/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.home.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    private View dashboardView;
    private View graphView;
    private ToggleButton toggleButton;

    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardView = view.findViewById(R.id.dashboard_view);
        graphView = view.findViewById(R.id.graph_view);
        toggleButton = view.findViewById(R.id.toggle_view_button);

        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                dashboardView.setVisibility(View.GONE);
                graphView.setVisibility(View.VISIBLE);
            } else {
                dashboardView.setVisibility(View.VISIBLE);
                graphView.setVisibility(View.GONE);
            }
        });

        return view;
    }
}