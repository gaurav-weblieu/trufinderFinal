package com.business.findtrue.vendor;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.pramonow.endlessrecyclerview.EndlessRecyclerView;
import com.pramonow.endlessrecyclerview.EndlessScrollCallback;
import com.business.findtrue.R;
import com.business.findtrue.adapter.LeadTableViewAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.model.GetUserLead;
import com.business.findtrue.model.TotalLeadsResponseModel;
import com.business.findtrue.model.UserLeads;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadActivity extends BaseActivity implements LeadTableViewAdapter.LeadViewSuccess {

    private Context context = LeadActivity.this;
    private RecyclerView mRecyclerViewLead;
    private List<UserLeads> leadList = new ArrayList<>();
    private ApiInterface apiInterface;
    String VENDOR_ID;
    private ProgressBar progressBar;
    private ImageView back, imageViewIcons;
    private int offset = 0;
    private EndlessRecyclerView endlessRecyclerView;
    LeadTableViewAdapter leadTableViewAdapter;
    RegularTextView totalLeads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        endlessRecyclerView = findViewById(R.id.endless_scroll_view);
        mRecyclerViewLead = (RecyclerView) findViewById(R.id.recyclerViewLeadList);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageViewIcons = (ImageView) findViewById(R.id.imageViewIcons);
        back = (ImageView) findViewById(R.id.back);
        totalLeads = findViewById(R.id.total_leads);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewLead.setLayoutManager(linearLayoutManager);

        leadList = new ArrayList<>();
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        System.out.println("VENDOR_ID================================" + VENDOR_ID);

        if (TextUtils.isDigitsOnly(CommonUtils.getPreferencesString(this, AppConstant.TOTAL_LEADS))) {
            if (Integer.parseInt(CommonUtils.getPreferencesString(this, AppConstant.TOTAL_LEADS)) > 0) {
                totalLeads.setVisibility(View.VISIBLE);
                totalLeads.setText("Leads : " + CommonUtils.getPreferencesString(this, AppConstant.TOTAL_LEADS));
            } else {
                totalLeads.setText("Leads : 0");
            }
        }

        getAllLeads();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        endlessRecyclerView.setEndlessScrollCallback(new EndlessScrollCallback() {
            @Override
            public void loadMore() {
                if (leadList.size() % 10 == 0) {
                    getAllLeads();
                }

            }
        });
        leadTableViewAdapter = new LeadTableViewAdapter(LeadActivity.this, leadList, this);
        endlessRecyclerView.setAdapter(leadTableViewAdapter);

        getTotalLeads();

    }

    private void getTotalLeads() {
        //showDialog();
        String vendorId = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        apiInterface.getTotalLeads(vendorId).enqueue(new Callback<TotalLeadsResponseModel>() {
            @Override
            public void onResponse(Call<TotalLeadsResponseModel> call, Response<TotalLeadsResponseModel> response) {
                //showDialog();
                if (response.isSuccessful()) {
                    if (response.body().getType().equalsIgnoreCase("error")) {
                        totalLeads.setText("Leads : 0");
                    } else {
                        CommonUtils.savePreferenceString(context, AppConstant.TOTAL_LEADS, response.body().getData().getLastUpdtTotlLeads());
                        if (TextUtils.isDigitsOnly(CommonUtils.getPreferencesString(LeadActivity.this, AppConstant.TOTAL_LEADS))) {
                            if (Integer.parseInt(CommonUtils.getPreferencesString(LeadActivity.this, AppConstant.TOTAL_LEADS)) > 0) {
                                totalLeads.setText("Leads : " + response.body().getData().getLastUpdtTotlLeads());
                            } else {
                                totalLeads.setText("Leads : 0");
                            } }
                    } }
            }
            @Override
            public void onFailure(Call<TotalLeadsResponseModel> call, Throwable t) {
                //showDialog();
            }
        });
    }

    private void getAllLeads() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.getUserLeads(VENDOR_ID, offset).enqueue(new Callback<GetUserLead>() {
            @Override
            public void onResponse(Call<GetUserLead> call, Response<GetUserLead> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("all leads-------------------------------" + offset);
                    if (response.body().getData().size() > 0) {
                        leadList.addAll(response.body().getData());
                        //leadTableViewAdapter = new LeadTableViewAdapter(LeadActivity.this, leadList);
                        //mRecyclerViewLead.setAdapter(leadTableViewAdapter);
                        leadTableViewAdapter.setList(leadList);
                        offset = leadList.size();
                    } }
            }
            @Override
            public void onFailure(Call<GetUserLead> call, Throwable t) {
                Log.d("Error", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onLeadViewSuccess(String leads) {
        if (TextUtils.isDigitsOnly(CommonUtils.getPreferencesString(LeadActivity.this, AppConstant.TOTAL_LEADS))) {
            if (Integer.parseInt(CommonUtils.getPreferencesString(LeadActivity.this, AppConstant.TOTAL_LEADS)) > 0) {
                totalLeads.setText("Leads : " + leads);
            } else totalLeads.setText("Leads : 0");
        }
    }
}