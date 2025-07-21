package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.supportActionBarFragments;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationAdapter;
import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class NotificationsFragment extends Fragment {

    private NotificationAdapter adapter;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private List<NotificationModel> notificationModelList = new ArrayList<>();

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getContext(), "Notifications permissions granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Notifications permissions denied!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationModelList);
        recyclerView.setAdapter(adapter);

        // === TEMP: Add dummy notifications for local testing ===
        //List<NotificationModel> dummyList = new ArrayList<>();

        //long now = System.currentTimeMillis();
       // dummyList.add(new NotificationModel("EOD Stat", "Algae level: Healthy", new Date(now - 3600_000))); // 1 hour ago
       // dummyList.add(new NotificationModel("Water Alert", "Water level is low", new Date(now - 7200_000))); // 2 hours ago
       // dummyList.add(new NotificationModel("Critical Alert", "Algae oxygen production dropping", new Date(now - 10_000))); // just now

        //adapter.updateList(dummyList);


        NotificationManagerPhytopurifier.getInstance(requireContext()).getAllNotifications(fetchedList -> {
            adapter.updateList(fetchedList);
        });

        Button clearButton = view.findViewById(R.id.clearAllButton);
        clearButton.setOnClickListener(v -> {
            // 1. Clear from the local list
            notificationModelList.clear();
            adapter.notifyDataSetChanged();

            // 2. Clear system notifications (optional)
            androidx.core.app.NotificationManagerCompat manager = androidx.core.app.NotificationManagerCompat.from(requireContext());
            manager.cancelAll();

            // 3. Feedback
            Toast.makeText(getContext(), "Notifications cleared", Toast.LENGTH_SHORT).show();
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        return view;
    }
}