package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.legal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentLegalBinding;

public class LegalFragment extends Fragment {

    private FragmentLegalBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLegalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.privacyPolicyText.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.privacyPolicyFragment));
        binding.termsOfServiceText.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.termsOfServiceFragment));

        return view;
    }
}