package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter.LeaderboardAdapter;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard.UserStat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.R)
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
    public void viewholder_sets_carbon_dioxide_text() {
        UserStat user = new UserStat("Bob", 8.6);
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, List.of(user));

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 0);

        TextView co2Text = view.findViewById(R.id.carbondioxide_amount);
        assertEquals("8.6 kg O₂", co2Text.getText().toString());
    }

    @Test
    public void position_0_shows_gold_medal() {
        UserStat user = new UserStat("Gold", 20.0);
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, List.of(user));

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 0);

        ImageView medal = view.findViewById(R.id.medal_icon);
        assertEquals(View.VISIBLE, medal.getVisibility());
    }

    @Test
    public void position_1_shows_silver_medal() {
        List<UserStat> testUsers = Arrays.asList(
                new UserStat("User1", 20.0), // gold
                new UserStat("User2", 15.0)  // silver
        );

        LeaderboardAdapter adapter = new LeaderboardAdapter(context, testUsers);

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 1);

        ImageView medal = view.findViewById(R.id.medal_icon);
        assertEquals(View.VISIBLE, medal.getVisibility());
    }

    @Test
    public void position_2_shows_bronze_medal() {
        List<UserStat> testUsers = Arrays.asList(
                new UserStat("User1", 25.0), // gold
                new UserStat("User2", 20.0), // silver
                new UserStat("User3", 15.0)  // bronze
        );
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, testUsers);

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 2);

        ImageView medal = view.findViewById(R.id.medal_icon);
        assertEquals(View.VISIBLE, medal.getVisibility());
    }

    @Test
    public void position_3_has_no_medal() {
        List<UserStat> users = Arrays.asList(
                new UserStat("A", 5.0),
                new UserStat("B", 4.0),
                new UserStat("C", 3.0),
                new UserStat("D", 2.0) // index 3
        );
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, users);

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, null, false);
        LeaderboardAdapter.ViewHolder holder = new LeaderboardAdapter.ViewHolder(view);

        adapter.onBindViewHolder(holder, 3);
        ImageView medal = view.findViewById(R.id.medal_icon);

        assertEquals(View.GONE, medal.getVisibility());
    }

    @Test
    public void adapter_handles_empty_list() {
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, new ArrayList<>());
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void adapter_updateData_does_not_crash_with_empty_list() {
        LeaderboardAdapter adapter = new LeaderboardAdapter(context, new ArrayList<>());
        adapter.updateData(new ArrayList<>());
        assertEquals(0, adapter.getItemCount());
    }
}