package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

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


}
