package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
        if (uid == null) { return; }

        firestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Map<String, Object>> notificationList = (List<Map<String, Object>>) documentSnapshot.get("notifications");
                List<NotificationModel> results = new ArrayList<>();
                if (notificationList != null) {
                    for (Map<String, Object> notification :  notificationList) {
                        if (notification != null) {
                            String title = (String) notification.get("title");
                            String message = (String) notification.get("message");
                            if (title != null && message != null && !title.trim().isEmpty() && !message.trim().isEmpty()) {
                                Timestamp timestamp;
                                if (notification.get("timestamp") == null) {
                                    timestamp = new Timestamp(new Date());
                                } else {
                                    timestamp = (Timestamp) notification.get("timestamp");
                                }
                                results.add(new NotificationModel(title, message, timestamp.toDate()));
                            }
                        }
                    }
                }
                callback.accept(results);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("NotificationManager", "Failed to retrieve notifications: " + e);
            }
        });
    }

    public void sendNotification() {}

    private void addNotificationToFirestore() {}
}
