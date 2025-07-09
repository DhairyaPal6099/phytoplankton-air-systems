package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class NotificationManager {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static NotificationManager instance;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
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
                .addOnFailureListener(e -> Log.e("NotificationManager", "Fetch failed: " + e.getMessage()));
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
                .addOnFailureListener(e -> Log.e("NotificationManager", "Send failed: " + e.getMessage()));
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
                .addOnSuccessListener(aVoid -> Log.d("NotificationManager", "End-of-day algae status added"))
                .addOnFailureListener(e -> Log.e("NotificationManager", "Failed to add notification", e));
    }
}
