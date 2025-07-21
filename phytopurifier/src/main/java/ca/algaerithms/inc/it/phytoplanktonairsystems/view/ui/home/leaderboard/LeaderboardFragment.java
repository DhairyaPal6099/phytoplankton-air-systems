/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.LeaderboardManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter.LeaderboardAdapter;


public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecyclyerView;
    private LeaderboardAdapter adapter;
    private List<UserStat> userStatList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardRecyclyerView = view.findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclyerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LeaderboardManager manager = new LeaderboardManager();

        adapter = new LeaderboardAdapter(getContext(), userStatList);
        leaderboardRecyclyerView.setAdapter(adapter);
        manager.fetchTopTen(new LeaderboardManager.OnLeaderboardFetchedListener() {
            @Override
            public void onSuccess(List<UserStat> leaderboard) {
                userStatList = leaderboard;
                adapter.updateData(leaderboard);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to fetch leaderboard from database", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}