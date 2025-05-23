package com.qubitech.oboshor.ui.book;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.BookQueryDataModel;
import com.qubitech.oboshor.datamodels.CategoryDataModel;
import com.qubitech.oboshor.datamodels.PublisherDataModel;
import com.qubitech.oboshor.datamodels.UserDataModel;
import com.qubitech.oboshor.ui.dashboard.recyclerview.BookAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookCategoryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<BookDataModel>> books = new MutableLiveData<>();
    private ArrayList<BookDataModel> bookDataModels = new ArrayList<>();
    public MutableLiveData<Integer> numOfPages = new MutableLiveData<>();
    public MutableLiveData<Boolean> currentProgress = new MutableLiveData<>();

    public BookCategoryViewModel() {

        books.setValue(bookDataModels);
        numOfPages.setValue(1);
        currentProgress.setValue(false);

    }

    public MutableLiveData<ArrayList<BookDataModel>> getBooks() {
        return books;
    }

    public LiveData<ArrayList<BookDataModel>> getAllBooks(){

        currentProgress.setValue(true);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<BookDataModel>> booksCall = restAPI.getAllBooks(StaticData.currentUserData.getValue().getToken());
        booksCall.enqueue(new Callback<ArrayList<BookDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BookDataModel>> call, Response<ArrayList<BookDataModel>> response) {

                if(response.code() == 200){

                    if(response.body() != null) {

                        currentProgress.setValue(false);
                        books.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookDataModel>> call, Throwable t) {

                currentProgress.setValue(false);

            }
        });


        return books;
    }

    public LiveData<ArrayList<BookDataModel>> getAllBooksByCategory(String categoryName, String subcategoryName, int pageNo) {

        currentProgress.setValue(true);

        bookDataModels = books.getValue();
        RestAPI restAPI = RetrofitClient.createRetrofitClient();

        if(subcategoryName != null){
            BookQueryDataModel bookQueryDataModel = new BookQueryDataModel(categoryName, subcategoryName, pageNo, 10);
            Call<BookQueryDataModel> booksCall = restAPI.getAllBooksBySubcategory(bookQueryDataModel);

            booksCall.enqueue(new Callback<BookQueryDataModel>() {
                @Override
                public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                    if (response.code() == 200) {

                        if(response.body() != null) {

                            numOfPages.setValue(response.body().getNumOfPages());

                            for(int i=0;i<response.body().getBooks().size();i++){

                                bookDataModels.add(response.body().getBooks().get(i));

                            }
                            currentProgress.setValue(false);

                            books.setValue(bookDataModels);
                        }

                    }
                }

                @Override
                public void onFailure(Call<BookQueryDataModel> call, Throwable t) {
                    currentProgress.setValue(false);

                }
            });


        }
        else{

            BookQueryDataModel bookQueryDataModel = new BookQueryDataModel(categoryName, pageNo, 10);
            Call<BookQueryDataModel> booksCall = restAPI.getAllBooksByCategory(bookQueryDataModel);

            booksCall.enqueue(new Callback<BookQueryDataModel>() {
                @Override
                public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                    if (response.code() == 200) {

                        if(response.body() != null) {

                            numOfPages.setValue(response.body().getNumOfPages());

                            for(int i=0;i<response.body().getBooks().size();i++){

                                bookDataModels.add(response.body().getBooks().get(i));

                            }
                            currentProgress.setValue(false);

                            books.setValue(bookDataModels);
                        }

                    }
                }

                @Override
                public void onFailure(Call<BookQueryDataModel> call, Throwable t) {
                    currentProgress.setValue(false);

                }
            });

        }



        return books;
    }

    public LiveData<ArrayList<BookDataModel>> getAllBooksByPublisher(String publisherName, int pageNo){

        currentProgress.setValue(true);

        bookDataModels = books.getValue();
        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel(pageNo, publisherName, 10);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.getAllBooksByPublisher(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    numOfPages.setValue(response.body().getNumOfPages());

                    if(response.body() != null) {

                        for(int i=0;i<response.body().getBooks().size();i++){

                            bookDataModels.add(response.body().getBooks().get(i));

                        }
                        currentProgress.setValue(false);

                        books.setValue(bookDataModels);
                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {
                currentProgress.setValue(false);

            }
        });


        return books;
    }

    public LiveData<ArrayList<BookDataModel>> getDashboardBooks(String type, int pageNo){

        currentProgress.setValue(true);

        bookDataModels = books.getValue();

        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel();
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall;

        if(type.equals("best-seller")) {
            bookQueryDataModel = new BookQueryDataModel("bestSeller",pageNo,10,0);
        }
        else if(type.equals("new-arrival")) {
            bookQueryDataModel = new BookQueryDataModel("newArrivals",pageNo,10,0);
        }
        else if(type.equals("on-sale")) {
            bookQueryDataModel = new BookQueryDataModel("onSell",pageNo,10,0);
        }
        else if(type.equals("pre-order")) {
            bookQueryDataModel = new BookQueryDataModel("preOrderList",pageNo,10,0);
        }
        else if(type.equals("trending")) {
            bookQueryDataModel = new BookQueryDataModel("trending",pageNo,10,0);
        }


        booksCall = restAPI.getDashboardViewAllBooks(bookQueryDataModel);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    numOfPages.setValue(response.body().getNumOfPages());

                    if(response.body() != null) {

                        for(int i=0;i<response.body().getBooks().size();i++){

                            bookDataModels.add(response.body().getBooks().get(i));

                        }
                        currentProgress.setValue(false);

                        books.setValue(bookDataModels);
                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {
                currentProgress.setValue(false);

            }
        });

        return books;
    }


    public LiveData<ArrayList<BookDataModel>> searchBooks(String search, int pageNo, boolean mode){

        currentProgress.setValue(true);

        BookQueryDataModel bookQueryDataModel = new BookQueryDataModel(pageNo, 10);
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<BookQueryDataModel> booksCall = restAPI.searchBooks(StaticData.currentUserData.getValue().getToken(), search, pageNo, 10);

        booksCall.enqueue(new Callback<BookQueryDataModel>() {
            @Override
            public void onResponse(Call<BookQueryDataModel> call, Response<BookQueryDataModel> response) {

                if (response.code() == 200) {

                    numOfPages.setValue(response.body().getNumOfPages());


                    if(mode){
                        bookDataModels = new ArrayList<>();
                        books.setValue(bookDataModels);
                    }
                    else{
                        bookDataModels = books.getValue();

                    }

                    if(response.body() != null) {

                        for(int i=0;i<response.body().getBooks().size();i++){

                            bookDataModels.add(response.body().getBooks().get(i));

                        }
                        currentProgress.setValue(false);

                        books.setValue(bookDataModels);
                    }

                }
            }

            @Override
            public void onFailure(Call<BookQueryDataModel> call, Throwable t) {
                currentProgress.setValue(false);

            }
        });


        return books;
    }

    public LiveData<ArrayList<BookDataModel>> searchBooksByAuthor(String author){

        currentProgress.setValue(true);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<BookDataModel>> searchBooksCall = restAPI.searchBooksByAuthor(StaticData.currentUserData.getValue().getToken(),author);
        searchBooksCall.enqueue(new Callback<ArrayList<BookDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BookDataModel>> call, Response<ArrayList<BookDataModel>> response) {

                if(response.code() == 200){
                    Log.d("Response","Code: "+response.code());

                    if(response.body() != null) {

                        for(int i=0;i<response.body().size();i++){
                            Log.d("ResponseBody",response.body().get(i).getTitle());
                        }
                        currentProgress.setValue(false);

                        books.setValue(response.body());
                    }
                    else{
                        Log.d("Response","Code: "+response.code());

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookDataModel>> call, Throwable t) {
                Log.d("Response","Failed!");
                currentProgress.setValue(false);

            }
        });


        return books;
    }

    public LiveData<ArrayList<BookDataModel>> searchBooksByPublisher(String publisher){

        currentProgress.setValue(true);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<BookDataModel>> searchBooksCall = restAPI.searchBooksByPublisher(StaticData.currentUserData.getValue().getToken(),publisher);
        searchBooksCall.enqueue(new Callback<ArrayList<BookDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BookDataModel>> call, Response<ArrayList<BookDataModel>> response) {

                if(response.code() == 200){
                    Log.d("Response","Code: "+response.code());

                    if(response.body() != null) {

                        for(int i=0;i<response.body().size();i++){
                            Log.d("ResponseBody",response.body().get(i).getTitle());
                        }
                        currentProgress.setValue(false);

                        books.setValue(response.body());
                    }
                    else{
                        Log.d("Response","Code: "+response.code());

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookDataModel>> call, Throwable t) {
                Log.d("Response","Failed!");
                currentProgress.setValue(false);

            }
        });


        return books;
    }

    public LiveData<ArrayList<BookDataModel>> searchBooksByGenre(String genre){

        currentProgress.setValue(true);

        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<BookDataModel>> searchBooksCall = restAPI.searchBooksByGenre(StaticData.currentUserData.getValue().getToken(),genre);
        searchBooksCall.enqueue(new Callback<ArrayList<BookDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BookDataModel>> call, Response<ArrayList<BookDataModel>> response) {

                if(response.code() == 200){
                    Log.d("Response","Code: "+response.code());

                    if(response.body() != null) {

                        for(int i=0;i<response.body().size();i++){
                            Log.d("ResponseBody",response.body().get(i).getTitle());
                        }
                        currentProgress.setValue(false);

                        books.setValue(response.body());
                    }
                    else{
                        Log.d("Response","Code: "+response.code());

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookDataModel>> call, Throwable t) {
                Log.d("Response","Failed!");
                currentProgress.setValue(false);

            }
        });


        return books;
    }



    public LiveData<ArrayList<BookDataModel>> filterBooks(String query) {

        currentProgress.setValue(true);

        ArrayList<BookDataModel> bookDataModels = new ArrayList<>();
        ArrayList<BookDataModel> bookDataModelsFiltered = books.getValue();
        bookDataModels.addAll(bookDataModelsFiltered);
        Log.d("Response", "BookDataModels : "+bookDataModels.size());
        Log.d("Response", "BookDataModelsFiltered : "+bookDataModelsFiltered.size());

        bookDataModelsFiltered = new ArrayList<>();
        if(query.isEmpty()){
            bookDataModelsFiltered.addAll(bookDataModels);

        }
        else{

            query = query.toLowerCase();


                for (BookDataModel bookDataModel : bookDataModels) {
                    if (bookDataModel.getTitle().toLowerCase().contains(query) || bookDataModel.getAuthor().toLowerCase().contains(query)
                    || bookDataModel.getPublisher().toLowerCase().contains(query) || bookDataModel.getCategory().toLowerCase().contains(query)) {
                        bookDataModelsFiltered.add(bookDataModel);
                    }
                }


            Log.d("Response", "BookDataModels : "+bookDataModels.size());
            Log.d("Response", "BookDataModelsFiltered : "+bookDataModelsFiltered.size());
        }

        MutableLiveData<ArrayList<BookDataModel>> booksFiltered = new MutableLiveData<>();
        booksFiltered.setValue(bookDataModelsFiltered);
        Log.d("Response", "Books.getValue() : "+booksFiltered.getValue().size());
        currentProgress.setValue(false);

        return booksFiltered;
    }


}