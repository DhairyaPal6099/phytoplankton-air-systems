package ca.algaerithms.inc.it.phytoplanktonairsystems;

import static org.junit.Assert.*;
import org.junit.Test;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard.UserStat;

public class UserStatTest {

    @Test
    public void testNameIsSetCorrectly() {
        UserStat user = new UserStat("Sanskriti", 12.345);
        assertEquals("Sanskriti", user.name);
    }

    @Test
    public void testCarbonDioxideRoundedToTwoDecimalPlaces() {
        UserStat user = new UserStat("Dharmik", 12.34567);
        assertEquals(12.35, user.carbonDioxideConvertedKg, 0.001);
    }

    @Test
    public void testCarbonDioxideRoundedDown() {
        UserStat user = new UserStat("Dhairya", 3.454);
        assertEquals(3.45, user.carbonDioxideConvertedKg, 0.001);
    }

    @Test
    public void testCarbonDioxideRoundedUp() {
        UserStat user = new UserStat("Julian", 3.456);
        assertEquals(3.46, user.carbonDioxideConvertedKg, 0.001);
    }

    @Test
    public void testNameIsNotNull() {
        UserStat user = new UserStat("Eve", 10.0);
        assertNotNull(user.name);
    }

    @Test
    public void testCO2IsNotNull() {
        UserStat user = new UserStat("Frank", 5.5);
        assertNotNull(user.carbonDioxideConvertedKg);
    }

    @Test
    public void testDifferentUsersHaveDifferentNames() {
        UserStat user1 = new UserStat("George", 7.0);
        UserStat user2 = new UserStat("Helen", 7.0);
        assertNotEquals(user1.name, user2.name);
    }

    @Test
    public void testSameNameDifferentCO2() {
        UserStat user1 = new UserStat("Sanskriti", 3.0);
        UserStat user2 = new UserStat("Sanskriti", 4.0);
        assertNotEquals(user1.carbonDioxideConvertedKg, user2.carbonDioxideConvertedKg);
    }

    @Test
    public void testValueIsPositive() {
        UserStat user = new UserStat("Jack", 15.0);
        assertTrue(user.carbonDioxideConvertedKg > 0);
    }

    @Test
    public void testZeroValueHandling() {
        UserStat user = new UserStat("Kate", 0.0);
        assertEquals(0.0, user.carbonDioxideConvertedKg, 0.001);
    }
}
