package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.supportActionBarFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

        Button emailButton = binding.emailButton;
        Button phoneButton = binding.phoneButton;

        ImageView gifImageView = binding.gifImageView;
        Glide.with(this).asGif().load(R.drawable.gif_contactsupport).into(gifImageView);

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:algaerithms@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_request));
                startActivity(intent);
            }
        });
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(getString(R.string.contact_support_phonenumber)));
                startActivity(intent);
            }
        });

        return view;
    }
}