package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.supportActionBarFragments;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter.NotificationAdapter;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationManagerPhytopurifier;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationModel;
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

        NotificationManagerPhytopurifier.getInstance(requireContext()).getAllNotifications(fetchedList -> {
            adapter.updateList(fetchedList);
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