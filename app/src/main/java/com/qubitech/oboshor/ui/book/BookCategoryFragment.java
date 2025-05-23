package com.qubitech.oboshor.ui.book;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookCategoryFragment extends Fragment {

    private BookCategoryViewModel bookCategoryViewModel;
    private TextView bookCategoryTitle;
    private ImageView backBtn;
    private EditText searchView;
    private Button loadBtn;
    private RecyclerView bookCategoryRecycler;
    private ProgressBar progressBar;
    private ArrayList<BookDataModel> bookDataModels;
    private BookAdapter bookCategoryAdapter;
    private ConstraintLayout emptyBookConstraintLayout;
    private String arg="", searchString="";
    private int currentPage = 1;

    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(root==null)
            root = inflater.inflate(R.layout.fragment_book_category, container, false);
        bookCategoryViewModel = new ViewModelProvider(this).get(BookCategoryViewModel.class);


        bookCategoryTitle = root.findViewById(R.id.book_category_title);
        backBtn = root.findViewById(R.id.book_category_back);
        searchView = root.findViewById(R.id.book_category_search_view);
        progressBar = root.findViewById(R.id.book_category_progress_bar);
        bookCategoryRecycler = root.findViewById(R.id.book_category_recycler_view);
        emptyBookConstraintLayout = root.findViewById(R.id.empty_book_category_container);
        loadBtn = root.findViewById(R.id.book_category_load_btn);

        loadBtn.setVisibility(View.GONE);

        bookDataModels = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(2);
        bookCategoryRecycler.setLayoutManager(layoutManager);
        bookCategoryRecycler.setHasFixedSize(true);
        bookCategoryAdapter = new BookAdapter(getActivity(),bookDataModels,2);
        bookCategoryRecycler.setAdapter(bookCategoryAdapter);



        int id = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        if (searchEditText != null) {
            searchEditText.setGravity(Gravity.CENTER);
            searchEditText.setBackground(null);

        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                searchString = editable.toString();

                if(bookCategoryTitle.getText().equals("Search")) {

                    bookCategoryViewModel.searchBooks(editable.toString(),1, true).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
                        @Override
                        public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                            bookCategoryAdapter.setBookDataModels(bookDataModels);

                            if(bookCategoryViewModel.numOfPages.getValue() == 1){

                                loadBtn.setVisibility(View.GONE);
                            }

                            else if(currentPage >= bookCategoryViewModel.numOfPages.getValue()){
                                loadBtn.setVisibility(View.GONE);
                            }

                            else{
                                loadBtn.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                }
                else{

                    bookCategoryViewModel.filterBooks(editable.toString()).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
                        @Override
                        public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                            bookCategoryAdapter.setBookDataModels(bookDataModels);
                        }
                    });
                }
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StaticData.CURRENT_FRAGMENT = "book_category";

        Log.d("Fragment","Fragment: "+StaticData.CURRENT_FRAGMENT);
        Log.d("BookCategory","Arg: "+getArguments().getString("category"));

        Log.d("Fragment","Current Arguments: "+getArguments().getString("category"));
        if (getArguments().getString("category") != null) {

            arg = getArguments().getString("category");

            if (arg.equals("search")) {
                getAllBooks();
            }
            else if (arg.equals("all-categories")) {
                getFromAllCategories(currentPage);
            }
            else if (arg.equals("all-publishers")) {
                getFromAllPublishers(currentPage);
            }
            else if (arg.equals("best-seller")) {
                getDashboardBooks("Obosor Best Seller",arg, currentPage);
            }
            else if (arg.equals("new-arrival")) {
                getDashboardBooks("New Arrivals",arg, currentPage);
            }
            else if (arg.equals("on-sale")) {
                getDashboardBooks("Extra Discount",arg, currentPage);
            }
            else if (arg.equals("pre-order")) {
                getDashboardBooks("Pre-Order List",arg, currentPage);
            }
            else if (arg.equals("trending")) {
                getDashboardBooks("Trending",arg, currentPage);
            }
        }

        if(getArguments().getString("book_details") != null){

            arg = getArguments().getString("book_details");

            if (arg.equals("author")) {
                searchAuthorBooks(getArguments().getString("author"));
            }
            else if (arg.equals("publisher")) {
                searchPublisherBooks(getArguments().getString("publisher"));
            }
            else if (arg.equals("genre")) {
                searchGenreBooks(getArguments().getString("genre"));
            }
        }

        bookCategoryViewModel.currentProgress.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    progressBar.setVisibility(View.VISIBLE);

                }
                else{
                    progressBar.setVisibility(View.GONE);

                }
            }
        });

        bookCategoryViewModel.getBooks().observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPage++;

                Log.d("LoadBtn",arg);

                if (arg.equals("search")) {
                    bookCategoryViewModel.searchBooks(searchString,currentPage,false);
                }
                else if (arg.equals("all-categories")) {
                    getFromAllCategories(currentPage);
                }
                else if (arg.equals("all-publishers")) {
                    getFromAllPublishers(currentPage);
                }
                else if (arg.equals("best-seller")) {
                    getDashboardBooks("Obosor Best Seller",arg, currentPage);
                }
                else if (arg.equals("new-arrival")) {
                    getDashboardBooks("New Arrivals",arg, currentPage);
                }
                else if (arg.equals("on-sale")) {
                    getDashboardBooks("Extra Discount",arg, currentPage);
                }
                else if (arg.equals("pre-order")) {
                    getDashboardBooks("Pre-Order List",arg, currentPage);
                }
                else if (arg.equals("trending")) {
                    getDashboardBooks("Trending",arg, currentPage);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigateUp();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.getArguments().clear();

    }

    private void checkProgress(ArrayList<BookDataModel> bookDataModels){

        if(bookCategoryViewModel.currentProgress.getValue()){

            emptyBookConstraintLayout.setVisibility(View.GONE);

        }
        else{

            if(bookDataModels.size()==0){
                emptyBookConstraintLayout.setVisibility(View.VISIBLE);
            }
            else {
                emptyBookConstraintLayout.setVisibility(View.GONE);
            }

        }

    }



    private void getAllBooks(){

        bookCategoryTitle.setText("Search");

        bookCategoryViewModel.getDashboardBooks("best-seller",currentPage).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


            }
        });
    }

    private void getFromAllCategories(int pageNo){

        String categoryName = getArguments().getString("category-name");
        String subcategoryName = getArguments().getString("subcategory-name");

        Log.d("BookCategory","Category: "+categoryName+" Subcategory: "+subcategoryName);

        if(subcategoryName == null) {
            bookCategoryTitle.setText(categoryName);
        }
        else{
            bookCategoryTitle.setText(subcategoryName);

        }

        bookCategoryViewModel.getAllBooksByCategory(categoryName, subcategoryName, pageNo).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


                if(bookCategoryViewModel.numOfPages.getValue() == 1){

                    loadBtn.setVisibility(View.GONE);
                }

                else if(currentPage >= bookCategoryViewModel.numOfPages.getValue()){
                    loadBtn.setVisibility(View.GONE);
                }

                else{
                    loadBtn.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private void getFromAllPublishers(int pageNo){

        String publisherName = getArguments().getString("publisher-name");

        Log.d("BookCategory","Publisher: "+publisherName);

        bookCategoryTitle.setText(publisherName);

        bookCategoryViewModel.getAllBooksByPublisher(publisherName, pageNo).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


                if(bookCategoryViewModel.numOfPages.getValue() == 1){

                    loadBtn.setVisibility(View.GONE);
                }

                else if(currentPage >= bookCategoryViewModel.numOfPages.getValue()){
                    loadBtn.setVisibility(View.GONE);
                }

                else{
                    loadBtn.setVisibility(View.VISIBLE);

                }
            }
        });

    }


    private void getDashboardBooks(String title, String type, int pageNo){

        bookCategoryTitle.setText(title);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(2);
        bookCategoryRecycler.setLayoutManager(layoutManager);
        bookCategoryRecycler.setHasFixedSize(true);
        bookCategoryAdapter = new BookAdapter(getActivity(),bookDataModels,2);
        bookCategoryRecycler.setAdapter(bookCategoryAdapter);

        bookCategoryViewModel.getDashboardBooks(type, pageNo).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


                if(bookCategoryViewModel.numOfPages.getValue() == 1){

                    loadBtn.setVisibility(View.GONE);
                }

                else if(currentPage >= bookCategoryViewModel.numOfPages.getValue()){
                    loadBtn.setVisibility(View.GONE);
                }

                else{
                    loadBtn.setVisibility(View.VISIBLE);

                }


            }
        });

    }

    private void searchAuthorBooks(String author){


        bookCategoryTitle.setText(author);


        bookCategoryViewModel.searchBooksByAuthor(author).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {

            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


            }
        });
    }

    private void searchPublisherBooks(String publisher){

        bookCategoryTitle.setText(publisher);


        bookCategoryViewModel.searchBooksByPublisher(publisher).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);

            }
        });
    }

    private void searchGenreBooks(String genre){

        bookCategoryTitle.setText(genre);

        bookCategoryViewModel.searchBooksByGenre(genre).observe(getViewLifecycleOwner(), new Observer<ArrayList<BookDataModel>>() {
            @Override
            public void onChanged(ArrayList<BookDataModel> bookDataModels) {

                bookCategoryAdapter.setBookDataModels(bookDataModels);

                checkProgress(bookDataModels);


            }
        });
    }




}