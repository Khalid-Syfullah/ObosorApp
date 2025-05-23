package com.qubitech.oboshor.ui.notifications;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.qubitech.oboshor.MainActivity;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.NotificationDataModel;
import com.qubitech.oboshor.ui.dashboard.DashboardFragment;
import com.qubitech.oboshor.ui.notifications.recyclerview.NotificationAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private RecyclerView notificationRecycler;
    private ImageView closeBtn;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationDataModel> notificationDataModels;
    private boolean revealFlag = false;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        if(root==null)
            root = inflater.inflate(R.layout.fragment_notifications, container, false);


        closeBtn = root.findViewById(R.id.notifications_close);
        constraintLayout = root.findViewById(R.id.notifications_constraint_layout);
        progressBar = root.findViewById(R.id.notifications_progress_bar);
        notificationDataModels = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        notificationRecycler = root.findViewById(R.id.notifications_recyclerview);
        notificationAdapter = new NotificationAdapter(getActivity(),notificationDataModels);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        notificationRecycler.setAdapter(notificationAdapter);

        notificationsViewModel.getNotifications().observe(getViewLifecycleOwner(), new Observer<ArrayList<NotificationDataModel>>() {
            @Override
            public void onChanged(ArrayList<NotificationDataModel> notificationDataModels) {

                notificationAdapter.setNotificationDataModels(notificationDataModels);
                progressBar.setVisibility(View.GONE);
            }
        });


        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!revealFlag) {
                    revealFAB(root);
                    Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);
                    animation.setDuration(500);
                    closeBtn.setAnimation(animation);
                    revealFlag = true;
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeBtn.setEnabled(false);
                hideFAB(constraintLayout);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void revealFAB(View view) {

        int cx = view.getWidth() ;
        int cy = view.getHeight() ;
        float finalRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, 0, 0, finalRadius);
        anim.setDuration(500);

        anim.start();
    }
    private void hideFAB(View view) {

        int cx = view.getWidth();
        int cy = view.getHeight() ;
        float initialRadius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, 0, initialRadius, 0);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack("nt", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getSupportFragmentManager().beginTransaction().remove(NotificationsFragment.this).commit();

//                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigateUp();


            }
        });
        anim.start();
    }
}