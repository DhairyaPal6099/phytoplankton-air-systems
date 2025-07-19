/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentInsightsBinding;


public class InsightsFragment extends Fragment {

    private FragmentInsightsBinding binding;
    private Spinner videoSpinner;
    private WebView videoWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);

        videoSpinner = view.findViewById(R.id.video_spinner);

        String[] videoTitles = getResources().getStringArray(R.array.video_titles);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, videoTitles);
        videoSpinner.setAdapter(adapter);

        return view;
    }
}