package ca.algaerithms.inc.it.phytoplanktonairsystems;

import java.util.Date;

public class SensorData {
    private int light;
    private int turbidity;
    private double co2Converted;
    private boolean proximity;
    private Date lastUpdated;

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

    // Setters - Needed for Firebase to deserialize
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

    // TODO: Implement algae health calculation based on sensor values
    public double getAlgaeHealth() {
        // Placeholder logic
        return turbidity > 0 ? Math.min(100, turbidity * 1.0) : 0;
    }
}