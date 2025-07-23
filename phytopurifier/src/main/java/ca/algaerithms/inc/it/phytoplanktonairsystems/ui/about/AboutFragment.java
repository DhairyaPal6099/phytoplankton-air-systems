package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.about;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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
                new TeamMember(
                        "Sanskriti",
                        "Scrum Master & Developer",
                        "Not quite an engineer yet—just a crime doc junkie with a reluctant thing for code. Scrum Master by role, surviving by default. Weirdly good at writing bios.",
                        R.drawable.ic_sanskriti,
                        "https://www.linkedin.com/in/sanskriti-mansotra-70a045268/",
                        "https://github.com/SanskritiMansotra3183"
                ),
                new TeamMember(
                        "Dharmik",
                        "Main Developer & Database Manager",
                        "The introvert who somehow knows everyone. Constantly reminds us he’s the ‘coding guy’ — which he is… and somehow still weirds us out.",
                        R.drawable.ic_dharmik,
                        "https://www.linkedin.com/in/dharmik-shah-427261268/",
                        "https://github.com/DharmikShah1796"
                ),
                new TeamMember(
                        "Dhairya",
                        "Team Lead & Developer",
                        "Writes clean code, plays cleaner chords but still panics first after saying ‘don’t panic, guys.’",
                        R.drawable.ic_dhairya,
                        "https://www.linkedin.com/in/dhairyapal/",
                        "https://github.com/DhairyaPal6099"
                ),
                new TeamMember(
                        "Julian",
                        "Developer & Agile Support",
                        "Moves fast, codes faster. Spams rat stickers like a reflex and roams campus in Crocs like it's a uniform.",
                        R.drawable.ic_julian,
                        "https://www.linkedin.com/in/julian-aldrich-imperial-a629b0297/",
                        "https://github.com/JulianImperial8310"
                )
        );

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        for (TeamMember member : members) {
            View card = layoutInflater.inflate(R.layout.item_team_member_card, teamScroll, false);

            ImageView avatar = card.findViewById(R.id.avatarImageView);
            TextView name = card.findViewById(R.id.nameTextView);
            TextView role = card.findViewById(R.id.roleTextView);
            TextView bio = card.findViewById(R.id.bioTextView);
            ImageView linkedinIcon = card.findViewById(R.id.linkedinIcon);
            ImageView githubIcon = card.findViewById(R.id.githubIcon);
            CardView memberCard = card.findViewById(R.id.memberCard);

            avatar.setImageResource(member.imageRes);
            name.setText(member.name);
            role.setText(member.role);
            bio.setText(member.bio);

            int backgroundColor;
            switch (member.name) {
                case "Sanskriti":
                    backgroundColor = Color.parseColor("#F3E5F5"); // Soft Pink
                    break;
                case "Dharmik":
                    backgroundColor = Color.parseColor("#E6F7FF"); // Light Blue
                    break;
                case "Dhairya":
                    backgroundColor = Color.parseColor("#FFFDE7"); // Pale Yellow
                    break;
                case "Julian":
                    backgroundColor = Color.parseColor("#E8F5E9"); // Soft Green
                    break;
                default:
                    backgroundColor = Color.WHITE;
            }

            memberCard.setCardBackgroundColor(backgroundColor);

            linkedinIcon.setOnClickListener(v -> openUrl(member.linkedInUrl));
            githubIcon.setOnClickListener(v -> openUrl(member.githubUrl));

            teamScroll.addView(card);
        }

        return view;
    }

    private void openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse(url));
            startActivity(intent);
        }
    }
}
