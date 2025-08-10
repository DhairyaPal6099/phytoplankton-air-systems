/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import java.util.Date;

public class SensorData {
    private double co2;
    private double humidity;
    private double temperature;

    // Added fields
    private double algaeHealth;
    private double light;
    private double co2_converted;
    private double waterLevel;
    private double turbidity;
    private long timestamp;

    private boolean proximity;

    // Default constructor (required for Firebase)
    public SensorData() {}

    public SensorData(double co2, double humidity, double temperature) {
        this.co2 = co2;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    // --- Existing Getters/Setters ---
    public double getCo2() { return co2; }
    public void setCo2(double co2) { this.co2 = co2; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public long gettimestamp() { return timestamp; }
    public void settimestamp(long timestamp) { this.timestamp = timestamp; }

    // --- Added Getters/Setters ---
    public double getAlgaeHealth() { return algaeHealth; }
    public void setAlgaeHealth(double algaeHealth) { this.algaeHealth = algaeHealth; }

    public double getLight() { return light; }
    public void setLight(double light) { this.light = light; }

    public boolean isProximity() { return proximity; }
    public void setProximity(boolean proximity) { this.proximity = proximity; }

    public double getCo2_converted() { return co2_converted; }
    public void setCo2_converted(double co2_converted) { this.co2_converted = co2_converted; }

    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) { this.waterLevel = waterLevel; }

    public double getTurbidity() { return turbidity; }
    public void setTurbidity(double turbidity) { this.turbidity = turbidity; }

    // --- AQI Calculator Logic ---
    public int calculateAqi() {
        double score = 0.0;
        if (co2 > 1000) score += 50;
        else if (co2 > 800) score += 40;
        else score += 20;

        if (humidity < 30 || humidity > 60) score += 30;
        else score += 10;

        if (temperature < 18 || temperature > 26) score += 20;
        else score += 10;

        return (int) Math.min(score, 100);
    }
}