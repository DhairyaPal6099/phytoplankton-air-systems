package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

public class FeedbackModel {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public FeedbackModel() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public boolean isUserSignedIn() {
        return auth.getCurrentUser() != null;
    }

    public String getUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    public void getUserName(OnCompleteListener<DocumentSnapshot> listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(listener);
        }
    }

    public void getLastFeedbackTime(OnCompleteListener<DocumentSnapshot> listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get().addOnCompleteListener(listener);
        }
    }

    public void submitFeedback(Map<String, Object> data, OnCompleteListener<Void> listener) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            data.put("userId", user.getUid());
            db.collection("feedbacks").document(user.getUid())
                    .set(data, SetOptions.merge())
                    .addOnCompleteListener(listener);
            db.collection("users").document(user.getUid()).update(Map.of("feedback_disabled_time", System.currentTimeMillis()));
        }
    }

    public String getDeviceModel() {
        return "sdk_" + Build.MODEL.replace(" ", "_").toLowerCase();
    }
}
