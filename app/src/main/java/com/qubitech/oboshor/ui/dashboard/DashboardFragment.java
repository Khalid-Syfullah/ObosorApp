package com.qubitech.oboshor.ui.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.datamodels.SliderViewDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.CategoriesAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.PublishersAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.SliderAdapter;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class DashboardFragment extends Fragment {


    private View root=null;
    private DashboardViewModel dashboardViewModel;
    private SliderView sliderView;
    private TextView searchTextView;
    private RecyclerView categoriesRecycler, publishersRecycler, bestSellerRecycler, newArrivalRecycler, onSaleRecycler, preOrderRecycler, trendingRecycler;
    private TextView viewAllCategories, viewAllPublishers, viewAllBestSellers, viewAllNewArrivals, viewAllOnSale, viewAllPreOrders, viewAllTrending;

    private ArrayList<SliderViewDataModel> sliderViewDataModels;
    private ArrayList<CategoryDataModel> categoryDataModels;
    private ArrayList<PublisherDataModel> publishersDataModels;
    private ArrayList<BookDataModel> bestSellerDataModels, newArrivalDataModels, onSaleDataModels, preOrderDataModels, trendingDataModels;

    private SliderAdapter sliderAdapter;
    private CategoriesAdapter categoriesAdapter;
    private PublishersAdapter publishersAdapter;
    private BookAdapter bookAdapter, newArrivalAdapter, onSaleAdapter, preOrderAdapter, trendingAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashboardViewModel=new ViewModelProvider(this).get(DashboardViewModel.class);
        sliderViewDataModels = new ArrayList<>();
        categoryDataModels = new ArrayList<>();
        publishersDataModels = new ArrayList<>();
        bestSellerDataModels = new ArrayList<>();
        newArrivalDataModels = new ArrayList<>();
        onSaleDataModels = new ArrayList<>();
        preOrderDataModels = new ArrayList<>();
        trendingDataModels = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(root==null) {
            root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        }

        sliderView = root.findViewById(R.id.dashboard_imageslider);
        searchTextView = root.findViewById(R.id.dashboard_book_search_edittext);
        categoriesRecycler = root.findViewById(R.id.dashboard_categories_recyclerview);
        publishersRecycler = root.findViewById(R.id.dashboard_publishers_recyclerview);
        bestSellerRecycler = root.findViewById(R.id.dashboard_best_seller_recyclerview);
        newArrivalRecycler = root.findViewById(R.id.dashboard_new_arrivals_recyclerview);
        onSaleRecycler = root.findViewById(R.id.dashboard_on_sale_recyclerview);
        preOrderRecycler = root.findViewById(R.id.dashboard_pre_order_recyclerview);
        trendingRecycler = root.findViewById(R.id.dashboard_trending_recyclerview);
        viewAllCategories = root.findViewById(R.id.dashboard_categories_viewall);
        viewAllPublishers = root.findViewById(R.id.dashboard_publishers_viewall);
        viewAllBestSellers = root.findViewById(R.id.dashboard_best_seller_viewall);
        viewAllNewArrivals = root.findViewById(R.id.dashboard_new_arrivals_viewall);
        viewAllOnSale = root.findViewById(R.id.dashboard_on_sale_viewall);
        viewAllPreOrders = root.findViewById(R.id.dashboard_pre_order_viewall);
        viewAllTrending = root.findViewById(R.id.dashboard_trending_viewall);


        StaticData.CURRENT_FRAGMENT = "dashboard";
        Log.d("Fragment","Fragment: "+StaticData.CURRENT_FRAGMENT);

        sliderAdapter = new SliderAdapter(getActivity(), sliderViewDataModels);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new CategoriesAdapter(getActivity(), categoryDataModels,1);
        categoriesRecycler.setAdapter(categoriesAdapter);

        publishersRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        publishersAdapter = new PublishersAdapter(getActivity(),publishersDataModels);
        publishersRecycler.setAdapter(publishersAdapter);

        bestSellerRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        bookAdapter = new BookAdapter(getActivity(),bestSellerDataModels,1);
        bestSellerRecycler.setAdapter(bookAdapter);

        newArrivalRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        newArrivalAdapter = new BookAdapter(getActivity(),newArrivalDataModels,1);
        newArrivalRecycler.setAdapter(newArrivalAdapter);

        onSaleRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        onSaleAdapter = new BookAdapter(getActivity(),onSaleDataModels,1);
        onSaleRecycler.setAdapter(onSaleAdapter);

        preOrderRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        preOrderAdapter = new BookAdapter(getActivity(),preOrderDataModels,1);
        preOrderRecycler.setAdapter(preOrderAdapter);

        trendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
        trendingAdapter = new BookAdapter(getActivity(), trendingDataModels,1);
        trendingRecycler.setAdapter(trendingAdapter);

        dashboardViewModel.getSliderData().observe(getViewLifecycleOwner(), new Observer<ArrayList<SliderViewDataModel>>() {
            @Override
            public void onChanged(ArrayList<SliderViewDataModel> sliderViewDataModels) {

                sliderAdapter.setSliderViewDataModels(sliderViewDataModels);
            }
        });

        dashboardViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<ArrayList<CategoryDataModel>>() {
            @Override
            public void onChanged(ArrayList<CategoryDataModel> categoryDataModels) {

                categoriesAdapter.setCategoryDataModels(categoryDataModels);
            }
        });

        dashboardViewModel.getPublishers().observe(getViewLifecycleOwner(), new Observer<ArrayList<PublisherDataModel>>() {
            @Override
            public void onChanged(ArrayList<PublisherDataModel> publisherDataModels) {

                publishersAdapter.setPublisherDataModels(publisherDataModels);
            }
        });

        dashboardViewModel.getBestSellerBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {
                bookAdapter.setBookDataModels(bookDataModels);
            }
        });

        dashboardViewModel.getNewArrivalBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {
                newArrivalAdapter.setBookDataModels(bookDataModels);
            }
        });
        dashboardViewModel.getOnSaleBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {
                onSaleAdapter.setBookDataModels(bookDataModels);
            }
        });
        dashboardViewModel.getPreOrderBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {
                preOrderAdapter.setBookDataModels(bookDataModels);
            }
        });

        dashboardViewModel.getTrendingBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {
                trendingAdapter.setBookDataModels(bookDataModels);
            }
        });

        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "search");
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);

            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category",null);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_all_categories, bundle);
            }
        });

        viewAllPublishers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_all_publishers);
            }
        });

        viewAllBestSellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category","best-seller");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });

        viewAllNewArrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category","new-arrival");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });

        viewAllOnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category","on-sale");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });

        viewAllPreOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category","pre-order");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });

        viewAllTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category","trending");
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_dashboard_to_navigation_book_category, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}