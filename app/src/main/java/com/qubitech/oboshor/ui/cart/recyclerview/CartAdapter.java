package com.qubitech.oboshor.ui.cart.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.qubitech.oboshor.R;
import com.qubitech.oboshor.api.RestAPI;
import com.qubitech.oboshor.api.RetrofitClient;
import com.qubitech.oboshor.datamodels.BookDataModel;
import com.qubitech.oboshor.datamodels.CartDataModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.qubitech.oboshor.api.RetrofitClient.BASE_URL;

public class CartAdapter  extends RecyclerView.Adapter<CartViewHolder> {
    List<CartDataModel> cartDataModels=new ArrayList<>();

    MutableLiveData<Integer> total=new MutableLiveData<>();
    AddRemoveOnclickListener onclickListener;


    public CartAdapter( AddRemoveOnclickListener onclickListener) {


        this.onclickListener = onclickListener;
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cart,parent,false);
        CartViewHolder mvh=new CartViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {

        holder.title.setText(cartDataModels.get(position).getTitle());
        holder.author.setText(cartDataModels.get(position).getAuthor());
        holder.price.setText(Integer.toString(cartDataModels.get(position).getPrice()));
        holder.category.setText(cartDataModels.get(position).getCategory());
        holder.quantity.setText(Integer.toString(cartDataModels.get(position).getQuantity()));
        Picasso.get().load(BASE_URL + "/" + cartDataModels.get(position).getImageUrl()).into(holder.bookImage);



        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = Integer.parseInt(holder.quantity.getText().toString()) +1;
                holder.quantity.setText(String.valueOf(newQuantity));
                cartDataModels.get(position).setQuantity(newQuantity);
                onclickListener.onClick(cartDataModels.get(position));
                total.setValue(total.getValue()+cartDataModels.get(position).getPrice());
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = Integer.parseInt(holder.quantity.getText().toString()) -1;
                holder.quantity.setText(String.valueOf(newQuantity));
                cartDataModels.get(position).setQuantity(newQuantity);
                onclickListener.onClick(cartDataModels.get(position));
                total.setValue(total.getValue()-cartDataModels.get(position).getPrice());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartDataModels.size();
    }

    public MutableLiveData<Integer> getTotalAmount(){
        total.setValue(0);
        for (int i = 0; i <cartDataModels.size() ; i++) {
            total.setValue(total.getValue()+ cartDataModels.get(i).getPrice()*cartDataModels.get(i).getQuantity());
        }
        return total;
    }

    public void setData(List<CartDataModel> cartDataModelList){
        this.cartDataModels=cartDataModelList;
        notifyDataSetChanged();
        getTotalAmount();
    }

}
