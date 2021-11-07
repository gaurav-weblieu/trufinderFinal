package com.business.findtrue.vendor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chinalwb.are.AREditor;
import com.chinalwb.are.strategies.VideoStrategy;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.model.DescriptionModel;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionActivity extends BaseActivity {

    private Context context = DescriptionActivity.this;
    private ImageView back;
    private EditText editTextTextPersonName2;
    private ApiInterface apiInterface;
    String VENDOR_ID;
    private RegularButton btnSubmit;
    private ProgressBar progressBar;
    private AREditor arEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        arEditor = findViewById(R.id.areditor);
        this.arEditor.setVideoStrategy(mVideoStrategy);
        back = (ImageView)findViewById(R.id.back);
        //editTextTextPersonName2 = (EditText)findViewById(R.id.editTextTextPersonName2);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        getDescription();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                String description = arEditor.getHtml().toString();
                /*String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?«»“”‘’]))";
                Pattern patt = Pattern.compile(str);
                Matcher matcher = patt.matcher(description);
                description = matcher.replaceAll("<a href=\"$1\">$1</a>");
                System.out.println("description---------------------"+description);*/

                apiInterface.submitDescription(VENDOR_ID, description).enqueue(new Callback<ResponseRequest>() {
                    @Override
                    public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                        hideDialog();
                        if (response.isSuccessful()){
                            Toast.makeText(context, "Add Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRequest> call, Throwable t) {
                        hideDialog();
                        Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    public void getDescription(){
        showDialog();
        apiInterface.getDescription(VENDOR_ID).enqueue(new Callback<DescriptionModel>() {
            @Override
            public void onResponse(Call<DescriptionModel> call, Response<DescriptionModel> response) {
                if (response.isSuccessful()){
                    hideDialog();
                    arEditor.fromHtml(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<DescriptionModel> call, Throwable t) {
                hideDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.arEditor.onActivityResult(requestCode, resultCode, data);
    }

    private VideoStrategy mVideoStrategy = new VideoStrategy() {
        @Override
        public String uploadVideo(Uri uri) {
            try {
                Thread.sleep(3000); // Do upload here
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "http://www.xx.com/x.mp4";
        }

        @Override
        public String uploadVideo(String videoPath) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "http://www.xx.com/x.mp4";
        }
    };
}