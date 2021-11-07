package com.business.findtrue.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.custom.TitleTextView;
import com.business.findtrue.login.LoginActivity;
import com.business.findtrue.model.UpdateDeviceToken;
import com.business.findtrue.notification.NotificationActivity;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorMainActivity extends BaseActivity {

    private Context context = VendorMainActivity.this;
    private RelativeLayout mRelativeOne, mRelativeTwo, mRelativeThree, mRelativeFour, mRelativeFive, mRelativeSix, relativeLogOut, relativeLid, relativePackage;
    AlertDialog materialAlertDialogBuilder;
    @BindView(R.id.userName)
    RegularTextView userName;
    ImageView notification;
    TitleTextView totalLeads;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vendor_main);
        ButterKnife.bind(this);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        mRelativeOne = (RelativeLayout)findViewById(R.id.relativeProfile);
        mRelativeTwo = (RelativeLayout)findViewById(R.id.relativeEditProfile);
        mRelativeThree = (RelativeLayout)findViewById(R.id.relativeDescription);
        mRelativeFour = (RelativeLayout)findViewById(R.id.relativeFaq);
        mRelativeFive = (RelativeLayout)findViewById(R.id.relativeGallery);
        mRelativeSix = (RelativeLayout)findViewById(R.id.relativeVideo);
        relativeLogOut = (RelativeLayout)findViewById(R.id.relativeLogOut);
        relativeLid = (RelativeLayout)findViewById(R.id.relativeLid);
        relativePackage = (RelativeLayout)findViewById(R.id.relativePackage);
        notification = findViewById(R.id.iv_notification);
        totalLeads = findViewById(R.id.total_leads);


        String VENDOR_ID = CommonUtils.getPreferencesString(context, AppConstant.VENDOR_ID);
        System.out.println("VENDOR_ID================================"+VENDOR_ID);
        userName.setText(CommonUtils.getPreferencesString(context,AppConstant.VENDOR_NAME));

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        String fcmToken = CommonUtils.getPreferencesString(context, AppConstant.FCM_TOKEN);
        Log.d("fcm_token",fcmToken);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        updateDeviceToken(token);
                        Log.d(TAG, token);
                    }
                });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotificationActivity.class);
                startActivity(intent);
            }
        });
        relativePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageActivity.class);
                startActivity(intent);
            }
        });
        mRelativeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileImageActivity.class);
                startActivity(intent);
            }
        });

        mRelativeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        mRelativeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LeadActivity.class);
                startActivity(intent);
            }
        });

        mRelativeFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFAQActivity.class);
                startActivity(intent);

            }
        });

        mRelativeFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageGalleryActivity.class);
                startActivity(intent);
            }
        });

        mRelativeSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageViedoGalleryActivity.class);
                startActivity(intent);
            }
        });

        relativeLid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                startActivity(intent);
            }
        });

        relativeLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final FlatDialog flatDialog = new FlatDialog(context);
//                flatDialog.setBackgroundColor(R.color.app_color2);
//                flatDialog.setTitle("Logout")
//                        .setSubtitle("Are you sure you want to end the session")
//                        .setFirstButtonText("YES")
//                        .setSecondButtonText("NO")
//                        .withFirstButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                CommonUtils.savePreferenceString(context, AppConstant.IS_LOGGED_IN, "false");
//                                finish();
//                                flatDialog.dismiss();
//                            }
//                        })
//                        .withSecondButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                flatDialog.dismiss();
//                            }
//                        })
//                        .show();
                logout();
            }
        });

    }







    private void updateDeviceToken(String deviceId){
        String user_id = CommonUtils.getPreferencesString(this, AppConstant.VENDOR_ID);
        String usertype = CommonUtils.getPreferencesString(this, AppConstant.IS_USERS);
        apiInterface.updateDeviceToken(deviceId, user_id,usertype).enqueue(new Callback<UpdateDeviceToken>() {
            @Override
            public void onResponse(Call<UpdateDeviceToken> call, Response<UpdateDeviceToken> response) {
                hideDialog();
                if (response.isSuccessful()){
                }
            }

            @Override
            public void onFailure(Call<UpdateDeviceToken> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void logout() {

        View view = LayoutInflater.from(this).inflate(R.layout.logout_custom_view, null, false);
        MaterialButton ok = view.findViewById(R.id.ok);
        MaterialButton cancel = view.findViewById(R.id.cancel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                    .setView(view)
                    .setBackground(getDrawable(R.drawable.grid_bg))
                    .setCancelable(false)
                    .show();
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    materialAlertDialogBuilder.dismiss();
                    CommonUtils.savePreferenceString(context, AppConstant.IS_LOGGED_IN, "false");
                    CommonUtils.clearPreferenceString(VendorMainActivity.this);
                    startActivity(new Intent(VendorMainActivity.this, LoginActivity.class));
                    finish();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialAlertDialogBuilder.dismiss();
                }
            });
        }
    }
}