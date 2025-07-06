package ca.algaerithms.inc.it.phytoplanktonairsystems;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorDataManager {
    private final DatabaseReference deviceRef;
    private final MutableLiveData<SensorData> sensorLiveData = new MutableLiveData<>();
    private static SensorDataManager instance;

    private SensorDataManager() {
        deviceRef = FirebaseDatabase.getInstance().getReference("device_001");
        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SensorData data = snapshot.getValue(SensorData.class);
                sensorLiveData.setValue(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to fetch sensor data
                //If this happens often generate a notification
            }
        });
    }

    public static SensorDataManager getInstance() {
        if (instance == null) {
            instance = new SensorDataManager();
        }
        return instance;
    }

    public LiveData<SensorData> getSensorLiveData() {
        return sensorLiveData;
    }
}
