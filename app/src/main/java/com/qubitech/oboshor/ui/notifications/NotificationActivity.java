package com.qubitech.oboshor.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.NotificationDataModel;
import com.qubitech.oboshor.ui.notifications.recyclerview.NotificationAdapter;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private NotificationsViewModel notificationsViewModel;
    private RecyclerView notificationRecycler;
    private ImageView closeBtn;
    private ProgressBar progressBar;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationDataModel> notificationDataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);

        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        closeBtn = findViewById(R.id.notifications_close);
        progressBar = findViewById(R.id.notifications_progress_bar);
        notificationDataModels = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        notificationRecycler = findViewById(R.id.notifications_recyclerview);
        notificationAdapter = new NotificationAdapter(this,notificationDataModels);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        notificationRecycler.setAdapter(notificationAdapter);

        notificationsViewModel.getNotifications().observe(this, new Observer<ArrayList<NotificationDataModel>>() {
            @Override
            public void onChanged(ArrayList<NotificationDataModel> notificationDataModels) {

                notificationAdapter.setNotificationDataModels(notificationDataModels);
                progressBar.setVisibility(View.GONE);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeBtn.setEnabled(false);
                finish();

            }
        });
    }
}