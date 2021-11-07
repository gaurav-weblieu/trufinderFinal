package com.business.findtrue.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.business.findtrue.R;
import com.business.findtrue.ViewProfileActivity;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.model.WishlistData;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.service.AppConfig;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShortListAdapter extends RecyclerView.Adapter<AddShortListAdapter.ViewHolder>{
    private Context mContext;
    List<WishlistData> listWishList;
    ApiInterface apiInterface;

    public AddShortListAdapter(Context mContext, List<WishlistData> listWishList){
        this.mContext = mContext;
        this.listWishList = listWishList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.add_short_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WishlistData wishlistData = listWishList.get(position);
        Glide.with(mContext).load(AppConfig.CATEGORY_DETAILS_IMAGE + wishlistData.getProfilepic()).placeholder(R.drawable.no_img).into(holder.ivImage);

        holder.tvVenderName.setText(wishlistData.getVendorName());
        holder.tvAddress.setText(wishlistData.getAddress());
        if (wishlistData.getVendorDetails().getPackagePrice().equals("0")){
            holder.tvPrice.setText("PACKAGE PRICE : " +"\u20B9" +"Ask for Price");
        }else {
            holder.tvPrice.setText("PACKAGE PRICE : " +"\u20B9" +wishlistData.getVendorDetails().getPackagePrice());
        }

        holder.linear_cat_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewProfileActivity.class);
                intent.putExtra("PROFILE_IMAGE", listWishList.get(position).getProfilepic());
                intent.putExtra("VENDOR_NAME", listWishList.get(position).getVendorName());
                CommonUtils.savePreferenceString(mContext, AppConstant.VENDOR_EMAIL, listWishList.get(position).getEmail());
                mContext.startActivity(intent);
            }
        });


        holder.btnEnquiry.setVisibility(View.GONE);
        holder.btnProfile.setVisibility(View.GONE);
        holder.image_shortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isLoggedIn = CommonUtils.getPreferencesString(mContext, AppConstant.IS_LOGGED_IN);
                if (isLoggedIn.equals("true")){
                    apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                    String USER_ID = CommonUtils.getPreferencesString(mContext, AppConstant.USER_ID);
                    String id = listWishList.get(position).getWishlist_id();
                    System.out.println("id----------------------------"+id);
                    apiInterface.removeWishList(id).enqueue(new Callback<ResponseRequest>() {
                        @Override
                        public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                            if (response.isSuccessful()){
                                listWishList.remove(position);
                                notifyDataSetChanged();
                                System.out.println("message----------------------"+response.body().getMessage());
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRequest> call, Throwable t) {

                        }
                    });
//                    //

                }else {
                    Toast.makeText(mContext, "Please Login", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return listWishList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RegularTextView btnEnquiry, btnProfile, tvAddress, tvPrice;
        RoundedImageView ivImage;
        TitleTextView tvVenderName;
        LinearLayout linear_cat_details;
        ImageView image_shortlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEnquiry = itemView.findViewById(R.id.btnSendEnquery);
            btnProfile = itemView.findViewById(R.id.btnProfile);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvVenderName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            linear_cat_details = itemView.findViewById(R.id.linear_cat_details);
            image_shortlist = itemView.findViewById(R.id.image_shortlist);
        }
    }
}
