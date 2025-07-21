package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.settings;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class PrivacyPolicyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        TextView privacyTextView = view.findViewById(R.id.privacy_content);
        privacyTextView.setText(getString(R.string.privacy_policy_content));



        return view;
    }
}
