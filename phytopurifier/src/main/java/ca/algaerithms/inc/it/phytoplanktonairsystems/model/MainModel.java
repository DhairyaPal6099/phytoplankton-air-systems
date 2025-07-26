package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class MainModel {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private final Context context;

    public MainModel(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void fetchUserName(String uid, OnNameLoaded callback) {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    String name = document.getString("name");
                    callback.onNameLoaded(name);
                })
                .addOnFailureListener(e -> callback.onNameLoaded(null));
    }

    public void logout(OnLogoutCallback callback) {
        auth.signOut();
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("triggered_worker").apply();
        callback.onLogoutComplete();
    }

    public Uri captureAndSaveDashboard(View view) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "dashboard_screenshot.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } catch (IOException e) {
            Toast.makeText(context, R.string.error_sharing_screenshot, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public interface OnNameLoaded {
        void onNameLoaded(String name);
    }

    public interface OnLogoutCallback {
        void onLogoutComplete();
    }
}
