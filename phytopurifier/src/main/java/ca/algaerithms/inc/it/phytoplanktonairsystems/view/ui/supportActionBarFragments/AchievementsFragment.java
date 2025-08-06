/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.supportActionBarFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.AchievementManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.AchievementModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter.AchievementAdapter;

public class AchievementsFragment extends Fragment {

    private final SensorDataManager sensorManager = SensorDataManager.getInstance();
    private final AchievementManager achievementManager = AchievementManager.getInstance();
    private AchievementAdapter adapter;
    private List<AchievementModel> achievementList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.achievementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AchievementAdapter(achievementList);
        recyclerView.setAdapter(adapter);

        //Loading existing achievements from Firestore
        achievementManager.getAllAchievements(fetchedAchievements -> {
            achievementList.clear();
            achievementList.addAll(fetchedAchievements);
            adapter.notifyDataSetChanged();
        });

        //Observe sensor model and evaluate achievements
        sensorManager.getSensorLiveData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                achievementManager.evaluateCo2Achievements();
                checkAlgaeAchievements(data.getAlgaeHealth(), new Date(data.gettimestamp()));
            }
        });

        return view;
    }

    private void checkAlgaeAchievements(double health, Date timestamp) {
        if (health < 85.0 || timestamp == null) return;

        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.setTime(timestamp);

        long months = (now.get(Calendar.YEAR) - last.get(Calendar.YEAR)) * 12L
                + (now.get(Calendar.MONTH) - last.get(Calendar.MONTH));

        if (months >= 12)
            achievementManager.checkAndAddAchievement("Eco Legend 🌍", "Algae health above 85% for 12 months!");
        else if (months >= 6)
            achievementManager.checkAndAddAchievement("Eco Expert 🍀", "Algae health above 85% for 6 months!");
        else if (months >= 3)
            achievementManager.checkAndAddAchievement("Green Guardian 🌿", "Algae health above 85% for 3 months!");
        else if (months >= 1)
            achievementManager.checkAndAddAchievement("Steady Start 🌱", "Algae health above 85% for 1 month!");
    }
}