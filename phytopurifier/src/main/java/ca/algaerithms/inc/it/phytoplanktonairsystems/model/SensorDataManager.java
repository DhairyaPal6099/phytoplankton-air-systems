/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorDataManager {
    private static SensorDataManager instance;
    private final DatabaseReference databaseRef;
    private ValueEventListener liveSensorListener;

    private final MutableLiveData<SensorData> sensorLiveData = new MutableLiveData<>();


    private SensorDataManager() {
        databaseRef = FirebaseDatabase.getInstance().getReference("device_001");
    }

    public static SensorDataManager getInstance() {
        if (instance == null) {
            instance = new SensorDataManager();
        }
        return instance;
    }

    public interface SensorDataCallback {
        void onDataFetched(SensorData data);
        void onError(DatabaseError error);
    }

    public void getSensorLatestData(SensorDataCallback callback) {
        databaseRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        SensorData data = child.getValue(SensorData.class);
                        callback.onDataFetched(data);
                        return;
                    }
                    callback.onDataFetched(null);
                } else {
                    callback.onDataFetched(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public void startListeningToSensorData(SensorDataCallback callback) {
        liveSensorListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SensorData data = snapshot.getValue(SensorData.class);
                    if (data != null) {
                        sensorLiveData.postValue(data);
                        callback.onDataFetched(data);
                    }
                } else {
                    callback.onDataFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        };

        databaseRef.addValueEventListener(liveSensorListener);
    }

    public void stopListening() {
        if (liveSensorListener != null) {
            databaseRef.removeEventListener(liveSensorListener);
            liveSensorListener = null;
        }
    }

    public LiveData<SensorData> getSensorLiveData() {
        return sensorLiveData;
    }
}