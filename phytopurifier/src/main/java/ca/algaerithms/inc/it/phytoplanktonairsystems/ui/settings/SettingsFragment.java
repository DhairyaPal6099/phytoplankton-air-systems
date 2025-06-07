/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.algaerithms.inc.it.phytoplanktonairsystems.databinding.FragmentSettingsBinding;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String[] settingTitles = getResources().getStringArray(R.array.settings_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.list_item_setting,
                R.id.setting_title,
                settingTitles
        );

        binding.settingsList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
