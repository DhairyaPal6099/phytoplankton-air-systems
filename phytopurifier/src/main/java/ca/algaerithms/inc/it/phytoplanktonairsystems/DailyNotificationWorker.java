package ca.algaerithms.inc.it.phytoplanktonairsystems;
import android.content.Context;
import android.util.Log;

import androidx.work.ListenableWorker;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DatabaseError;

import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.SensorDataManager;

public class DailyNotificationWorker extends ListenableWorker {
    public DailyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            Log.d("DailyWorker", "Running daily notification check...");

            SensorDataManager.getInstance().getSensorLatestData(new SensorDataManager.SensorDataCallback() {
                @Override
                public void onDataFetched(SensorData data) {
                    if (data != null) {
                        Log.d("DailyWorker", "Sensor data fetched: algae=" + data.getAlgaeHealth() + ", turbidity=" + data.getTurbidity());

                        NotificationManagerPhytopurifier.getInstance(getApplicationContext())
                                .sendEndOfDayAlgaeStatus(data.getAlgaeHealth(), data.getTurbidity());
                        completer.set(Result.success());
                    } else {
                        Log.e("DailyWorker", "Sensor data null.");
                        completer.set(Result.failure());
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("DailyWorker", "Sensor fetch failed: " + error.getMessage());
                    completer.set(Result.failure());
                }
            });

            return "SensorDataFetch";  // tag for debugging
        });
    }
}
