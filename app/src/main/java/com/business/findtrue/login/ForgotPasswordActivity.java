package com.business.findtrue.login;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.model.VerifyEmailModel;
import com.business.findtrue.model.VerifyEmailVendorModel;
import com.business.findtrue.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.email)
    RegularTextInputEditText email;
    @BindView(R.id.inputLayoutEmail)
    RegularTextInputLayout inputLayoutEmail;
    AlertDialog materialAlertDialogBuilder;
    int intentType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        intentType = getIntent().getIntExtra("intentType",0);
        Log.d("intentType",intentType+"");
    }

    @OnClick(R.id.submit)
    void submit(){
            hideKeyboard();

            if (CommonUtils.isOnline(ForgotPasswordActivity.this)){
                if (checkValidation()){
                    if(intentType!=0){
                        showDialog();
                        if(intentType==1){
                            apiInterface.verifyEmail(email.getText().toString().trim()).enqueue(new Callback<VerifyEmailModel>() {
                                @Override
                                public void onResponse(Call<VerifyEmailModel> call, Response<VerifyEmailModel> response) {
                                    hideDialog();
                                    if (response.isSuccessful()){
                                        System.out.println("response----------------------"+response.body().getMessage());
                                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                        showOptionDialog(response.body().getData().get(0).getEmail(),response.body().getData().get(0).getMobileno());
                                    }
                                }

                                @Override
                                public void onFailure(Call<VerifyEmailModel> call, Throwable t) {
                                    hideDialog();
                                    Toast.makeText(ForgotPasswordActivity.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else if(intentType==2){
                            apiInterface.verifyVendorEmail(email.getText().toString().trim()).enqueue(new Callback<VerifyEmailVendorModel>() {
                                @Override
                                public void onResponse(Call<VerifyEmailVendorModel> call, Response<VerifyEmailVendorModel> response) {
                                    hideDialog();
                                    if (response.isSuccessful()){
                                        System.out.println("response----------------------"+response.body().getMessage());
                                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                        showOptionDialog(response.body().getData().get(0).getVendorEmail(),response.body().getData().get(0).getContactno());
                                    }
                                }

                                @Override
                                public void onFailure(Call<VerifyEmailVendorModel> call, Throwable t) {
                                    hideDialog();
                                    Toast.makeText(ForgotPasswordActivity.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }else {
                Toast.makeText(ForgotPasswordActivity.this, "Please check your intenet connection", Toast.LENGTH_LONG).show();
            }

       // startNewActvity(this,OTPActivity.class);
    }

    private void showOptionDialog(String emailid,String mobileNo) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_radio_button_option, null, false);
        RadioGroup radioGroup =view.findViewById(R.id.radioGroup);
        RadioButton email = view.findViewById(R.id.email);
        RadioButton mobile = view.findViewById(R.id.mobile);
        final RadioButton[] checkedRadioButton = new RadioButton[1];
        RegularButton ok = view.findViewById(R.id.ok);
        if(email.isChecked()){
            checkedRadioButton[0] = email;
        }else {
            checkedRadioButton[0] = mobile;
        }

        email.setText("Recover by : "+emailid);
        mobile.setText("Recover by : "+mobileNo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                    .setView(view)
                    .setBackground(getDrawable(R.drawable.grid_bg))
                    .setCancelable(false)
                    .show();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    // This will get the radiobutton that has changed in its check state
                    checkedRadioButton[0] = group.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    boolean isChecked = checkedRadioButton[0].isChecked();
                    // If the radiobutton that has changed in check state is now checked...
                    if (isChecked)
                    {
                        Log.d("radio checked", checkedRadioButton[0].getText().toString()+" : "+ checkedRadioButton[0].getText().toString().substring( 13 , checkedRadioButton[0].getText().toString().trim().length() ));
                        //tv.setText("Checked:" + checkedRadioButton.getText());
                    }
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ForgotPasswordActivity.this,OTPActivity.class);
                    intent.putExtra("intentType",intentType);
                    intent.putExtra("data", checkedRadioButton[0].getText().toString().substring( 13 , checkedRadioButton[0].getText().toString().trim().length() ));
                    startActivity(intent);
                  //  overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
                    finish();
                    materialAlertDialogBuilder.dismiss();
                }
            });
        }
    }


    private boolean checkValidation(){
        boolean result = false;
        if (!isEmailValid(email.getText().toString().trim())) {
            inputLayoutEmail.requestFocus();
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Please Enter Valid Email id");
            result = false;
        } else {
            result = true;
        }
        return result;
    }
}