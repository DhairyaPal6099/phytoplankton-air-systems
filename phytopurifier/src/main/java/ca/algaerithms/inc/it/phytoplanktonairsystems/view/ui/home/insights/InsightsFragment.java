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
    private Spinner videoSpinner;
    private WebView videoWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);

        videoSpinner = view.findViewById(R.id.video_spinner);
        videoWebView = view.findViewById(R.id.video_webview);

        String[] videoTitles = getResources().getStringArray(R.array.video_titles);
        String[] videoUrls = getResources().getStringArray(R.array.video_urls);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, videoTitles);
        videoSpinner.setAdapter(adapter);

        videoWebView.getSettings().setJavaScriptEnabled(true);

        videoSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String videoUrl = videoUrls[position];
                String html = "<html><body style='margin:0'><iframe width=\"100%\" height=\"100%\" src=\"" +
                        videoUrl + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                videoWebView.loadData(html, "text/html", "utf-8");
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });


        return view;
    }
}