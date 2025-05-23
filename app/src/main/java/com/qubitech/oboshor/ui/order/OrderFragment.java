package com.qubitech.oboshor.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.OrderDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;
import com.qubitech.oboshor.ui.order.recyclerview.OrderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private View root;
    private OrderViewModel orderViewModel;
    private TextView appBarTitle;
    private ImageView backBtn;
    private Button continueShoppingBtn;
    private RecyclerView orderRecycler;
    private ArrayList<OrderDataModel> orderDataModels;
    private ConstraintLayout emptyOrderConstraintLayout;
    private OrderAdapter orderAdapter;
    private ProgressBar progressBar;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root==null) {
            root = inflater.inflate(R.layout.fragment_order, container, false);
        }
        View includedAppBar = root.findViewById(R.id.include);
        includedAppBar.setVisibility(View.GONE);


        orderRecycler = root.findViewById(R.id.order_recyclerview);
        progressBar = root.findViewById(R.id.order_progress_bar);
        appBarTitle = root.findViewById(R.id.fragment_title);
        backBtn = root.findViewById(R.id.backBtn);
        continueShoppingBtn = root.findViewById(R.id.empty_order_continue_shopping_btn);
        emptyOrderConstraintLayout = root.findViewById(R.id.empty_order_container);

        appBarTitle.setText("Orderlist");
        orderDataModels = new ArrayList<>();

        orderAdapter = new OrderAdapter(getActivity(),orderDataModels);
        orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        orderRecycler.setAdapter(orderAdapter);

        progressBar.setVisibility(View.VISIBLE);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrdersForUser().observe(getViewLifecycleOwner(), new Observer<ArrayList<OrderDataModel>>() {
            @Override
            public void onChanged(ArrayList<OrderDataModel> orderDataModels2) {

                orderAdapter.setOrderDataModels(orderDataModels2);
                progressBar.setVisibility(View.GONE);

                Log.d("OrderDataModel","Size: "+orderDataModels2.size());

                if(orderDataModels2.size()==0){
                    emptyOrderConstraintLayout.setVisibility(View.VISIBLE);
                    orderRecycler.setVisibility(View.GONE);
                }
                else {
                    emptyOrderConstraintLayout.setVisibility(View.GONE);
                    orderRecycler.setVisibility(View.VISIBLE);
                }

            }
        });




        //getOrdersForParticularUser();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigateUp();
            }
        });
    }

    private void getOrdersForParticularUser(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call getOrdersCall = restAPI.getOrdersForUser(StaticData.currentUserData.getValue().getToken(),StaticData.currentUserData.getValue().getCurrentUser().getId());

        getOrdersCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.code() == 200){

                    orderDataModels = (ArrayList<OrderDataModel>) response.body();

                    OrderAdapter orderAdapter = new OrderAdapter(getActivity(), orderDataModels);

                    orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                    orderRecycler.setAdapter(orderAdapter);

                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}