/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CO2Updater extends Worker {
    private static final DatabaseReference rtdbRef = FirebaseDatabase.getInstance().getReference("device_001/co2_converted");
    private static final CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

    public CO2Updater(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public void updateAllUsersWithCO2Converted() {
        rtdbRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;

                Double co2Converted = dataSnapshot.getValue(Double.class);
                if (co2Converted == null || co2Converted <= 0) return;

                usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot userDoc : queryDocumentSnapshots.getDocuments()) {
                            DocumentReference userRef = userDoc.getReference();

                            Double currentTotal = userDoc.getDouble("lifetime_co2_converted");
                            if (currentTotal == null) currentTotal = 0.0;

                            double updatedTotal = currentTotal + co2Converted;

                            Map<String, Object> updateMap = new HashMap<>();
                            updateMap.put("lifetime_co2_converted", updatedTotal);
                            userRef.update(updateMap);
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("CO2Updater", "Worker started: syncing CO2 data...");
        updateAllUsersWithCO2Converted();
        return Result.success();
    }
}
