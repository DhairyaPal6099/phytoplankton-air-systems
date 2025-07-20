package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.about;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        LinearLayout teamScroll = view.findViewById(R.id.team_scroll);

        List<TeamMember> members = Arrays.asList(
                new TeamMember("Sanskriti", "Sun Whisperer. Accidentally an engineer.", R.drawable.ic_sanskriti),
                new TeamMember("Dharmik", "Quiet coder, louder crew.", R.drawable.ic_dharmik),
                new TeamMember("Dhairya", "Steady like his strum. Drums and design.", R.drawable.ic_dhairya),
                new TeamMember("Julian", "Turbidity Titan. Moves fast, lives faster.", R.drawable.ic_julian)
        );

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (TeamMember member : members) {
            View card = layoutInflater.inflate(R.layout.item_team_member_card, teamScroll, false);

            ImageView avatar = card.findViewById(R.id.avatar);
            TextView name = card.findViewById(R.id.name);
            TextView bio = card.findViewById(R.id.bio);

            avatar.setImageResource(member.imageRes);
            name.setText(member.name);
            bio.setText(member.bio);

            teamScroll.addView(card);
        }

        return view;
    }
}
