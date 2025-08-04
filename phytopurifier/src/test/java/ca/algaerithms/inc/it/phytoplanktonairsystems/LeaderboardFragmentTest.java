package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter.LeaderboardAdapter;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard.UserStat;

public class LeaderboardFragmentTest {

    Context context = ApplicationProvider.getApplicationContext();


    @Test
    public void adapter_initializes_with_given_user_stats() {
        List<UserStat> mockList = Arrays.asList(new UserStat("Julian", 12.5));
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, mockList);
        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void adapter_updates_data_correctly() {
        List<UserStat> oldList = new ArrayList<>();
        oldList.add(new UserStat("Old", 5.0));
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, oldList);

        List<UserStat> newList = Arrays.asList(new UserStat("New", 10.0));
        adapter.updateData(newList);

        assertEquals(1, adapter.getItemCount());
    }

    @Test
    public void viewholder_binds_user_name_correctly() {
        UserStat user = new UserStat("Alice", 7.3);
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, List.of(user));

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 0);

        TextView nameText = view.findViewById(R.id.user_name);
        assertEquals("Alice", nameText.getText().toString());
    }

    @Test
    public void viewholder_binds_user_name_correctly() {
        UserStat user = new UserStat("Alice", 7.3);
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, List.of(user));

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 0);

        TextView nameText = view.findViewById(R.id.user_name);
        assertEquals("Alice", nameText.getText().toString());
    }

    @Test
    public void viewholder_sets_carbon_dioxide_text() {
        UserStat user = new UserStat("Bob", 8.6);
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, List.of(user));

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 0);

        TextView co2Text = view.findViewById(R.id.carbondioxide_amount);
        assertEquals("8.6 kg O₂", co2Text.getText().toString());
    }



}
