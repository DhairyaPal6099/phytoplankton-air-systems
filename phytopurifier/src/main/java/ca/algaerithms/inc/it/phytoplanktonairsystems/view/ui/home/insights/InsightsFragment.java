/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
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
import java.util.Arrays;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


public class InsightsFragment extends Fragment implements ArticleAdapter.OnArticleClickListener {

    private RecyclerView basicInsights, intermediateInsights, advancedInsights, articlesInsights;
    private ImageView closeButton;
    private FrameLayout floatingContainer;
    private WebView floatingWebView;
    private ViewGroup rootView;
    private View dragHandle;
    private static final String PREFS_NAME = "fun_facts_prefs";
    private static final String KEY_LAST_PAGE = "last_page";

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

        setupFunFactsPager(view);
        articleView();

        // Configure WebView
        floatingWebView.getSettings().setJavaScriptEnabled(true);
        floatingWebView.setWebChromeClient(new WebChromeClient());

        // Close button logic
        closeButton.setOnClickListener(v -> floatingContainer.setVisibility(View.GONE));// stop video

        dragFucntion();
    }

    private void setupFunFactsPager(View view) {
        ViewPager2 funFactsPager = view.findViewById(R.id.funFactsPager);

        String[] funFactsArray = getResources().getStringArray(R.array.fun_facts_array);
        List<String> funFacts = Arrays.asList(funFactsArray);

        FunFactAdapter adapter = new FunFactAdapter(funFacts);
        funFactsPager.setAdapter(adapter);

        // Peeking effect: show partial previous & next pages
        funFactsPager.setOffscreenPageLimit(1);
        RecyclerView recyclerView = (RecyclerView) funFactsPager.getChildAt(0);
        recyclerView.setPadding(80, 0, 80, 0);
        recyclerView.setClipToPadding(false);
        recyclerView.setClipChildren(false);

        // Scale and fade page transformer for smooth effect
        funFactsPager.setPageTransformer((page, position) -> {
            float scale = 1 - Math.abs(position) * 0.15f; // scale down a bit
            page.setScaleY(scale);
            float alpha = 0.7f + (1 - Math.abs(position)) * 0.3f; // fade edges
            page.setAlpha(alpha);
        });

        // Retrieve last saved page index, default 0
        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int lastPage = prefs.getInt(KEY_LAST_PAGE, 0);
        funFactsPager.post(() -> funFactsPager.setCurrentItem(lastPage, false));
        // Save current page when user scrolls manually
        funFactsPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                prefs.edit().putInt(KEY_LAST_PAGE, position).apply();
            }
        });

        // Auto-scroll every 15 seconds
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable autoScroll = new Runnable() {
            @Override
            public void run() {
                int current = funFactsPager.getCurrentItem();
                int next = (current + 1) % funFacts.size();
                funFactsPager.setCurrentItem(next, true);
                handler.postDelayed(this, 15 * 1000);
            }
        };
        handler.postDelayed(autoScroll, 15 * 1000);
    }

    public void articleView() {
        List<ArticleItem> articles = new ArrayList<>();

        articles.add(new ArticleItem(
                getString(R.string.article_title_1),
                getString(R.string.article_snippet_1),
                getString(R.string.article_meta_1),
                R.drawable.phytoplankton_c,
                getString(R.string.article_url_1)
        ));

        articles.add(new ArticleItem(
                getString(R.string.article_title_2),
                getString(R.string.article_snippet_2),
                getString(R.string.article_meta_2),
                R.drawable.phytoplankton_a,
                getString(R.string.article_url_2)
        ));

        articles.add(new ArticleItem(
                getString(R.string.article_title_3),
                getString(R.string.article_snippet_3),
                getString(R.string.article_meta_3),
                R.drawable.phytoplankton,
                getString(R.string.article_url_3)
        ));

        articles.add(new ArticleItem(
                getString(R.string.article_title_4),
                getString(R.string.article_snippet_4),
                getString(R.string.article_meta_4),
                R.drawable.phytoplankton_e,
                getString(R.string.article_url_4)
        ));

        articles.add(new ArticleItem(
                getString(R.string.article_title_5),
                getString(R.string.article_snippet_5),
                getString(R.string.article_meta_5),
                R.drawable.phytoplankton_c,
                getString(R.string.article_url_5)
        ));

        articles.add(new ArticleItem(
                getString(R.string.article_title_6),
                getString(R.string.article_snippet_6),
                getString(R.string.article_meta_6),
                R.drawable.phytoplankton_d,
                getString(R.string.article_url_6)
        ));

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