package ca.algaerithms.inc.it.phytoplanktonairsystems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShareDashboard {
    public static void prepareAndShareDashboard(Context context) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Double co2 = documentSnapshot.getDouble("lifetime_co2_converted");
                String highestAchievement = "-";
                List<Map<String, Object>> achievementsList = (List<Map<String, Object>>) documentSnapshot.get("achievements");
                if (achievementsList != null && !achievementsList.isEmpty()) {
                    Map<String, Object> topAchievement = achievementsList.get(0);
                    if (topAchievement != null) {
                        highestAchievement = String.valueOf(topAchievement.get("title"));
                    }
                }
                View shareView = createShareableDashboardView(context, co2, highestAchievement);
                shareViewAsImage(context, shareView);
            }
        });
    }
    public static View createShareableDashboardView(Context context, double lifetime_co2_converted, String achievementTitle) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View shareView = inflater.inflate(R.layout.share_dashboard_view, null);
        TextView lifetimeCO2 = shareView.findViewById(R.id.lifetimeCO2);
        TextView highestAchievement = shareView.findViewById(R.id.highestAchievement);

        lifetimeCO2.setText("Lifetime CO2 converted: " + String.format(Locale.getDefault(), "%.1f g CO₂", lifetime_co2_converted));
        highestAchievement.setText(achievementTitle);

        return shareView;
    }

    @SuppressLint("Range")
    private static void shareViewAsImage(Context context, View view) {
        // Measure + draw view to bitmap
        view.measure(
                View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "shared_dashboard.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

            if (contentUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, "Share dashboard via"));
            }
        } catch (IOException e) {
            Toast.makeText(context, "Error sharing screenshot", Toast.LENGTH_SHORT).show();
        }
    }
}
