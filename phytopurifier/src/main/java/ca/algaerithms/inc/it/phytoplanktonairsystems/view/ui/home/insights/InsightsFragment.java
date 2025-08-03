/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentInsightsBinding;


public class InsightsFragment extends Fragment {
    private RecyclerView basicInsights, intermediateInsights, advancedInsights;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);

        basicInsights = view.findViewById(R.id.basicInsightsRecycler);
        intermediateInsights = view.findViewById(R.id.intermediateInsightsRecycler);
        advancedInsights = view.findViewById(R.id.advancedInsightsRecycler);

        setupVideoRecyclerViews();

        return view;
    }

    private List<VideoItem> loadVideosFromResources(int titlesResId, int idsResId) {
        String[] titles = getResources().getStringArray(titlesResId);
        String[] ids = getResources().getStringArray(idsResId);

        List<VideoItem> list = new ArrayList<>();
        for (int i = 0; i < Math.min(titles.length, ids.length); i++) {
            list.add(new VideoItem(titles[i], ids[i]));
        }
        return list;
    }

    private void setupVideoRecyclerViews() {
        Context ctx = getContext();

        // Basic Insights
        basicInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> phytoList = loadVideosFromResources(R.array.basic_titles, R.array.basic_ids);
        basicInsights.setAdapter(new VideoAdapter(ctx, phytoList));

        // Intermediate Insights
        intermediateInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> bruhList = loadVideosFromResources(R.array.intermediate_titles, R.array.intermediate_ids);
        intermediateInsights.setAdapter(new VideoAdapter(ctx, bruhList));

        // Advanced Insights
        advancedInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> omgList = loadVideosFromResources(R.array.advanced_titles, R.array.advanced_ids);
        advancedInsights.setAdapter(new VideoAdapter(ctx, omgList));
    }
}