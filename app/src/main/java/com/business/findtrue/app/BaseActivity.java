package com.business.findtrue.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.business.findtrue.R;
import com.business.findtrue.app.connection.ConnectionLiveData;
import com.business.findtrue.app.connection.ConnectionModel;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BaseActivity extends AppCompatActivity {
    //protected static final String TAG = "BaseActivity";
    //protected AppSharePreference appSharePreference;
    protected boolean isInternet = false;
    protected ApiInterface apiInterface;
    protected String TAG;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //appSharePreference = AppSharePreference.getInstance(getApplicationContext());
        checkInternet();
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        TAG = this.getClass().getSimpleName();
        pDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
    }

    protected void hideKeyboard(){
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(this.getWindow(), 0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void showDialog(){
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    protected void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    protected void showSuccessMessage(String title,String message){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    protected void showErrorMessage(String title,String message){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    protected boolean isValidMobile(String phone) {
        if (phone.length()==10) {
            return true;
        } else if(!Pattern.matches("(0/91)?[6-9][0-9]{9}", phone)) {
            return phone.length() > 6 && phone.length() <= 10;
        } else {
            return false;
        }

        /*if(!Pattern.matches("(0/91)?[6-9][0-9]{9}", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }*/
        /*if (!TextUtils.isEmpty(phone)) {
            return Patterns.PHONE.matcher(phone).matches();
        }*/

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void checkInternet() {
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connection) {
                if(connection==null){
                    return;
                }
                if (connection.getIsConnected()) {
                    switch (connection.getType()) {
                        case 1:
                            //setInternetStatusValue(connection.getIsConnected());
                            Log.d(TAG, String.format("Wifi turned ON"));
                            isInternet = true;
                            break;
                        case 2:
                            //setInternetStatusValue(connection.getIsConnected());
                            isInternet= true;
                            Log.d(TAG, String.format("Mobile data turned ON"));
                            break;
                    }
                } else {
                    //setInternetStatusValue(connection.getIsConnected());
                    isInternet = false;
                    Log.d(TAG, String.format("Connection turned OFF"));
                }
            }
        });
    }

    protected void startNewActvity(Context context, Class<?> cls){
        startActivity(new Intent(context,cls));
        overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}
