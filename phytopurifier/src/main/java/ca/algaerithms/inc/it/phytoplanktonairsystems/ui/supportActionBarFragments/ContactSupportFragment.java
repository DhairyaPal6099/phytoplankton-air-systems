package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.supportActionBarFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        Button emailButton = binding.emailButton;
        Button phoneButton = binding.phoneButton;

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(getString(R.string.mailto_algaerithms_gmail_com)));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_request));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_email_app_found), Toast.LENGTH_SHORT).show();
                }
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