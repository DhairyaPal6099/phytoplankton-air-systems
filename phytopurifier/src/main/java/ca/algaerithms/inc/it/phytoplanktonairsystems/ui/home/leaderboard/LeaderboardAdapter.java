package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.home.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserStat> userStats;

    public LeaderboardAdapter(List<UserStat> userStats) {
        this.userStats = userStats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, oxygenText;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.user_name);
            oxygenText = itemView.findViewById(R.id.oxygen_amount);
        }
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
        UserStat user = userStats.get(position);
        holder.nameText.setText(user.name);
        holder.oxygenText.setText(user.oxygenProducedKg + " kg O₂");
    }

    @Override
    public int getItemCount() {
        return userStats.size();
    }
}

