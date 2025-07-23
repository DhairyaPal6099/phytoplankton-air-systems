/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DatabaseError;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;

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
                Log.e("DailyWorker", "Sensor model fetch failed: " + error.getMessage());
            }
        });

        return Result.success();
    }
}