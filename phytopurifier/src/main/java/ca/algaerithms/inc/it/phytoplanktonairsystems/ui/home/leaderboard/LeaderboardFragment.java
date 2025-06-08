package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.home.leaderboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentLeaderboardBinding;


public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecyclyerView;
    private LeaderboardAdapter adapter;
    private List<UserStat> userStatList;

    private FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardRecyclyerView = view.findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclyerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Sample Data
        userStatList = new ArrayList<>();
        userStatList.add(new UserStat("Scotia Bank", 2000));
        userStatList.add(new UserStat("RBC", 1800));
        userStatList.add(new UserStat("TD", 1500));

        adapter = new LeaderboardAdapter(getContext(), userStatList);
        leaderboardRecyclyerView.setAdapter(adapter);


        return view;
    }
}