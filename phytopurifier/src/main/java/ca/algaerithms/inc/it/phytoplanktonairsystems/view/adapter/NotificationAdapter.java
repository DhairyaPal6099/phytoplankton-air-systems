package ca.algaerithms.inc.it.phytoplanktonairsystems.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.algaerithms.inc.it.phytoplanktonairsystems.model.NotificationModel;
import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModelList.get(position);
        holder.title.setText(notificationModel.getTitle());
        holder.message.setText(notificationModel.getMessage());
        holder.timestamp.setText(notificationModel.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            message = itemView.findViewById(R.id.notificationMessage);
            timestamp = itemView.findViewById(R.id.notificationTimestamp);
        }
    }

    public void updateList(List<NotificationModel> newList) {
        this.notificationModelList = new ArrayList<>(newList); // Defensive copy
        notifyDataSetChanged();
    }
}
