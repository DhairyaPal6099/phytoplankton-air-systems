package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
        private List<ArticleItem> articles;
        private Context context;
        private OnArticleClickListener listener;

        public interface OnArticleClickListener {
            void onArticleClick(ArticleItem article);
        }

        public ArticleAdapter(Context context, List<ArticleItem> articles, OnArticleClickListener listener) {
            this.context = context;
            this.articles = articles;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_articles, parent, false);
            return new ArticleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
            ArticleItem article = articles.get(position);
            holder.title.setText(article.getTitle());
            holder.snippet.setText(article.getSnippet());
            holder.meta.setText(article.getMeta());
            holder.image.setImageResource(article.getImageResId());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onArticleClick(article);
                }
            });
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        static class ArticleViewHolder extends RecyclerView.ViewHolder {
            TextView title, snippet, meta;
            ImageView image;

            public ArticleViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.articleTitle);
                snippet = itemView.findViewById(R.id.articleSnippet);
                meta = itemView.findViewById(R.id.articleMeta);
                image = itemView.findViewById(R.id.articleImage);
            }
        }
    }