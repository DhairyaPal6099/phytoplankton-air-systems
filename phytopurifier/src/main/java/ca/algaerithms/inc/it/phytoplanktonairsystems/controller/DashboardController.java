package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.database.DatabaseError;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.AqiCalculator;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.dashboard.DashboardView;

public class DashboardController {
    private final DashboardView view;
    private final SensorDataManager sensorDataManager;

    public DashboardController(DashboardView view) {
        this.view = view;
        this.sensorDataManager = SensorDataManager.getInstance();
    }

    public DashboardController(DashboardView view, LifecycleOwner lifecycleOwner) {
        this.view = view;
        this.sensorDataManager = SensorDataManager.getInstance();

        sensorDataManager.getSensorLiveData().observe(lifecycleOwner, data -> {
            if (data != null) {
                int aqi = AqiCalculator.calculateAqi(data);
                view.showAqi(aqi);
                view.showLight(data.getLight());
                view.showTurbidity(data.getTurbidity());
                view.showProximity(data.isProximity());
            } else {
                view.showError("No sensor data available");
            }
        });
    }

    public void startListeningToSensorData() {
        sensorDataManager.startListeningToSensorData(new SensorDataManager.SensorDataCallback() {
            @Override
            public void onDataFetched(SensorData data) {
                // No-op here because we already observe LiveData
            }

            @Override
            public void onError(DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void stopListeningToSensorData() {
        sensorDataManager.stopListening();
    }
}
