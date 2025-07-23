/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.leaderboard.UserStat;

public class LeaderboardManager {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private List<UserStat> topTen = new ArrayList<>();

    public void fetchTopTen(OnLeaderboardFetchedListener listener) {
        firestore.collection("users")
                .orderBy("lifetime_co2_converted", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        topTen.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            String name = doc.getString("name");
                            Double co2 = doc.getDouble("lifetime_co2_converted");
                            if (name != null && co2 != null) {
                                topTen.add(new UserStat(name, co2));
                            }
                        }
                        if (listener != null) listener.onSuccess(topTen);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (listener != null) listener.onFailure(e);
                    }
                });
    }

    public interface OnLeaderboardFetchedListener {
        void onSuccess(List<UserStat> leaderboard);
        void onFailure(Exception e);
    }
}
