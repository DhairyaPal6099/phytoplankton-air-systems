package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jspecify.annotations.NonNull;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<VideoItem> videoList;
    private OnVideoClickListener listener;

    // Interface for click callback
    public interface OnVideoClickListener {
        void onVideoClick(String videoId);
    }

    // Constructor
    public VideoAdapter(Context context, List<VideoItem> videoList, OnVideoClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    // Convenience constructor to preserve existing usage without listener
    public VideoAdapter(Context context, List<VideoItem> videoList) {
        this(context, videoList, null);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem item = videoList.get(position);

        holder.title.setText(item.getTitle());
        Glide.with(context).load(item.getThumbnailUrl()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVideoClick(item.getVideoId());  // Pass the videoId to the listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, playIcon;
        TextView title;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnailImageView);
            playIcon = itemView.findViewById(R.id.playIcon);
            title = itemView.findViewById(R.id.videoTitle);
        }
    }
}
