package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DatabaseError;

public class DailyNotificationWorker extends Worker {

    public DailyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("DailyWorker", "Running daily notification check...");

        SensorDataManager.getInstance().getSensorLatestData(new SensorDataManager.SensorDataCallback() {
            @Override
            public void onDataFetched(SensorData data) {
                if (data != null) {
                    NotificationManagerPhytopurifier.getInstance(getApplicationContext()).sendEndOfDayAlgaeStatus(data.getAlgaeHealth(), data.getTurbidity());
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("DailyWorker", "Sensor data fetch failed: " + error.getMessage());
            }
        });

        return Result.success();
    }
}