package com.business.findtrue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.business.findtrue.adapter.AddShortListAdapter;
import com.business.findtrue.model.AddShortList;
import com.business.findtrue.model.WishlistData;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToShortListActivity extends AppCompatActivity {

    private Context context = AddToShortListActivity.this;
    private RecyclerView mRecyclerView;
    private ApiInterface apiInterface;
    String USER_ID;
    private AddShortListAdapter addShortListAdapter;
    private List<WishlistData> listWishList;
    private ImageView ivBackScreen;
    LinearLayout linear_layout_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_short_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewAddWishlist);
        ivBackScreen = (ImageView)findViewById(R.id.ivBackScreen);
        linear_layout_no_data = findViewById(R.id.linear_layout_no_data);
        listWishList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        USER_ID = CommonUtils.getPreferencesString(context, AppConstant.USER_ID);

        apiInterface.getShortList(USER_ID).enqueue(new Callback<AddShortList>() {
            @Override
            public void onResponse(Call<AddShortList> call, Response<AddShortList> response) {
                System.out.println("id============================="+USER_ID);
                if (response.body().getType().equals("success")){
                    System.out.println("message----------------------"+response.body().getMessage());
                    List<WishlistData> results = response.body().getData();
                    for (WishlistData data : results){
                        listWishList.add(data);
                        addShortListAdapter = new AddShortListAdapter(AddToShortListActivity.this, listWishList);
                        mRecyclerView.setAdapter(addShortListAdapter);
                    }

                }else {
                    linear_layout_no_data.setVisibility(View.VISIBLE);
                    System.out.println("message----------------------"+response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddShortList> call, Throwable t) {
                linear_layout_no_data.setVisibility(View.VISIBLE);

            }
        });

        ivBackScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}