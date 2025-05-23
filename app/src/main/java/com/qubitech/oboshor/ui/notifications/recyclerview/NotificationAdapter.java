package com.qubitech.oboshor.ui.notifications.recyclerview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.NotificationDataModel;

import java.util.ArrayList;

class NotificationViewHolder extends RecyclerView.ViewHolder{

    TextView title,body;
    ImageView icon;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.recyclerview_notification_title);
        body = itemView.findViewById(R.id.recyclerview_notification_text);
        icon = itemView.findViewById(R.id.recyclerview_notification_icon);

    }

}

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    ArrayList<NotificationDataModel> notificationDataModels;
    Activity activity;

    public NotificationAdapter(Activity activity, ArrayList<NotificationDataModel> notificationDataModels) {

        this.activity = activity;
        this.notificationDataModels = notificationDataModels;

    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.recyclerview_notifications, parent, false);
        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(view);
        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        NotificationDataModel notificationDataModel = notificationDataModels.get(position);

        holder.title.setText(notificationDataModel.getTitle());
        holder.body.setText(notificationDataModel.getBody());


    }

    @Override
    public int getItemCount() {
        return notificationDataModels.size();
    }

    public void setNotificationDataModels(ArrayList<NotificationDataModel> notificationDataModels) {
        this.notificationDataModels = notificationDataModels;
        notifyDataSetChanged();
    }
}
