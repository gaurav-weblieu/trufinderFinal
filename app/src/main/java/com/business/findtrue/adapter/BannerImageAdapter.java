package com.business.findtrue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.findtrue.R;
import com.business.findtrue.model.AllAdsBannerResponseModel;

import java.util.List;

public class BannerImageAdapter extends RecyclerView.Adapter<BannerImageAdapter.ViewHolder>{
    Context mContext;
    List<AllAdsBannerResponseModel.Datum> packageDetailsList;
    String baseURl;

    public BannerImageAdapter(Context mContext,String baseURl, List<AllAdsBannerResponseModel.Datum> packageDetailsList){
        this.mContext = mContext;
        this.baseURl = baseURl;
        this.packageDetailsList = packageDetailsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.banner_image_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllAdsBannerResponseModel.Datum item = packageDetailsList.get(position);
        Glide.with(mContext)
                .load(baseURl+"/"+ item.getBannerImage())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return packageDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.bannerImage);
        }
    }
}
