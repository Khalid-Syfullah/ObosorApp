package com.qubitech.oboshor.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.ui.book.recyclerview.AllPublishersAdapter;
import com.qubitech.oboshor.ui.dashboard.DashboardViewModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.CategoriesAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPublishersFragment extends Fragment {
    private View root;

    private RecyclerView allPublishersRecycler;
    private AllPublishersAdapter allPublishersAdapter;
    private ProgressBar progressBar;
    private ArrayList<PublisherDataModel> allPublishersDataModels;
    private DashboardViewModel publisherViewModel;
    private String[] publisherNames={"Walker","Prothoma","Bangla","Alrahellah"};

    public static AllPublishersFragment newInstance() {
        return new AllPublishersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(root==null)
            root = inflater.inflate(R.layout.fragment_all_publishers, container, false);

        allPublishersRecycler = root.findViewById(R.id.all_publishers_recycler_view);
        progressBar = root.findViewById(R.id.all_publishers_progress_bar);

        allPublishersDataModels = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        allPublishersRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        allPublishersAdapter = new AllPublishersAdapter(getActivity(), allPublishersDataModels);
        allPublishersRecycler.setAdapter(allPublishersAdapter);

        publisherViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        publisherViewModel.getPublishers().observe(getViewLifecycleOwner(), new Observer<ArrayList<PublisherDataModel>>() {
            @Override
            public void onChanged(ArrayList<PublisherDataModel> publisherDataModels) {

                allPublishersAdapter.setPublisherDataModels(publisherDataModels);
                progressBar.setVisibility(View.GONE);

            }
        });

        //getPublishers();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void getPublishers(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<PublisherDataModel>> publishersCall = restAPI.getAllPublishers(StaticData.currentUserData.getValue().getToken());
        publishersCall.enqueue(new Callback<ArrayList<PublisherDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PublisherDataModel>> call, Response<ArrayList<PublisherDataModel>> response) {

                if(response.code() == 200){

                    allPublishersDataModels = response.body();

                    allPublishersRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    allPublishersAdapter = new AllPublishersAdapter(getActivity(), allPublishersDataModels);
                    allPublishersRecycler.setAdapter(allPublishersAdapter);
                    progressBar.setVisibility(View.GONE);


                }
            }

            @Override
            public void onFailure(Call<ArrayList<PublisherDataModel>> call, Throwable t) {

            }
        });
    }


}