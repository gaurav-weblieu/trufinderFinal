package com.business.findtrue.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.business.findtrue.CategoryDetailsActivity;
import com.business.findtrue.MainActivity;
import com.business.findtrue.R;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.listner.ProductListner;
import com.business.findtrue.model.Category;
import com.business.findtrue.model.CityList;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.service.AppConfig;
import com.business.findtrue.utils.AppConstant;

import java.util.List;
import java.util.Random;

import de.mrapp.android.dialog.MaterialDialog;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context mContext;
    List<Category> categoryList;
    private ApiInterface apiInterface;
    CityAdapter cityAdapter;
    ProductListner lisnter = null;
    MaterialDialog.Builder builder;
    private boolean expanedOrNot;

    public CategoryAdapter(Context mContext,
                           List<Category> categoryList,
                           boolean expanedOrNot,
                           ProductListner lisnter) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.expanedOrNot = expanedOrNot;
        this.lisnter = lisnter;
    }

    public CategoryAdapter(Context mContext,
                           List<Category> categoryList,
                           boolean expanedOrNot){
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.expanedOrNot = expanedOrNot;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem=null;
        if(expanedOrNot)
        {
            listItem = layoutInflater.inflate(R.layout.all_category_list_item, parent, false);
        }else {
            listItem = layoutInflater.inflate(R.layout.category_item, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Category category = categoryList.get(position);
        Random rnd = new Random();
        /*int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.linear_one.setBackgroundColor(currentColor);*/
//        if (position%2 != 0){
//            holder.linear_one.setBackgroundColor(Color.parseColor("#33F6D3D3"));
//        }else{
//            holder.linear_one.setBackgroundColor(Color.parseColor("#33DBFFF7"));
//        }

        Log.d("image URL",AppConfig.IMAGE_URL + category.getCategory_icon());
        holder.tvCategoryName.setText(category.getCategory_name());
        Glide.with(mContext).load(AppConfig.IMAGE_URL + category.getCategory_icon()).into(holder.ivCategoryIcon);

       /* MainActivity.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new MaterialDialog.Builder(mContext);
                builder.setView(R.layout.change_city_dialog);
                builder.show();
                RecyclerView mRecyclerView = (RecyclerView) builder.create().findViewById(R.id.recyclerViewCityName);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerView.setHasFixedSize(true);
                ImageView imageClose = (ImageView)builder.create().findViewById(R.id.imageViewClose);
                ProgressBar mProgressBar = (ProgressBar)builder.create().findViewById(R.id.mProgressBar);

                List<CityList> cityLists = new ArrayList<>();
                cityAdapter = new CityAdapter(mContext, cityLists);
                apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                apiInterface.getCity().enqueue(new Callback<CityModel>() {
                    @Override
                    public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                        if (response.isSuccessful()){
                            mProgressBar.setVisibility(View.GONE);
                            System.out.println("type=========------------------------"+response.body().getType());
                            List<CityList> models = response.body().getData();
                            for (CityList list : models){
                                cityLists.add(list);
                                cityAdapter = new CityAdapter(mContext, cityLists);
                                mRecyclerView.setAdapter(cityAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CityModel> call, Throwable t) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.create().hide();
                    }
                });
            }
        });*/

        holder.linear_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lisnter!=null){
                    lisnter.productClick(position,category);
                }else {
                    Intent intent = new Intent(mContext, CategoryDetailsActivity.class);
                    intent.putExtra("CATEGORY_NAME", category.getId());
                    mContext.startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        if(!expanedOrNot){
            return 12;
        }else {
            return categoryList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivCategoryIcon;
        RegularTextView tvCategoryName;
        LinearLayout linear_one;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCategoryIcon = (ImageView)itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            linear_one = (LinearLayout)itemView.findViewById(R.id.linear_one);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }

    //city adapter class

    public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
        Context context;
        List<CityList> cityLists;

        public CityAdapter(Context context, List<CityList> cityLists) {
            this.context = context;
            this.cityLists = cityLists;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.city_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CityList cityList = cityLists.get(position);
            System.out.println("city_name-------------------------"+cityList.getCity());
            holder.tvCityName.setText(cityList.getCity());

            //city name in on click event
            holder.tvCityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, cityList.getCity_alias(), Toast.LENGTH_LONG).show();
                    AppConstant.CITY_NAME = cityList.getCity_alias();
                    System.out.println("AppCityName------------------"+ AppConstant.CITY_NAME);
                    MainActivity.tvLocation.setText(cityList.getCity());
                    builder.create().hide();

                }
            });
        }


        @Override
        public int getItemCount() {
            return cityLists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            RegularTextView tvCityName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCityName = itemView.findViewById(R.id.tvCityName);
            }
        }
    }

}