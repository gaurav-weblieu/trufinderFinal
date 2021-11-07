package com.business.findtrue.vendor;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularEditText;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageViedoGalleryActivity extends BaseActivity {

    private Context context = ManageViedoGalleryActivity.this;
    private ImageView back;
    private RegularEditText editYouTube;
    private RegularButton btnSubmit;
    private ApiInterface apiInterface;
    String VENDOR_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_viedo_gallery);

        back = findViewById(R.id.back);
        editYouTube = findViewById(R.id.editYouTube);
        btnSubmit = findViewById(R.id.btnSubmit);

        VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

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
                String youTubeLink = editYouTube.getText().toString();
                apiInterface.uploadYouTubeUrl(VENDOR_ID, youTubeLink).enqueue(new Callback<ResponseRequest>() {
                    @Override
                    public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                        if (response.isSuccessful()){
                            hideDialog();
                            Toast.makeText(context, "Upload successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRequest> call, Throwable t) {
                        Toast.makeText(context, "Upload falied", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
            }
        });
    }
}