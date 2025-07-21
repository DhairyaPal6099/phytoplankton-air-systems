package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NotificationManagerPhytopurifier {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static NotificationManagerPhytopurifier instance;
    private final Context context;
    private static final String CHANNEL_ID = "eod_channel";

    private NotificationManagerPhytopurifier(Context context) {
        this.context = context.getApplicationContext();
        createNotificationChannel();
    }

    public static NotificationManagerPhytopurifier getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationManagerPhytopurifier(context);
        }
        return instance;
    }

    public void getAllNotifications(Consumer<List<NotificationModel>> callback) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        firestore.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<Map<String, Object>> notifList = (List<Map<String, Object>>) documentSnapshot.get("notifications");
                    List<NotificationModel> result = new ArrayList<>();

                    if (notifList != null) {
                        for (Map<String, Object> notif : notifList) {
                            String title = (String) notif.get("title");
                            String message = (String) notif.get("message");
                            Timestamp timestamp = (Timestamp) notif.get("timestamp");
                            if (title != null && message != null && timestamp != null) {
                                result.add(new NotificationModel(title, message, timestamp.toDate()));
                            }
                        }
                    }

                    callback.accept(result);
                })
                .addOnFailureListener(e -> Log.e("NotificationManagerPhytopurifier", "Fetch failed: " + e.getMessage()));
    }

    public void sendNotification(String title, String message) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null || title.isEmpty() || message.isEmpty()) return;

        DocumentReference userRef = firestore.collection("users").document(uid);

        Map<String, Object> newNotif = new HashMap<>();
        newNotif.put("title", title);
        newNotif.put("message", message);
        newNotif.put("timestamp", new Timestamp(new Date()));

        userRef.update("notifications", FieldValue.arrayUnion(newNotif))
                .addOnFailureListener(e -> Log.e("NotificationManagerPhytopurifier", "Send failed: " + e.getMessage()));
    }

    private void showSystemNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify((int) System.currentTimeMillis(), builder.build());
    }

    public void sendEndOfDayAlgaeStatus(double algaeHealth, double turbidity) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        String statusMessage;

        if (algaeHealth >= 85.0 && turbidity <= 150.0) {
            statusMessage = "Your algae is thriving today! Keep it up. 🌱";
        } else if (algaeHealth >= 60.0) {
            statusMessage = "Your algae is doing okay, but could use some attention.";
        } else {
            statusMessage = "Your algae's condition is deteriorating. Please check light and water levels!";
        }

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", "Daily Algae Status 🌿");
        notification.put("message", statusMessage);
        notification.put("timestamp", new Timestamp(new Date()));

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update("notifications", FieldValue.arrayUnion(notification))
                .addOnSuccessListener(aVoid -> Log.d("NotificationManagerPhytopurifier", "End-of-day algae status added"))
                .addOnFailureListener(e -> Log.e("NotificationManagerPhytopurifier", "Failed to add notification", e));

        showSystemNotification("Daily algae status", statusMessage);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Phytopurifier Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Daily algae health and system status notifications");

            android.app.NotificationManager manager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
