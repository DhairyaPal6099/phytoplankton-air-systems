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