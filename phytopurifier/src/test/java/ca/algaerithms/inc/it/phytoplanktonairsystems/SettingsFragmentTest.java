/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Switch;


import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.settings.SettingsFragment;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.R)
public class SettingsFragmentTest {
    private FragmentScenario<SettingsFragment> scenario;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        scenario = FragmentScenario.launchInContainer(SettingsFragment.class);
    }

    @Test
    public void testDarkModeSwitchUpdatesPreference() {
        scenario.onFragment(settingsFragment -> {
            Switch darkModeSwitch = settingsFragment.requireView().findViewById(R.id.darkModeSwitch);
            darkModeSwitch.setChecked(true);

            prefs = settingsFragment.requireContext().getSharedPreferences(settingsFragment.getString(R.string.settings_lowercase), Context.MODE_PRIVATE);

            boolean value = prefs.getBoolean(settingsFragment.getString(R.string.dark_mode), false);
            assertTrue(value);
        });
    }

    @Test
    public void testLockScreenSwitchSetsPreference() {
        scenario.onFragment(settingsFragment -> {
            Switch lockScreenSwitch = settingsFragment.requireView().findViewById(R.id.lockScreenModeSwitch);
            lockScreenSwitch.setChecked(true);

            prefs = settingsFragment.requireContext().getSharedPreferences(settingsFragment.getString(R.string.settings_lowercase), Context.MODE_PRIVATE);

            boolean value = prefs.getBoolean(settingsFragment.getString(R.string.lockportrait), false);
            assertTrue(value);
        });
    }

    @Test
    public void testReduceMotionSwitchUpdatesPreference() {
        scenario.onFragment(settingsFragment -> {
            Switch reduceMotionSwitch = settingsFragment.requireView().findViewById(R.id.reduceMotion);
            reduceMotionSwitch.setChecked(true);

            prefs = settingsFragment.requireContext().getSharedPreferences(settingsFragment.getString(R.string.settings_lowercase), Context.MODE_PRIVATE);

            boolean value = prefs.getBoolean(settingsFragment.getString(R.string.reduce_motion_key), false);
            assertTrue(value);
        });
    }

    @Test
    public void testReduceMotionToastMessage() {
        scenario.onFragment(fragment -> {
            Switch reduceMotionSwitch = fragment.requireView().findViewById(R.id.reduceMotion);
            reduceMotionSwitch.setChecked(true);

            String toastText = ShadowToast.getTextOfLatestToast();
            assertEquals("Animations Reduced", toastText);
        });
    }


    @Test
    public void testDarkModePreferencePersistsAfterRecreation() {
        scenario.onFragment(fragment -> {
            Switch darkModeSwitch = fragment.requireView().findViewById(R.id.darkModeSwitch);
            darkModeSwitch.setChecked(true);
        });

        scenario.recreate();

        scenario.onFragment(fragment -> {
            Switch darkModeSwitch = fragment.requireView().findViewById(R.id.darkModeSwitch);
            assertTrue(darkModeSwitch.isChecked());
        });
    }

}
