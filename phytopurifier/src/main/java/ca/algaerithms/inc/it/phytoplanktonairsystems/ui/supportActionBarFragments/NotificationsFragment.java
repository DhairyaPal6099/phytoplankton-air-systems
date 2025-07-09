package ca.algaerithms.inc.it.phytoplanktonairsystems.ui.supportActionBarFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationAdapter;
import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationManager;
import ca.algaerithms.inc.it.phytoplanktonairsystems.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class NotificationsFragment extends Fragment {

    private NotificationAdapter adapter;
    private List<NotificationModel> notificationModelList = new ArrayList<>();

    public NotificationsFragment() {
        // Required empty public constructor
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

        NotificationManager.getInstance().getAllNotifications(fetchedList -> {
            notificationModelList.clear();
            notificationModelList.addAll(fetchedList);
            adapter.notifyDataSetChanged();
        });
        return view;
    }
}