/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import java.util.Date;

public class SensorData {
    private int light;
    private int turbidity;
    private double co2Converted;
    private boolean proximity;
    private double waterLevel;  // percentage (0–100)

    private Date lastUpdated;

    private double co2_converted;
    private double algaeHealth;
    private String timestamp;

    private int aqi;

    // No-arg constructor required for Firebase
    public SensorData() {}

    // Getters
    public int getTurbidity() {
        return turbidity;
    }

    public int getLight() {
        return light;
    }

    public double getCo2Converted() {
        return co2Converted;
    }

    public boolean isProximity() {
        return proximity;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setTurbidity(int turbidity) {
        this.turbidity = turbidity;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setCo2Converted(double co2Converted) {
        this.co2Converted = co2Converted;
    }

    public void setProximity(boolean proximity) {
        this.proximity = proximity;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public double getCo2_converted() {
        return co2_converted;
    }

    public void setCo2_converted(double co2_converted) {
        this.co2_converted = co2_converted;
    }

    public void setAlgaeHealth(double algaeHealth) {
        this.algaeHealth = algaeHealth;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int calculateAqi() {
        // Normalize turbidity to 0–100 (assuming max turbidity = 100)
        double turbidityScore = Math.min(100, turbidity);

        // Normalize CO2 converted to 0–100 (assume max = 10g for now)
        double co2Score = Math.min(100, (co2Converted / 10.0) * 100);

        // Water level score: ideal is above 50%
        double waterLevelScore = Math.min(100, (waterLevel / 100.0) * 100);

        // Algae health is already in 0–100
        double algaeScore = Math.min(100, getAlgaeHealth());

        // Weights
        double aqi = (0.3 * turbidityScore) +
                (0.3 * co2Score) +
                (0.2 * waterLevelScore) +
                (0.2 * algaeScore);

        return (int) Math.round(aqi);
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public double getAlgaeHealth() {
        // Placeholder logic
        return turbidity > 0 ? Math.min(100, turbidity * 1.0) : 0;
    }
}