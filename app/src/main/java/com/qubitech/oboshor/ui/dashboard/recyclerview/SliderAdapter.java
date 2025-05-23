package com.qubitech.oboshor.ui.dashboard.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qubitech.oboshor.R;
import com.qubitech.oboshor.datamodels.SliderViewDataModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class SliderViewHolder extends SliderViewAdapter.ViewHolder {
    // Adapter class for initializing
    // the views of our slider view.
    View itemView;
    ImageView imageViewBackground;

    public SliderViewHolder(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.myimage);
        imageViewBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.itemView = itemView;
    }
}


public class SliderAdapter extends SliderViewAdapter<SliderViewHolder> {

    // list for storing urls of images.
    private List<SliderViewDataModel> sliderViewDataModels;
    private Activity activity;

    // Constructor
    public SliderAdapter(Activity activity, ArrayList<SliderViewDataModel> sliderViewDataModels) {
        this.activity = activity;
        this.sliderViewDataModels = sliderViewDataModels;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, final int position) {

        SliderViewDataModel sliderViewDataModel = this.sliderViewDataModels.get(position);

        Picasso.get().load(sliderViewDataModel.getImgUrl()).into(viewHolder.imageViewBackground);
        Log.d("SliderView",sliderViewDataModel.getImgUrl());
        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sliderViewDataModel.getLink()));
                activity.startActivity(browserIntent);
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return sliderViewDataModels.size();
    }

    public void setSliderViewDataModels(ArrayList<SliderViewDataModel> sliderViewDataModels){
        this.sliderViewDataModels = sliderViewDataModels;
        notifyDataSetChanged();
    }
}