/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorData;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.SensorDataManager;

public class DailyNotificationWorker extends ListenableWorker {

    public DailyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            boolean onlyWeeklyStat = getInputData().getBoolean("weekly_only", false);

            SensorDataManager.getInstance().getSensorLatestData(new SensorDataManager.SensorDataCallback() {
                @Override
                public void onDataFetched(SensorData data) {
                    if (data != null) {
                        NotificationManagerPhytopurifier notifier = NotificationManagerPhytopurifier.getInstance(getApplicationContext());

                        if (onlyWeeklyStat) {
                            Calendar cal = Calendar.getInstance();
                            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                                notifier.sendNotification(
                                        "Weekly Stats 📊",
                                        "Check your weekly algae performance summary in the app.",
                                        "WEEKLY"
                                );
                            }
                            completer.set(Result.success());
                            return;
                        }

                        if (data.getAlgaeHealth() < 60 || data.getLight() < 3 || data.getCo2_converted() > 800) {
                            notifier.sendNotification("Algae Health Critical ⚠️",
                                    "Algae health is deteriorating. Check light, CO₂, and tank clarity.",
                                    "ALERT");
                        }

                        if (data.getWaterLevel() < 30) {
                            notifier.sendNotification("Water Refill Needed 💧",
                                    "Water level is below 30%. Please refill the tank.",
                                    "WATER");
                        }

                        notifier.sendNotification("Daily Algae Status 🌿",
                                (data.getAlgaeHealth() >= 85.0 && data.getTurbidity() <= 150.0)
                                        ? "Your algae is thriving today! Keep it up. 🌱"
                                        : (data.getAlgaeHealth() >= 60.0)
                                        ? "Your algae is doing okay, but could use some attention."
                                        : "Your algae's condition is deteriorating. Please check light and water levels!",
                                "EOD");

                        completer.set(Result.success());
                    } else {
                        completer.set(Result.failure());
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("DailyWorker", "Sensor model fetch failed: " + error.getMessage());
                    completer.set(Result.failure());
                }
            });

            return "SensorDataFetch";
        });
    }
}
