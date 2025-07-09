package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private List<AchievementModel> achievementList;

    public AchievementAdapter(List<AchievementModel> achievementList) {
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AchievementModel achievement = achievementList.get(position);
        holder.title.setText(achievement.getTitle());
        holder.message.setText(achievement.getMessage());
        holder.date.setText(achievement.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.achievementTitle);
            message = itemView.findViewById(R.id.achievementMessage);
            date = itemView.findViewById(R.id.achievementDate);
        }
    }
}