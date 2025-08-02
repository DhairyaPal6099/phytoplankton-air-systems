/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.supportActionBarFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentContactSupportBinding;

public class ContactSupportFragment extends Fragment {

    private FragmentContactSupportBinding binding;

    public ContactSupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactSupportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set custom illustration
        ImageView gifImageView = binding.gifImageView;
        gifImageView.setImageResource(R.drawable.ic_contact);

        // Set up card listeners
        CardView cardEmail = binding.cardEmail;
        CardView cardCall = binding.cardCall;

        cardEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:algaerithms@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_request));
            startActivity(intent);
            Toast.makeText(getContext(), "Opening email...", Toast.LENGTH_SHORT).show();
        });

        cardCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(getString(R.string.contact_support_phonenumber)));
            startActivity(intent);
            Toast.makeText(getContext(), "Opening phone dialer...", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
