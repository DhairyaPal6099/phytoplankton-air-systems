/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.about;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        ImageView sanskriti = view.findViewById(R.id.icon_sanskriti);
        ImageView dharmik = view.findViewById(R.id.icon_dharmik);
        ImageView dhairya = view.findViewById(R.id.icon_dhairya);
        ImageView julian = view.findViewById(R.id.icon_julian);

        sanskriti.setOnClickListener(v -> {
            showBioDialog("Sanskriti – The Sun Whisperer",
                    "Wants to open a bakery but accidentally got looped into engineering. Still glowing. Ambivert with a killer reel game and a sense of humor that's 60% dark roast. Brings the light — and the punchlines.");
        });

        dharmik.setOnClickListener(v -> {
            showBioDialog("Dharmik – The Denial Field",
                    "Claims to have no friends, yet somehow knows everyone in a 3-building radius. Quiet coder, louder crew.");
        });

        dhairya.setOnClickListener(v -> {
            showBioDialog("Dhairya – The Air Bender",
                    "Leads the team like he plays guitar — steady, composed, and never missing a beat. Fluent in drums, piano, and problem-solving.");
        });

        julian.setOnClickListener(v -> {
            showBioDialog("Julian – The Turbidity Titan",
                    "Volleyball beast. Constantly in motion — from court to class to shift. Filipino finesse, great hair, and a girlfriend hotter than our CPU under load.");
        });




        return view;
    }

    private void showBioDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", null)
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}