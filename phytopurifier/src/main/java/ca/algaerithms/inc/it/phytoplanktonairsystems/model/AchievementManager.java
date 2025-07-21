package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementManager {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static AchievementManager instance;
    private static final List<String> ACHIEVEMENT_PRIORITY = List.of(
            "Phresh Air Master",
            "Carbon Catcher",
            "Phytopurifier Pro",
            "Fresh Air Fan",
            "Rejuvenation Rookie"
    );


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

                    achievements.add(newAchievement);
                    List<Map<String, Object>> sortedAchievements = sortAchievementsByPriority(achievements);

                    reference.set(new HashMap<String, Object>() {{put("achievements", sortedAchievements);}}, SetOptions.merge());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AchievementManager", "Failed to get document: " + e);
            }
        });
    }

    private List<Map<String, Object>> sortAchievementsByPriority(List<Map<String, Object>> achievements) {
        achievements.sort((a, b) -> {
            String titleA = (String) a.get("title");
            String titleB = (String) b.get("title");

            int indexA = ACHIEVEMENT_PRIORITY.indexOf(titleA);
            int indexB = ACHIEVEMENT_PRIORITY.indexOf(titleB);

            // If not found in priority list, push to bottom
            if (indexA == -1) indexA = Integer.MAX_VALUE;
            if (indexB == -1) indexB = Integer.MAX_VALUE;

            return Integer.compare(indexA, indexB);
        });

        return achievements;
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

    public void evaluateCo2Achievements() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        DocumentReference reference = firestore.collection("users").document(uid);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long co2Long = documentSnapshot.getLong("lifetime_co2_converted");
                if (co2Long == null) return;

                double co2 = co2Long.doubleValue();
                List<AchievementModel> earned = new ArrayList<>();

                if (co2 > 10000) earned.add(new AchievementModel("Phresh Air Master", "Converted over 10,000kg of CO₂!", new Date()));
                if (co2 > 5000) earned.add(new AchievementModel("Carbon Catcher", "Converted over 5,000kg of CO₂!", new Date()));
                if (co2 > 1000) earned.add(new AchievementModel("Phytopurifier Pro", "Converted over 1,000kg of CO₂!", new Date()));
                if (co2 > 250) earned.add(new AchievementModel("Fresh Air Fan", "Converted over 250kg of CO₂!", new Date()));
                if (co2 > 25) earned.add(new AchievementModel("Rejuvenation Rookie", "Converted over 25kg of CO₂!", new Date()));

                List<Map<String, Object>> existingAchievements = (List<Map<String, Object>>) documentSnapshot.get("achievements");
                if (existingAchievements == null) existingAchievements = new ArrayList<>();

                addAchievementsBulk(earned, existingAchievements, reference);
            }
        });
    }

    private void addAchievementsBulk(List<AchievementModel> newAchievements, List<Map<String, Object>> existingAchievements, DocumentReference reference) {
        for (AchievementModel data : newAchievements) {
            boolean alreadyHas = existingAchievements.stream()
                    .anyMatch(a -> data.getTitle().equals(a.get("title")));
            if (!alreadyHas) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", data.getTitle());
                map.put("message", data.getMessage());
                map.put("timestamp", new Timestamp(new Date()));
                existingAchievements.add(map);
            }
        }

        List<Map<String, Object>> sortedAchievements = sortAchievementsByPriority(existingAchievements);
        reference.set(new HashMap<String, Object>() {{ put("achievements", sortedAchievements); }}, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("AchievementManager", "Bulk update successful"))
                .addOnFailureListener(e -> Log.e("AchievementManager", "Bulk update failed: " + e));
    }

}
