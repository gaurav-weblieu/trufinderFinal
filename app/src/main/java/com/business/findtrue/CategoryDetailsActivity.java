package com.business.findtrue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pramonow.endlessrecyclerview.EndlessRecyclerView;
import com.pramonow.endlessrecyclerview.EndlessScrollCallback;
import com.business.findtrue.adapter.CategoryDetailsAdapter;
import com.business.findtrue.model.CategoryData;
import com.business.findtrue.model.CategoryDetails;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private ImageView ivBack;
    private RecyclerView mRecyclerView;
    private CategoryDetailsAdapter mCategoryDetailsAdapter;
    private List<CategoryData> listCategoryData = new ArrayList<>();

    private ApiInterface apiInterface;
    String CATEGORY_NAME;
    String CITY_NAME;
    private ProgressBar progressBar3;
    private TextView textView14;
    private int offset = 0;
    private EndlessRecyclerView endlessRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

//        mToolBar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(mToolBar);
        endlessRecyclerView = findViewById(R.id.endless_scroll_view);
        ivBack = (ImageView)findViewById(R.id.ivBack);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerDetails);
        progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);
        textView14 = (TextView)findViewById(R.id.textView14);
        listCategoryData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);


        Intent intent = getIntent();
        if (intent!=null){
            CATEGORY_NAME = intent.getStringExtra("CATEGORY_NAME");
            CITY_NAME = intent.getStringExtra("CITY_NAME");
            System.out.println("CATEGORY_NAME----------------------"+CATEGORY_NAME);
            System.out.println("CITY_NAME----------------------"+CITY_NAME);
            AppConstant.CATEGORY_NAME = CATEGORY_NAME;
            AppConstant.CITY_NAME = CITY_NAME;
        }
        //CATEGORY_NAME =

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        getCategoryDetails();

        endlessRecyclerView.setEndlessScrollCallback(new EndlessScrollCallback() {
            @Override
            public void loadMore() {
                if(listCategoryData.size()%10==0){
                    getCategoryDetails();
                }

            }
        });
        mCategoryDetailsAdapter = new CategoryDetailsAdapter(CategoryDetailsActivity.this, listCategoryData);
        endlessRecyclerView.setAdapter(mCategoryDetailsAdapter);


    }


    //create function get api call in getCategoryDetails

    private void getCategoryDetails(){
        progressBar3.setVisibility(View.VISIBLE);
        String replaceText = CATEGORY_NAME;
        String replaceCategoryName = replaceText.replace(' ', '-');
        System.out.println("rel------------------------------"+replaceCategoryName);
        //String mobile_gallery = "mobile-gallery";
        //String city = "delhi";
        System.out.println("city_name=======-------------"+AppConstant.CITY_NAME);
        //AppConstant.CITY_NAME =
        //textView14.setVisibility(View.VISIBLE);
        String city = AppConstant.CITY_NAME;
        String replaceCity = city.replace(' ', '-');
        apiInterface.getCategoryDetails(replaceCategoryName, replaceCity,"",listCategoryData.size()+"").enqueue(new Callback<CategoryDetails>() {
            @Override
            public void onResponse(Call<CategoryDetails> call, Response<CategoryDetails> response) {
                System.out.println("---------------------------------------+++++");
                if (response.isSuccessful()){
                    progressBar3.setVisibility(View.GONE);
                    String type = response.body().getType();
                    System.out.println("type----------------------------"+type);
                    List<CategoryData> results = response.body().getData();
                    for (CategoryData data : results){
//                        CategoryData categoryData = new CategoryData();
//                        categoryData.getAddress();
//
                        listCategoryData.add(data);
                        mCategoryDetailsAdapter.setList(listCategoryData);
                        //mRecyclerView.setAdapter(mCategoryDetailsAdapter);
                    }
                }

            }

            @Override
            public void onFailure(Call<CategoryDetails> call, Throwable t) {
                System.out.println("--------------------------------------====-");
                progressBar3.setVisibility(View.GONE);
                textView14.setVisibility(View.VISIBLE);
            }
        });
    }
}