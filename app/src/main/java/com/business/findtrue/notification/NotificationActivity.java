package com.business.findtrue.notification;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.business.findtrue.R;
import com.business.findtrue.adapter.NotificationAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.model.NotificationResponseModel;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {
    private ApiInterface apiInterface;
    RecyclerView rvNotification;
    NotificationAdapter notificationAdapter;
    private ImageView back;
    LinearLayout linear_layout_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        /*  FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("TAG", msg);
                        Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
*/


        rvNotification = findViewById(R.id.rv_notification);
        linear_layout_no_data = findViewById(R.id.linear_layout_no_data);
        back = (ImageView)findViewById(R.id.back);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        getNotofication();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getNotofication(){
        showDialog();
        String user_id = CommonUtils.getPreferencesString(this, AppConstant.USER_ID);
        if(user_id==null || user_id ==""){
            user_id = CommonUtils.getPreferencesString(this, AppConstant.VENDOR_ID);
        }
        apiInterface.getNotification(user_id).enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                hideDialog();
                if (response.isSuccessful()){
                    if(response.body().getData().size()>0){
                        notificationAdapter = new NotificationAdapter(NotificationActivity.this,response.body().getData());
                        rvNotification.setAdapter(notificationAdapter);
                    }else {
                        linear_layout_no_data.setVisibility(View.VISIBLE);
                        // Toast.makeText(NotificationAct    ivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                linear_layout_no_data.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {
                linear_layout_no_data.setVisibility(View.VISIBLE);

                hideDialog();
            }
        });
    }



}