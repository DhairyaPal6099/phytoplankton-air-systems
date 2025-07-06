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

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementManager {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static AchievementManager instance;

    private AchievementManager() {
    }

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public void checkAndAddAchievement(String title, String message) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        DocumentReference reference = firestore.collection("users").document(uid);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Map<String, Object>> achievements = (List<Map<String, Object>>) documentSnapshot.get("achievements");
                if (achievements == null) {
                    achievements = new ArrayList<>();
                }

                boolean alreadyHas = false;
                for (Map<String, Object> achievement : achievements) {
                    if (title.equals(achievement.get("title"))) {
                        alreadyHas = true;
                        break;
                    }
                }

                if (!alreadyHas) {
                    Map<String, Object> newAchievement = new HashMap<>();
                    newAchievement.put("title", title);
                    newAchievement.put("message", message);
                    newAchievement.put("timestamp", new Timestamp(new Date()));

                    if (!title.isEmpty() && !message.isEmpty()) {
                        reference.update("achievements", FieldValue.arrayUnion(newAchievement));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AchievementManager", "Failed to get document: " + e);
            }
        });
    }

    public void getAllAchievements(Consumer<List<AchievementModel>> callback) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        firestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<Map<String, Object>> achievementList = (List<Map<String, Object>>) documentSnapshot.get("achievements");
                List<AchievementModel> results = new ArrayList<>();
                if (achievementList != null) {
                    for (Map<String, Object> achievement : achievementList) {
                        if (achievement != null) {
                            String title = (String) achievement.get("title");
                            String message = (String) achievement.get("message");

                            if (title != null && message != null && !title.trim().isEmpty() && !message.trim().isEmpty()) {
                                Timestamp timestamp;
                                if (achievement.get("timestamp") == null) {
                                    timestamp = new Timestamp(new Date());
                                } else {
                                    timestamp = (Timestamp) achievement.get("timestamp");
                                }
                                results.add(new AchievementModel(title, message, timestamp.toDate()));
                            }
                        }
                    }
                }

                callback.accept(results);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AchievementManager", "Failed to retrieve achievements: " + e);
            }
        });
    }

    public  void evaluateCo2Achievements() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    Double co2 = doc.getDouble("lifetime_co2_converted");
                    if (co2 == null) return;

                    if (co2 > 10000) {
                        checkAndAddAchievement("Phresh Air Master", "Converted over 10,000kg of CO₂!");
                    } else if (co2 > 5000) {
                        checkAndAddAchievement("Carbon Catcher", "Converted over 5,000kg of CO₂!");
                    } else if (co2 > 1000) {
                        checkAndAddAchievement("Phytopurifier Pro", "Converted over 1,000kg of CO₂!");
                    } else if (co2 > 250) {
                        checkAndAddAchievement("Fresh Air Fan", "Converted over 250kg of CO₂!");
                    } else if (co2 > 25) {
                        checkAndAddAchievement("Rejuvenation Rookie", "Converted over 25kg of CO₂!");
                    }
                });
    }
}
