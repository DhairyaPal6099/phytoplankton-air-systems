package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;

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
                        Log.d("DailyWorker", "Fetched Sensor Data:");
                        Log.d("DailyWorker", "AlgaeHealth = " + data.getAlgaeHealth());
                        Log.d("DailyWorker", "Turbidity = " + data.getTurbidity());
                        Log.d("DailyWorker", "CO2 = " + data.getCo2_converted());
                        Log.d("DailyWorker", "Light = " + data.getLight());
                        Log.d("DailyWorker", "Water Level = " + data.getWaterLevel());

                        NotificationManagerPhytopurifier notifier = NotificationManagerPhytopurifier.getInstance(getApplicationContext());

                        // 🔴 Algae Health Critical
                        if (data.getAlgaeHealth() < 60 || data.getLight() < 3 || data.getCo2_converted() > 800) {
                            notifier.sendNotification(
                                    "Algae Health Critical ⚠️",
                                    "Algae health is deteriorating. Check light, CO₂, and tank clarity.",
                                    "ALERT"
                            );
                        }

                        // 🟠 Water Refill Notification
                        if (data.getWaterLevel() < 30) {
                            notifier.sendNotification(
                                    "Water Refill Needed 💧",
                                    "Water level is below 30%. Please refill the tank.",
                                    "WATER"
                            );
                        }

                        // 🌿 EOD Status - Always sent
                        notifier.sendEndOfDayAlgaeStatus(data.getAlgaeHealth(), data.getTurbidity());

                        // 📊 Weekly Stats (only on Sundays)
                        Calendar cal = Calendar.getInstance();
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            notifier.sendNotification(
                                    "Weekly Stats 📊",
                                    "Check your weekly algae performance summary in the app.",
                                    "WEEKLY"
                            );
                        }

                        completer.set(Result.success());
                    } else {
                        Log.e("DailyWorker", "Sensor data is null.");
                        completer.set(Result.failure());
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("DailyWorker", "Sensor fetch failed: " + error.getMessage());
                    completer.set(Result.failure());
                }
            });

            return "SensorDataFetch"; // For debugging and tracing
        });
    }
}
