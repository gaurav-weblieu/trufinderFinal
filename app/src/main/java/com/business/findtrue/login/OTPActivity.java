package com.business.findtrue.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.model.ForgotPasswordModel;
import com.business.findtrue.model.OTPModel;
import com.business.findtrue.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends BaseActivity {

    String data;
    String type;
    int intentType = 0;
    int otpValue = 0;
    @BindView(R.id.otp)
    RegularTextInputEditText otp;
    @BindView(R.id.inputLayoutOTP)
    RegularTextInputLayout inputLayoutOTP;
    @BindView(R.id.newPassword)
    RegularTextInputEditText newPassword;
    @BindView(R.id.inputLayoutNewPassword)
    RegularTextInputLayout inputLayoutNewPassword;
    @BindView(R.id.confirmPassword)
    RegularTextInputEditText confirmPassword;
    @BindView(R.id.inputLayoutConfirmPassword)
    RegularTextInputLayout inputLayoutConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        data = getIntent().getStringExtra("data");
        //isEmailValid(data) : true : type = "mobile" ? type = "email"
        intentType = getIntent().getIntExtra("intentType",0);
        if(isEmailValid(data)){
            type = "email";
        }else {
            type = "mobile";
        }
        sendOTP();
        Log.d(TAG,data+" : "+type);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable == otp.getEditableText()){
                    if(editable.toString().length()==6){
                        if(otpValue == Integer.parseInt(editable.toString())){
                            hideKeyboard();
                            otp.setFocusable(false);
                            inputLayoutNewPassword.setVisibility(View.VISIBLE);
                            inputLayoutConfirmPassword.setVisibility(View.VISIBLE);
                        }else {
                            inputLayoutOTP.requestFocus();
                            inputLayoutOTP.setErrorEnabled(true);
                            inputLayoutOTP.setError("OTP not match");
                        }
                    }else if(inputLayoutOTP.isErrorEnabled()){
                        inputLayoutOTP.setErrorEnabled(false);
                    }
                }



                if(inputLayoutConfirmPassword.isErrorEnabled()){
                    inputLayoutConfirmPassword.setErrorEnabled(false);
                }

            }
        };

        otp.addTextChangedListener(afterTextChangedListener);
        confirmPassword.addTextChangedListener(afterTextChangedListener);

    }


    @OnClick(R.id.submit)
    void submit(){
        if (CommonUtils.isOnline(OTPActivity.this)){
            if (checkValidation()){
                if(intentType!=0){
                    if(intentType==1){
                        apiInterface.verifyOTP(type,data,newPassword.getText().toString().trim()).enqueue(new Callback<ForgotPasswordModel>() {
                            @Override
                            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                                if (response.isSuccessful()){
                                    System.out.println("response----------------------"+new Gson().toJson(response.body()));
                                    Toast.makeText(OTPActivity.this, "Password reset successfully", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            }

                            @Override
                            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                                Toast.makeText(OTPActivity.this, "User registration failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else if(intentType==2){
                        apiInterface.verifyVendorOTP(type,data,newPassword.getText().toString().trim()).enqueue(new Callback<ForgotPasswordModel>() {
                            @Override
                            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                                if (response.isSuccessful()){
                                    System.out.println("response----------------------"+new Gson().toJson(response.body()));
                                    Toast.makeText(OTPActivity.this, "Password reset successfully", Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            }

                            @Override
                            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                                Toast.makeText(OTPActivity.this, "User registration failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }


            }
        }else {
            Toast.makeText(OTPActivity.this, "Please check your intenet connection", Toast.LENGTH_LONG).show();
        }
        //startNewActvity(this,ResetPasswordActivity.class);
    }

    private void sendOTP(){
        showDialog();
        if (CommonUtils.isOnline(OTPActivity.this)){
            if(intentType!=0){
                if(intentType==1){
                    apiInterface.sendOTP(type,data).enqueue(new Callback<OTPModel>() {
                        @Override
                        public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                            hideDialog();
                            if (response.isSuccessful()){
                                System.out.println("response----------------------"+response.body().getMessage());
                                System.out.println("response----------------------"+response.body().getData().getOtp());
                                Toast.makeText(OTPActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                otpValue = response.body().getData().getOtp();

                            }
                        }

                        @Override
                        public void onFailure(Call<OTPModel> call, Throwable t) {
                            hideDialog();
                            Toast.makeText(OTPActivity.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else if(intentType==2){
                    apiInterface.vendorSendOTP(type,data).enqueue(new Callback<OTPModel>() {
                        @Override
                        public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                            hideDialog();
                            if (response.isSuccessful()){
                                System.out.println("response----------------------"+response.body().getMessage());
                                System.out.println("response----------------------"+response.body().getData().getOtp());
                                Toast.makeText(OTPActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                otpValue = response.body().getData().getOtp();

                            }
                        }

                        @Override
                        public void onFailure(Call<OTPModel> call, Throwable t) {
                            hideDialog();
                            Toast.makeText(OTPActivity.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }


        }else {
            Toast.makeText(OTPActivity.this, "Please check your intenet connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkValidation(){
        boolean result = false;
        if (!newPassword.getText().toString().trim().equalsIgnoreCase(confirmPassword.getText().toString().trim())) {
            inputLayoutConfirmPassword.requestFocus();
            inputLayoutConfirmPassword.setErrorEnabled(true);
            inputLayoutConfirmPassword.setError("Password not match");
            result = false;

        }else {
            result = true;
        }
        return result;
    }
}