package com.business.findtrue.vendor;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.ResponseRequest;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAccountVendorActivity extends BaseActivity {
    private Context context = NewAccountVendorActivity.this;
    private ApiInterface apiInterface;
    private TextView btnSave;
    String mobile_no;

    @BindView(R.id.mainConstraint)
    ConstraintLayout mainConstraint;
    @BindView(R.id.businessName)
    RegularTextInputEditText businessName;
    @BindView(R.id.ownerName)
    RegularTextInputEditText ownerName;
    @BindView(R.id.mobile)
    RegularTextInputEditText mobile;
    @BindView(R.id.email)
    RegularTextInputEditText email;
    @BindView(R.id.password)
    RegularTextInputEditText password;
    @BindView(R.id.inputLayoutBusinessName)
    RegularTextInputLayout inputLayoutBusinessName;
    @BindView(R.id.inputLayoutOwnerName)
    RegularTextInputLayout inputLayoutOwnerName;
    @BindView(R.id.inputLayoutMobile)
    RegularTextInputLayout inputLayoutMobile;
    @BindView(R.id.inputLayoutEmail)
    RegularTextInputLayout inputLayoutEmail;
    @BindView(R.id.inputLayoutPassword)
    RegularTextInputLayout inputLayoutPassword;
    @BindView(R.id.termAndCondition)
    CheckBox termAndCondition;
    @BindView(R.id.submit)
    RegularButton submit;
    @BindView(R.id.login)
    RegularTextView login;

    private boolean termAndCOnditionIsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_vendor);
        ButterKnife.bind(this);

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        termAndCOnditionIsChecked = termAndCondition.isChecked();
        termAndCondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    termAndCOnditionIsChecked = isChecked;
                }
            }
        );


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
                if(inputLayoutBusinessName.isErrorEnabled()){
                    inputLayoutBusinessName.setErrorEnabled(false);
                }else if(inputLayoutOwnerName.isErrorEnabled()){
                    inputLayoutOwnerName.setErrorEnabled(false);
                }else if(inputLayoutMobile.isErrorEnabled()){
                    inputLayoutMobile.setErrorEnabled(false);
                }else if(inputLayoutEmail.isErrorEnabled()){
                    inputLayoutEmail.setErrorEnabled(false);
                }else if(inputLayoutPassword.isErrorEnabled()){
                    inputLayoutPassword.setErrorEnabled(false);
                }


            }
        };

        businessName.addTextChangedListener(afterTextChangedListener);
        ownerName.addTextChangedListener(afterTextChangedListener);
        mobile.addTextChangedListener(afterTextChangedListener);
        email.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isOnline(context)) {
                    if (checkValidation()) {
                        showDialog();
                        apiInterface.vendorRegister(ownerName.getText().toString(), email.getText().toString(), businessName.getText().toString(), mobile.getText().toString(), password.getText().toString()).enqueue(new Callback<ResponseRequest>() {
                            @Override
                            public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                                hideDialog();
                                Log.d(TAG,response.body().getMessage());
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    if(response.body().getMessage().equalsIgnoreCase("success")) {
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseRequest> call, Throwable t) {
                                Log.e(TAG,t.getMessage());
                                hideDialog();
                                Toast.makeText(context, "User registration failed", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } else {
                    Toast.makeText(context, "Please check your intenet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private boolean checkValidation() {
        boolean result = false;
        if (businessName.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutBusinessName.requestFocus();
            inputLayoutBusinessName.setErrorEnabled(true);
            inputLayoutBusinessName.setError("Please Enter Business Name");
            result = false;

        }else if (ownerName.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutOwnerName.requestFocus();
            inputLayoutOwnerName.setErrorEnabled(true);
            inputLayoutOwnerName.setError("Please Enter Owner Name");
            result = false;

        }else if (!mobile.getText().toString().matches("(0/91)?[6-9][0-9]{9}")){
            inputLayoutMobile.requestFocus();
            inputLayoutMobile.setErrorEnabled(true);
            inputLayoutMobile.setError("Please Enter Valid Mobile No.");
            result = false;

        } else if (!isEmailValid(email.getText().toString().trim())) {
            inputLayoutEmail.requestFocus();
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Please Enter Valid EmailID");
            result = false;

        }else if (password.getText().toString().equalsIgnoreCase("")){
            inputLayoutPassword.requestFocus();
            inputLayoutPassword.setErrorEnabled(true);
            inputLayoutPassword.setError("Please Enter Password");
            result = false;

        }
        else if (!termAndCOnditionIsChecked){
            Snackbar.make(mainConstraint, "Please Accept term & Condition", Snackbar.LENGTH_SHORT).show();
            result = false;

        }else {
            result = true;
        }
        return result;
    }
}