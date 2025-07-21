/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard.UserStat;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserStat> userStats;
    private Context context;

    public LeaderboardAdapter(Context context, List<UserStat> userStats) {
        this.context = context;
        this.userStats = userStats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, carbonDioxideText;
        ImageView medalIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.user_name);
            carbonDioxideText = itemView.findViewById(R.id.carbondioxide_amount);
            medalIcon = itemView.findViewById(R.id.medal_icon);
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
        holder.carbonDioxideText.setText(user.carbonDioxideConvertedKg + " kg O₂");

        if(position == 0){
            holder.medalIcon.setVisibility(View.VISIBLE);
            holder.medalIcon.setImageResource(R.drawable.medal1st);
            holder.nameText.setTextColor(ContextCompat.getColor(context, R.color.gold));
        } else if(position == 1){
            holder.medalIcon.setVisibility(View.VISIBLE);
            holder.medalIcon.setImageResource(R.drawable.medal2nd);
            holder.nameText.setTextColor(ContextCompat.getColor(context, R.color.silver));
        } else if(position == 2){
            holder.medalIcon.setVisibility(View.VISIBLE);
            holder.medalIcon.setImageResource(R.drawable.medal3rd);
            holder.nameText.setTextColor(ContextCompat.getColor(context, R.color.bronze));
        }
    }

    @Override
    public int getItemCount() {
        return userStats.size();
    }
}

