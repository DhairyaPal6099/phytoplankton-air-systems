/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class InsightsFragment extends Fragment {
    private RecyclerView basicInsights, intermediateInsights, advancedInsights;
    private ImageView closeButton;
    private FrameLayout floatingContainer;
    private WebView floatingWebView;


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inflate floating video layout and add to root
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View floatingVideoView = inflater.inflate(R.layout.floating_video_layout, null);

        // Add it to root of fragment
        ViewGroup rootView = (ViewGroup) requireActivity().findViewById(android.R.id.content);
        rootView.addView(floatingVideoView);

        // Access elements inside the floating layout
        floatingContainer = floatingVideoView.findViewById(R.id.floatingVideoContainer);
        floatingWebView = floatingVideoView.findViewById(R.id.floatingWebView);
        closeButton = floatingVideoView.findViewById(R.id.closeFloatingWebView);

        // Configure WebView
        floatingWebView.getSettings().setJavaScriptEnabled(true);

        // Close button logic
        closeButton.setOnClickListener(v -> {
            floatingContainer.setVisibility(View.GONE);// stop video
        });
    }

    public void showFloatingVideo(String videoId) {
        if (floatingContainer.getVisibility() != View.VISIBLE) {
            floatingContainer.setVisibility(View.VISIBLE);
        }
        floatingWebView.loadUrl("https://www.youtube.com/embed/" + videoId + "?autoplay=1");
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

        // Use method reference for click listener (calls showFloatingVideo)
        VideoAdapter.OnVideoClickListener videoClickListener = this::showFloatingVideo;

        // Basic Insights
        basicInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> basicList = loadVideosFromResources(R.array.basic_titles, R.array.basic_ids);
        basicInsights.setAdapter(new VideoAdapter(ctx, basicList, videoClickListener));

        // Intermediate Insights
        intermediateInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> intermediateList = loadVideosFromResources(R.array.intermediate_titles, R.array.intermediate_ids);
        intermediateInsights.setAdapter(new VideoAdapter(ctx, intermediateList, videoClickListener));

        // Advanced Insights
        advancedInsights.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        List<VideoItem> advancedList = loadVideosFromResources(R.array.advanced_titles, R.array.advanced_ids);
        advancedInsights.setAdapter(new VideoAdapter(ctx, advancedList, videoClickListener));
    }
}