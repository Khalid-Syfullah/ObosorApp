package com.qubitech.oboshor.ui.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.ui.book.recyclerview.AllCategoriesAdapter;
import com.qubitech.oboshor.ui.dashboard.recyclerview.CategoriesAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoriesFragment extends Fragment {
    private View root;

    private TextView allCategoriesTitle;
    private ImageView closeBtn;
    private RecyclerView allCategoriesRecycler;
    private ProgressBar progressBar;
    private ArrayList<CategoryDataModel> allCategoryDataModels;
    private AllCategoriesAdapter allCategoriesAdapter;
    private String[] categoryNames={"Novel","Horror","Sci-Fi","Fantasy","Travel","Engineering","Science","History","Novel","Horror","Sci-Fi","Fantasy","Travel","Engineering","Science","History"};

    public static AllCategoriesFragment newInstance() {
        return new AllCategoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(root==null)
            root = inflater.inflate(R.layout.fragment_all_categories, container, false);

        allCategoriesTitle = root.findViewById(R.id.all_categories_title);
        closeBtn = root.findViewById(R.id.all_categories_back);
        allCategoriesRecycler = root.findViewById(R.id.all_categories_recycler_view);
        progressBar = root.findViewById(R.id.all_categories_progress_bar);
        allCategoryDataModels = new ArrayList<>();

        allCategoriesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        allCategoriesAdapter = new AllCategoriesAdapter(getActivity(), allCategoryDataModels, 1);
        allCategoriesRecycler.setAdapter(allCategoriesAdapter);
        allCategoriesRecycler.addItemDecoration(new DividerItemDecoration(allCategoriesRecycler.getContext(),DividerItemDecoration.VERTICAL));

        progressBar.setVisibility(View.VISIBLE);

        String category = getArguments().getString("title");

        if(category != null) {
            allCategoriesTitle.setText(category);

            String[] subcategory = getArguments().getStringArray("subcategory");

            if(subcategory != null) {

                for(int i=0;i<subcategory.length;i++) {
                    CategoryDataModel categoryDataModel = new CategoryDataModel(subcategory[i], subcategory, category);
                    allCategoryDataModels.add(categoryDataModel);
                }
            }
            allCategoriesAdapter = new AllCategoriesAdapter(getActivity(), allCategoryDataModels, 2);
            allCategoriesRecycler.setAdapter(allCategoriesAdapter);
            progressBar.setVisibility(View.GONE);

        }
        else {

            getCategories();
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigateUp();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void getCategories(){

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<CategoryDataModel>> categoryCall = restAPI.getAllCategory(StaticData.currentUserData.getValue().getToken());
        categoryCall.enqueue(new Callback<ArrayList<CategoryDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDataModel>> call, Response<ArrayList<CategoryDataModel>> response) {

                if(response.code() == 200){

                    allCategoryDataModels = response.body();
                    allCategoriesAdapter.setCategoryDataModels(allCategoryDataModels);
                    progressBar.setVisibility(View.GONE);

                    }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDataModel>> call, Throwable t) {

            }
        });
    }


}