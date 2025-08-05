/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class InsightsFragment extends Fragment implements ArticleAdapter.OnArticleClickListener {

    private RecyclerView articlesRecyclerView;
    private RecyclerView basicInsights, intermediateInsights, advancedInsights, articlesInsights;
    private ImageView closeButton;
    private FrameLayout floatingContainer;
    private WebView floatingWebView;
    private ViewGroup rootView;
    private View dragHandle;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insights, container, false);

        basicInsights = view.findViewById(R.id.basicInsightsRecycler);
        intermediateInsights = view.findViewById(R.id.intermediateInsightsRecycler);
        advancedInsights = view.findViewById(R.id.advancedInsightsRecycler);
        articlesInsights = view.findViewById(R.id.articlesRecycler);
        articlesInsights.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


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
        rootView = (ViewGroup) requireActivity().findViewById(android.R.id.content);
        rootView.addView(floatingVideoView);

        // Access elements inside the floating layout
        //scrollView = view.findViewById(R.id.scrollLayout);
        floatingContainer = floatingVideoView.findViewById(R.id.floatingVideoContainer);
        floatingWebView = floatingVideoView.findViewById(R.id.floatingWebView);
        closeButton = floatingVideoView.findViewById(R.id.closeFloatingWebView);
        dragHandle = floatingVideoView.findViewById(R.id.dragHandle);

        articleView();

        // Configure WebView
        floatingWebView.getSettings().setJavaScriptEnabled(true);
        floatingWebView.setWebChromeClient(new WebChromeClient());

        // Close button logic
        closeButton.setOnClickListener(v -> floatingContainer.setVisibility(View.GONE));// stop video

        dragFucntion();
    }

    public void articleView() {
        List<ArticleItem> articles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String title = getString(getResources().getIdentifier("article_title_" + i, "string", getContext().getPackageName()));
            String snippet = getString(getResources().getIdentifier("article_snippet_" + i, "string", getContext().getPackageName()));
            String meta = getString(getResources().getIdentifier("article_meta_" + i, "string", getContext().getPackageName()));
            int imageId = R.drawable.algaerithms_padded;
            String url = getString(getResources().getIdentifier("article_url_" + i, "string", getContext().getPackageName()));
            articles.add(new ArticleItem(title, snippet, meta, imageId, url));
        }

        ArticleAdapter adapter = new ArticleAdapter(getContext(), articles, this);
        articlesInsights.setAdapter(adapter);
    }

    @Override
    public void onArticleClick(ArticleItem article) {
        floatingWebView.loadUrl(article.getUrl());
        floatingContainer.setVisibility(View.VISIBLE);
        //scrollView.scrollTo(0, 0); // optional: scroll to top when WebView appears
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dragFucntion() {
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            float dX = 0f, dY = 0f;
            boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Record the difference between touch point and container corner
                        dX = floatingContainer.getX() - event.getRawX();
                        dY = floatingContainer.getY() - event.getRawY();
                        isDragging = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Set dragging flag to true and update position
                        isDragging = true;
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        // Constrain inside parent bounds
                        ViewGroup parent = (ViewGroup) floatingContainer.getParent();
                        newX = Math.max(0, Math.min(newX, parent.getWidth() - floatingContainer.getWidth()));
                        newY = Math.max(0, Math.min(newY, parent.getHeight() - floatingContainer.getHeight()));

                        floatingContainer.setX(newX);
                        floatingContainer.setY(newY);
                        return true;

                    case MotionEvent.ACTION_UP:
                        // Let other views handle it if it wasn’t a drag
                        return isDragging;
                }
                return false;
            }
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