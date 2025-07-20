package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.about;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Get each clickable card by ID
        View sanskritiCell = view.findViewById(R.id.card_sanskriti);
        View dharmikCell   = view.findViewById(R.id.card_dharmik);
        View dhairyaCell   = view.findViewById(R.id.card_dhairya);
        View julianCell    = view.findViewById(R.id.card_julian);

        // Set click listeners
        sanskritiCell.setOnClickListener(v -> showBioDialog(
                "Sanskriti – The Sun Whisperer",
                "Wants to open a bakery but accidentally got looped into engineering. Still glowing. Ambivert with a killer reel game and a sense of humor that's 60% dark roast. Brings the light — and the punchlines.")
        );

        dharmikCell.setOnClickListener(v -> showBioDialog(
                "Dharmik – The Denial Field",
                "Claims to have no friends, yet somehow knows everyone in a 3-building radius. Quiet coder, louder crew.")
        );

        dhairyaCell.setOnClickListener(v -> showBioDialog(
                "Dhairya – The Air Bender",
                "Leads the team like he plays guitar — steady, composed, and never missing a beat. Fluent in drums, piano, and problem-solving.")
        );

        julianCell.setOnClickListener(v -> showBioDialog(
                "Julian – The Turbidity Titan",
                "Volleyball beast. Constantly in motion — from court to class to shift. Filipino finesse, great hair, and a girlfriend hotter than our CPU under load.")
        );

        return view;
    }

    private void showBioDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }
}
