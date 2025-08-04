/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */
package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard;

import java.text.DecimalFormat;

public class UserStat {
    public String name;
    public Double carbonDioxideConvertedKg;

    public UserStat(String name, Double carbonDioxideConvertedKg) {
        this.name = name;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedValue = df.format(carbonDioxideConvertedKg);
        this.carbonDioxideConvertedKg = Double.parseDouble(formattedValue);
    }
}
