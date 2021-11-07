package com.business.findtrue.vendor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.business.findtrue.R;
import com.business.findtrue.model.EditProfileUpdate;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.service.AppConfig;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileImageActivity extends AppCompatActivity {

    private Context context = ProfileImageActivity.this;
    private ImageView back;
    private FloatingActionButton floatingActionButton;
    String VENDOR_ID;
    private ApiInterface apiInterface;
    private ImageView imageView7;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        back = (ImageView)findViewById(R.id.back);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        imageView7 = (ImageView)findViewById(R.id.imageView7);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        System.out.println("ghgjghjghj=========================="+VENDOR_ID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getEditProfile();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });
    }

    public void onSelectImageClick(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //System.out.println("resultUri-----------------------------"+resultUri);
                try {
                    InputStream is = getContentResolver().openInputStream(resultUri);
                    uploadFile(VENDOR_ID, getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }


    private void uploadFile(String user_id, byte[] imageBytes){

        RequestBody idBody = RequestBody.create(okhttp3.MultipartBody.FORM, user_id);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", "image.jpg", requestFile);

        System.out.println("================================="+user_id);
        apiInterface.updateVendorPic(idBody, body).enqueue(new Callback<ResponseRequest>() {
            @Override
            public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                //System.out.println("=================================");
                if (response.isSuccessful()){
                    getEditProfile();
                    Toast.makeText(ProfileImageActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseRequest> call, Throwable t) {
                Toast.makeText(ProfileImageActivity.this, "Image Updated failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEditProfile(){
        apiInterface.getVendorEditProfile(VENDOR_ID).enqueue(new Callback<EditProfileUpdate>() {
            @Override
            public void onResponse(Call<EditProfileUpdate> call, Response<EditProfileUpdate> response) {
                if (response.isSuccessful()){
                    Log.d("data_sdhfdsfdsgl",response.body().getData().getVendorName());
                    Log.d("profile_data",new Gson().toJson(response.body().getData()));

                    Glide.with(ProfileImageActivity.this)
                            .load(AppConfig.CATEGORY_DETAILS_IMAGE + response.body().getData().getNewProfileImg())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar2.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar2.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imageView7);
                    //Glide.with(context).load(AppConfig.CATEGORY_DETAILS_IMAGE + response.body().getData().getNewProfileImg()).into(imageView7);

                }
            }

            @Override
            public void onFailure(Call<EditProfileUpdate> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
            }
        });
    }
}