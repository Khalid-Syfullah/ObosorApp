package com.qubitech.oboshor.ui.book;

import static com.qubitech.oboshor.StaticData.CURRENT_BOOK_ID;
import static com.qubitech.oboshor.StaticData.currentUserData;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.common.util.ArrayUtils;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.StaticData;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.qubitech.oboshor.datamodels.ReviewDataModel;
import com.qubitech.oboshor.datamodels.WishListDataModel;
import com.qubitech.oboshor.ui.cart.CartViewModel;
import com.qubitech.oboshor.ui.wishlist.WishlistViewModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsFragment extends Fragment {

    private BookDetailsViewModel bookDetailsViewModel;
    private Button specificationBtn, reviewBtn;
    private ConstraintLayout bookDetailsConstraintLayout, reviewConstraintLayout;
    private LinearLayout reviewRecycler;
    private TextView tvIncrement,tvDecrement,tvTitle,tvOldAmount,tvNewAmount,tvOfferAmount,tvQuantity,tvDescription,tvYear,tvCategory,tvPublisher,
            tvGenre,tvPages,tvEdition,tvPrice,tvAvailableQuantity,tvAuthor;
    private ImageView bookImage, backBtn;
    private EditText feelingsText, reviewText;
    private Button submitBtn, cancelBtn;
    private RatingBar ratingBar;
    private AlertDialog alertDialog;
    private TextView addToCart;
    private TextView addToWishList;
    private CartViewModel cartViewModel;
    private WishlistViewModel wishlistViewModel;
    private BookDataModel bookDetails;
    private ArrayList<ReviewDataModel> reviewDataModels;
    private View root;
    private int newAmount = 0;

    private Map<String, ReviewDataModel> reviewModelMap;

    private boolean buttonFlag = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(root==null)
            root = inflater.inflate(R.layout.fragment_book_details,container,false);

        specificationBtn = root.findViewById(R.id.book_details_specifications);
        reviewBtn = root.findViewById(R.id.book_details_reviews);
        bookDetailsConstraintLayout = root.findViewById(R.id.book_details_constraint_layout3);
        reviewConstraintLayout = root.findViewById(R.id.book_details_review_constraint_layout);
        reviewRecycler = root.findViewById(R.id.book_details_reviews_linear_layout);
        tvTitle= root.findViewById(R.id.book_details_title);
        tvOldAmount= root.findViewById(R.id.book_details_old_amount);
        tvNewAmount= root.findViewById(R.id.book_details_amount);
        tvOfferAmount= root.findViewById(R.id.book_details_offer_amount);
        tvIncrement = root.findViewById(R.id.book_details_quantity_increase);
        tvDecrement = root.findViewById(R.id.book_details_quantity_decrease);
        tvQuantity= root.findViewById(R.id.book_details_quantity);
        tvDescription= root.findViewById(R.id.book_details_description);
        tvYear= root.findViewById(R.id.book_details_year);
        tvCategory= root.findViewById(R.id.book_details_category);
        tvPublisher= root.findViewById(R.id.book_details_publisher);
        tvGenre= root.findViewById(R.id.book_details_genre);
        tvPages= root.findViewById(R.id.book_details_total_pages);
        tvEdition= root.findViewById(R.id.book_details_edition);
        tvPrice= root.findViewById(R.id.book_details_price);
        tvAvailableQuantity= root.findViewById(R.id.book_details_available_quantity);
        bookImage = root.findViewById(R.id.book_details_image);
        tvAuthor = root.findViewById(R.id.book_details_author);
        addToCart = root.findViewById(R.id.book_details_cart_cardview);
        addToWishList = root.findViewById(R.id.book_details_wishlist_cardview);
        backBtn = root.findViewById(R.id.book_details_back);

        bookDetailsViewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        reviewModelMap = new HashMap<>(0);

        reviewDataModels = new ArrayList<>();

//        reviewAdapter = new ReviewAdapter( reviewDataModels);
//        reviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//        reviewRecycler.setAdapter(reviewAdapter);

        bookDetails = (BookDataModel) getArguments().getSerializable("book_details");

        reviewDataModels = bookDetails.getReviews();
        StaticData.CURRENT_BOOK_ID = bookDetails.get_id();
        StaticData.CURRENT_FRAGMENT = "";

        tvOldAmount.setVisibility(View.GONE);

        newAmount = bookDetails.getPrice();

        if(bookDetails.getDiscount() != 0){

            tvOldAmount.setVisibility(View.VISIBLE);
            tvOldAmount.setPaintFlags(tvOldAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            newAmount = bookDetails.getPrice() - (bookDetails.getPrice()*bookDetails.getDiscount())/100;

        }

        tvTitle.setText(bookDetails.getTitle());
        tvOldAmount.setText(Integer.toString(bookDetails.getPrice())+" tk");
        tvOfferAmount.setText(Integer.toString(bookDetails.getDiscount())+"% off");
        tvNewAmount.setText(Integer.toString(newAmount)+" tk");
        tvCategory.setText(bookDetails.getCategory());
        tvAuthor.setText(bookDetails.getAuthor());
        tvAvailableQuantity.setText(Integer.toString(bookDetails.getAvailable()));
        tvDescription.setText(bookDetails.getDescription());
        tvPrice.setText(Integer.toString(bookDetails.getPrice()));
        tvGenre.setText(bookDetails.getGenre());
        tvPublisher.setText(bookDetails.getPublisher());
        tvEdition.setText(bookDetails.getEdition());
        tvPages.setText(Integer.toString(bookDetails.getPages()));
        tvYear.setText(Integer.toString(bookDetails.getYear()));
        Picasso.get().load(RetrofitClient.BASE_URL +"/"+bookDetails.getImage()).into(bookImage);

        reviewRecycler.setVisibility(View.GONE);

        bookDetailsViewModel.fetchReviews().observe(getViewLifecycleOwner(), new Observer<ArrayList<ReviewDataModel>>() {
            @Override
            public void onChanged(ArrayList<ReviewDataModel> reviewDataModels) {
                Log.d("XXX", "Data changed: "+ reviewDataModels.size()+"  --  "+reviewRecycler.getChildCount());
                reviewRecycler.removeAllViews();
                for(int i=0;i<reviewDataModels.size(); ++i){
                    ReviewDataModel currentModel = reviewDataModels.get(i);
                    reviewModelMap.put(currentModel.get_id(), currentModel);
                }
                for(Map.Entry<String, ReviewDataModel> entry : reviewModelMap.entrySet()){
                    reviewRecycler.addView(
                            getReviewModelView(
                                    getActivity(),
                                    entry.getValue()
                            )
                    );
                }
            }
        });




//        final TextView textView = root.findViewById(R.id.text_home);
//        bookDetailsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvQuantity.setText(String.valueOf(Integer.parseInt(tvQuantity.getText().toString())+1));

            }
        });

        tvDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tvQuantity.getText().toString().equals("1")){

                    tvQuantity.setText(String.valueOf(Integer.parseInt(tvQuantity.getText().toString())-1));

                }
            }
        });

        specificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonFlag){

                    buttonFlag = false;

                    specificationBtn.setBackgroundColor(getResources().getColor(R.color.oboshor));
                    specificationBtn.setTextColor(getResources().getColor(R.color.white));
                    reviewBtn.setBackgroundColor(getResources().getColor(R.color.white));
                    reviewBtn.setTextColor(getResources().getColor(R.color.ash));

                    bookDetailsConstraintLayout.setVisibility(View.VISIBLE);
                    reviewConstraintLayout.setVisibility(View.GONE);
                    reviewRecycler.setVisibility(View.GONE);

                    specificationBtn.setStateListAnimator(null);
                    reviewBtn.setStateListAnimator(null);

                }

            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonFlag){

                    buttonFlag = true;

                    specificationBtn.setBackgroundColor(getResources().getColor(R.color.white));
                    specificationBtn.setTextColor(getResources().getColor(R.color.ash));
                    reviewBtn.setBackgroundColor(getResources().getColor(R.color.oboshor));
                    reviewBtn.setTextColor(getResources().getColor(R.color.white));

                    bookDetailsConstraintLayout.setVisibility(View.GONE);
                    reviewConstraintLayout.setVisibility(View.VISIBLE);
                    reviewRecycler.setVisibility(View.VISIBLE);

                    specificationBtn.setStateListAnimator(null);
                    reviewBtn.setStateListAnimator(null);

                }

            }
        });

        reviewConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewAlertDialog();
            }
        });

        tvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("book_details","author");
                bundle.putString("author",bookDetails.getAuthor());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_book_details_to_navigation_book_category,bundle);

            }
        });

        tvPublisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("book_details","publisher");
                bundle.putString("publisher",bookDetails.getPublisher());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_book_details_to_navigation_book_category,bundle);

            }
        });

        tvGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("book_details","genre");
                bundle.putString("genre",bookDetails.getGenre());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_book_details_to_navigation_book_category,bundle);

            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "all-categories");
                bundle.putString("category-name", bookDetails.getCategory());
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_book_details_to_navigation_book_category,bundle);

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDataModel cartDataModel = new CartDataModel(Integer.parseInt(tvQuantity.getText().toString()),bookDetails.get_id(),bookDetails.getTitle(),bookDetails.getAuthor(),bookDetails.getCategory(),newAmount,bookDetails.getImage());
                cartViewModel.insert(cartDataModel);


            }
        });
        addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishListDataModel wishListDataModel=new WishListDataModel(bookDetails.get_id(), bookDetails.getTitle(),bookDetails.getAuthor(), bookDetails.getCategory(),bookDetails.getPrice(),bookDetails.getImage());
                wishlistViewModel.insert(wishListDataModel);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigateUp();
            }
        });



    }


    private View getReviewModelView(Context context, ReviewDataModel reviewDataModel) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.recyclerview_review,
                null,
                false
        );
        TextView userName, commentTitle, commentBody, commentTime, like, dislike;
        ImageView userImage, likeImage, dislikeImage;
        RatingBar ratingBar;

        userImage = view.findViewById(R.id.recyclerview_review_user_image);
        userName = view.findViewById(R.id.recyclerview_review_user_name);
        commentTitle = view.findViewById(R.id.recyclerview_review_comment_title);
        commentBody = view.findViewById(R.id.recyclerview_review_comment_body);
        commentTime = view.findViewById(R.id.recyclerview_review_comment_time);
        like = view.findViewById(R.id.recyclerview_review_like);
        dislike = view.findViewById(R.id.recyclerview_review_dislike);
        likeImage = view.findViewById(R.id.recyclerview_review_like_image);
        dislikeImage = view.findViewById(R.id.recyclerview_review_dislike_image);
        ratingBar = view.findViewById(R.id.recyclerview_review_rating_bar);

        userName.setText(reviewDataModel.getName());
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.parseColor("#52341E"))
                .endConfig()
                .buildRound(reviewDataModel.getName().toUpperCase().substring(0, 1), Color.parseColor("#FFFFFF"));
        userImage.setImageDrawable(drawable);

        commentTitle.setText(reviewDataModel.getCommentTitle());
        commentBody.setText(reviewDataModel.getCommentBody());
        commentTime.setText(reviewDataModel.getCommentTime().split("T")[0]);

        like.setText(String.valueOf(reviewDataModel.getLikes().length));
        dislike.setText(String.valueOf(reviewDataModel.getDislikes().length));
        ratingBar.setRating(reviewDataModel.getRating());
//      Like Dislike icon setup

        if(hasAlreadyLiked(reviewDataModel)){
            like.setTextColor(context.getResources().getColor(R.color.yellowish));
            like.setTypeface(like.getTypeface(), Typeface.BOLD);
            likeImage.setImageResource(R.drawable.ic_liked);
            dislike.setTextColor(context.getResources().getColor(R.color.black));
            dislike.setTypeface(dislike.getTypeface(), Typeface.NORMAL);
            dislikeImage.setImageResource(R.drawable.dislike);

        }
        else if(hasAlreadyDisliked(reviewDataModel)){
            dislike.setTextColor(context.getResources().getColor(R.color.yellowish));
            dislike.setTypeface(dislike.getTypeface(), Typeface.BOLD);
            dislikeImage.setImageResource(R.drawable.ic_disliked);
            like.setTextColor(context.getResources().getColor(R.color.black));
            like.setTypeface(like.getTypeface(), Typeface.NORMAL);
            likeImage.setImageResource(R.drawable.like);
        }
        else {
            dislike.setTextColor(context.getResources().getColor(R.color.black));
            dislike.setTypeface(like.getTypeface(), Typeface.NORMAL);
            dislikeImage.setImageResource(R.drawable.dislike);
            like.setTextColor(context.getResources().getColor(R.color.black));
            like.setTypeface(like.getTypeface(), Typeface.NORMAL);
            likeImage.setImageResource(R.drawable.like);
        }


        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeImage.setEnabled(false);
                dislikeImage.setEnabled(false);

                ReviewDataModel reviewDataModel1 = new ReviewDataModel(reviewDataModel.get_id(), StaticData.currentUserData.getValue().getCurrentUser().getId());
                RestAPI restAPI = RetrofitClient.createRetrofitClient();
                if(hasAlreadyLiked(reviewDataModel)){
                    like.setText(String.valueOf(Integer.valueOf(like.getText().toString())-1));

                }
                else if(hasAlreadyDisliked(reviewDataModel)){
                    like.setText(String.valueOf(Integer.valueOf(like.getText().toString())+1));
                    dislike.setText(String.valueOf(Integer.valueOf(dislike.getText().toString())-1));
                }
                else {
                    like.setText(String.valueOf(Integer.valueOf(like.getText().toString())+1));
                }

                Log.d("Like", "ReviewID: " + reviewDataModel.get_id() + " UserID: " + StaticData.currentUserData.getValue().getCurrentUser().getId());

                Call<ArrayList<ReviewDataModel>> likeCall = restAPI.updateLike(currentUserData.getValue().getToken(), reviewDataModel1);
                likeCall.enqueue(new Callback<ArrayList<ReviewDataModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReviewDataModel>> call, Response<ArrayList<ReviewDataModel>> response) {
                        if (response.code() == 200) {
                            likeImage.setEnabled(true);
                            dislikeImage.setEnabled(true);

                            getReviews();

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReviewDataModel>> call, Throwable t) {
                        Log.d("ReviewAdapter", "Response failure");
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        likeImage.setEnabled(true);
                        dislikeImage.setEnabled(true);

                        like.setText(String.valueOf(reviewDataModel.getLikes().length));
                        dislike.setText(String.valueOf(reviewDataModel.getDislikes().length));


                    }
                });
            }
        });

        dislikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeImage.setEnabled(false);
                dislikeImage.setEnabled(false);

                ReviewDataModel reviewDataModel1 = new ReviewDataModel(reviewDataModel.get_id(), StaticData.currentUserData.getValue().getCurrentUser().getId());

                RestAPI restAPI = RetrofitClient.createRetrofitClient();

                Log.d("Dislike", "ReviewID: " + reviewDataModel.get_id() + " UserID: " + StaticData.currentUserData.getValue().getCurrentUser().getId());

                if(hasAlreadyLiked(reviewDataModel)){
                    like.setText(String.valueOf(Integer.valueOf(like.getText().toString())-1));
                    dislike.setText(String.valueOf(Integer.valueOf(dislike.getText().toString())+1));
                }
                else if(hasAlreadyDisliked(reviewDataModel)){
                    dislike.setText(String.valueOf(Integer.valueOf(dislike.getText().toString())-1));
                }
                else {
                    dislike.setText(String.valueOf(Integer.valueOf(dislike.getText().toString())+1));
                }
                Call<ArrayList<ReviewDataModel>> dislikeCall = restAPI.updateDislike(currentUserData.getValue().getToken(), reviewDataModel1);
                dislikeCall.enqueue(new Callback<ArrayList<ReviewDataModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReviewDataModel>> call, Response<ArrayList<ReviewDataModel>> response) {

                        if (response.code() == 200) {
                            likeImage.setEnabled(true);
                            dislikeImage.setEnabled(true);
                            getReviews();

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReviewDataModel>> call, Throwable t) {
                        Log.d("ReviewAdapter", "Response failure");
                        likeImage.setEnabled(true);
                        dislikeImage.setEnabled(true);
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        like.setText(String.valueOf(reviewDataModel.getLikes().length));
                        dislike.setText(String.valueOf(reviewDataModel.getDislikes().length));
                    }
                });

            }
        });

        return  view;
    }

    private boolean hasAlreadyDisliked(ReviewDataModel reviewDataModel) {

        return ArrayUtils.contains(reviewDataModel.getDislikes(), currentUserData.getValue().getCurrentUser().getId());
    }

    private boolean hasAlreadyLiked(ReviewDataModel reviewDataModel) {
        return ArrayUtils.contains(reviewDataModel.getLikes(), currentUserData.getValue().getCurrentUser().getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        CURRENT_BOOK_ID = "";

    }

    private void reviewAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomAlertDialog);
        View dialog = LayoutInflater.from(getContext()).inflate(R.layout.alert_write_review,null);
        builder.setView(dialog);
        alertDialog = builder.create();
        alertDialog.show();

        feelingsText = dialog.findViewById(R.id.review_alert_your_feelings);
        reviewText = dialog.findViewById(R.id.review_alert_your_review);
        ratingBar = dialog.findViewById(R.id.review_alert_rating_bar);
        submitBtn = dialog.findViewById(R.id.review_alert_submit_btn);
        cancelBtn = dialog.findViewById(R.id.review_alert_cancel_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyReview();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });


    }

    private void getReviews() {


        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call<ArrayList<ReviewDataModel>> getReviewCall = restAPI.getReviews(currentUserData.getValue().getToken());
        getReviewCall.enqueue(new Callback<ArrayList<ReviewDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ReviewDataModel>> call, Response<ArrayList<ReviewDataModel>> response) {

                if (response.code() == 200) {

                    ArrayList<ReviewDataModel> reviewDataModelsFetch = (ArrayList<ReviewDataModel>) response.body();
                    ArrayList<ReviewDataModel> reviewDataModels = new ArrayList<>();

                    for(int i=0;i<reviewDataModelsFetch.size();i++){

                        ReviewDataModel reviewDataModel = reviewDataModelsFetch.get(i);

                        if(reviewDataModel.getBookId().equals(CURRENT_BOOK_ID)) {

                            reviewDataModel.setLike(reviewDataModel.getLikes().length);
                            reviewDataModel.setDislike(reviewDataModel.getDislikes().length);

                            reviewDataModels.add(reviewDataModel);


                        }

                    }

                    bookDetailsViewModel.setReviews(reviewDataModels);


                } else {
                    Log.d("ReviewAdapter", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReviewDataModel>> call, Throwable t) {
                Log.d("ReviewAdapter", "Response failed");
            }
        });
    }


//    private void getReviews(){
//
//        reviewDataModels = new ArrayList<>();
//
//        RestAPI restAPI = RetrofitClient.createRetrofitClient();
//        Call<ArrayList<ReviewDataModel>> getReviewCall = restAPI.getReviews(currentUserData.getValue().getToken());
//        getReviewCall.enqueue(new Callback<ArrayList<ReviewDataModel>>() {
//            @Override
//            public void onResponse(Call<ArrayList<ReviewDataModel>> call, Response<ArrayList<ReviewDataModel>> response) {
//
//                if(response.code() == 200){
//
//
//                    ArrayList<ReviewDataModel> reviewDataModelsFetch =  response.body();
//
//
//                    for(int i=0;i<reviewDataModelsFetch.size();i++){
//
//                        ReviewDataModel reviewDataModel = reviewDataModelsFetch.get(i);
//
//                        if(reviewDataModel.getBookId().equals(CURRENT_BOOK_ID)) {
//
//                            reviewDataModel.setLike(reviewDataModel.getLikes().length);
//                            reviewDataModel.setDislike(reviewDataModel.getDislikes().length);
//
//                            reviewDataModels.add(reviewDataModel);
//                        }
//
//                    }
//
//                    reviewAdapter = new ReviewAdapter( reviewDataModels);
//                    reviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//                    reviewRecycler.setAdapter(reviewAdapter);
//                    Log.d("Review","Response received: "+reviewDataModels.size());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Log.d("Review","Response failed");
//
//            }
//        });
//    }

    private void verifyReview(){

        String feelings="", review="", username="", time="";
        int like=0, dislike=0;
        float rating=0;

        username = StaticData.currentUserData.getValue().getCurrentUser().getUsername();
        if(feelingsText.getText().toString().isEmpty()){
            feelingsText.setError("Feelings Required!");
            feelingsText.requestFocus();
        }
        else {
            feelings = feelingsText.getText().toString();
        }

        if(reviewText.getText().toString().isEmpty()){
            reviewText.setError("Review Required!");
            reviewText.requestFocus();
        }
        else {
            review = reviewText.getText().toString();
        }

        rating = ratingBar.getRating();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        time = sdf.format(Calendar.getInstance().getTime());
        Log.d("Review","Time: "+time+" Rating: "+rating);
        ReviewDataModel reviewDataModel = new ReviewDataModel(CURRENT_BOOK_ID,currentUserData.getValue().getCurrentUser().getId(),username,feelings,review,rating);

        if(!feelings.isEmpty() && !review.isEmpty()){
            uploadReview(reviewDataModel);
        }

    }

    private void uploadReview(ReviewDataModel reviewDataModel){
        RestAPI restAPI = RetrofitClient.createRetrofitClient();
        Call uploadReviewCall = restAPI.uploadNewReview(StaticData.currentUserData.getValue().getToken(),reviewDataModel);

        uploadReviewCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if(response.code() == 200){

                    alertDialog.dismiss();

                    getReviews();


                    //Toast.makeText(getActivity(),response.code()+": "+response.message(),Toast.LENGTH_SHORT).show();
                    Log.d("Review",response.code()+": "+response.message());
                }
                else {
                    //Toast.makeText(getActivity(),response.code()+": "+response.message(),Toast.LENGTH_SHORT).show();
                    Log.d("Review",response.code()+": "+response.message());

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

                //Toast.makeText(getActivity(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("Review","Error: "+t.getMessage());

            }
        });

    }


//    private class reviewTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            reviewDataModels = new ArrayList<>();
//
//
//            //API call for Dashboard Activity
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(BASE_URL+"/api/user/review")
//                    .build();
//
//            try {
//                okhttp3.Response response = client.newCall(request).execute();
//
//
//                //Getting Today's times from API
//                if(response.body() != null){
//
//                    JSONObject jsonObject = new JSONObject(response.body().string());
//                    JSONArray jsonArray = jsonObject.getJSONArray("reviews");
//
//                    for(int i=0;i<jsonArray.length();i++){
//
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                        String currentId = jsonObject1.getString("bookId");
//
//                        if(bookDetails.get_id().equals(currentId)){
//
//                            String _id = jsonObject1.getString("_id");
//                            String name = jsonObject1.getString("username");
//                            String image = jsonObject1.getString("username");
//                            String commentTitle = jsonObject1.getString("title");
//                            String commentBody = jsonObject1.getString("description");
//                            String commentTime = jsonObject1.getString("time");
//                            float rating = jsonObject1.getInt("star");
//
//                            JSONArray jsonArray1 = jsonObject1.getJSONArray("like");
//                            JSONArray jsonArray2 = jsonObject1.getJSONArray("dislike");
//
//                            int like = jsonArray1.length();
//                            int dislike = jsonArray2.length();
//
//                            Log.d("Reviews",i+": "+name+image+commentTitle+commentBody+commentTime+"Rating: "+rating+like+dislike);
//
//                            ReviewDataModel reviewDataModel = new ReviewDataModel(_id, name, image, commentTitle, commentBody, commentTime, like, dislike, rating);
//                            reviewDataModels.add(reviewDataModel);
//                        }
//
//                    }
//
//
//
//
//                }
//
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            reviewAdapter = new ReviewAdapter(getActivity(), reviewDataModels, reviewRecycler);
//            reviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//            reviewRecycler.setAdapter(reviewAdapter);
//        }
//
//    }


}