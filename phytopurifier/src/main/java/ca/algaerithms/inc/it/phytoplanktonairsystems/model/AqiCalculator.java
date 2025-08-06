package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

public class AqiCalculator {
        public static int calculateAqi(SensorData data) {
            double co2 = data.getCo2();
            double temperature = data.getTemperature();
            double humidity = data.getHumidity();

            int aqi = 0;

            if (co2 <= 600) aqi += 0;
            else if (co2 <= 1000) aqi += 50;
            else aqi += 100;

            if (temperature >= 18 && temperature <= 25) aqi += 0;
            else aqi += 25;

            if (humidity >= 30 && humidity <= 60) aqi += 0;
            else aqi += 25;

            return Math.min(aqi, 500);
        }
    }