package com.business.findtrue.registation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.model.UserRegistration;
import com.business.findtrue.model.UserRegistrationResponseModel;
import com.business.findtrue.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistationActivity extends BaseActivity {

    String gender ="";

    private TextView btnSave;
    @BindView(R.id.firstname)
    RegularTextInputEditText firstname;
    @BindView(R.id.lastname)
    RegularTextInputEditText lastname;
    @BindView(R.id.mobile)
    RegularTextInputEditText mobile;
    @BindView(R.id.email)
    RegularTextInputEditText email;
    @BindView(R.id.gender)
    AutoCompleteTextView genderDD;
    @BindView(R.id.password)
    RegularTextInputEditText password;
    @BindView(R.id.inputLayoutFirstName)
    RegularTextInputLayout inputLayoutFirstName;
    @BindView(R.id.inputLayoutLastName)
    RegularTextInputLayout inputLayoutLastName;
    @BindView(R.id.inputLayoutMobile)
    RegularTextInputLayout inputLayoutMobile;
    @BindView(R.id.inputLayoutEmail)
    RegularTextInputLayout inputLayoutEmail;
    @BindView(R.id.inputLayoutGender)
    RegularTextInputLayout inputLayoutGender;
    @BindView(R.id.inputLayoutPassword)
    RegularTextInputLayout inputLayoutPassword;
    @BindView(R.id.submit)
    RegularButton submit;
    @BindView(R.id.login)
    RegularTextView login;

    ArrayList<String> genderList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        ButterKnife.bind(this);
        initID();



        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,genderList);
        genderDD.setAdapter(arrayAdapter);
        genderDD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(RegistationActivity.this, ""+i+genderList.get(i), Toast.LENGTH_SHORT).show();
                gender = genderList.get(i);
                if(inputLayoutGender.isErrorEnabled()){
                    inputLayoutGender.setErrorEnabled(false);
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initID(){




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
                if(inputLayoutFirstName.isErrorEnabled()){
                    inputLayoutFirstName.setErrorEnabled(false);
                }else if(inputLayoutLastName.isErrorEnabled()){
                    inputLayoutLastName.setErrorEnabled(false);
                }else if(inputLayoutMobile.isErrorEnabled()){
                    inputLayoutMobile.setErrorEnabled(false);
                }else if(inputLayoutEmail.isErrorEnabled()){
                    inputLayoutEmail.setErrorEnabled(false);
                }else if(inputLayoutPassword.isErrorEnabled()){
                    inputLayoutPassword.setErrorEnabled(false);
                }


            }
        };

        firstname.addTextChangedListener(afterTextChangedListener);
        lastname.addTextChangedListener(afterTextChangedListener);
        mobile.addTextChangedListener(afterTextChangedListener);
        email.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,gender);
                if (CommonUtils.isOnline(RegistationActivity.this)){
                    if (checkValidation()){
                    UserRegistration userRegistration = new UserRegistration();
                    userRegistration.setFname(firstname.getText().toString());
                    userRegistration.setLname(lastname.getText().toString());
                    userRegistration.setMobileno(mobile.getText().toString());
                    userRegistration.setEmail(email.getText().toString());
                    userRegistration.setPassword(password.getText().toString());
                    userRegistration.setGender(gender);

                        //email.getText().toString(),firstname.getText().toString(),gender,lastname.getText().toString(),mobile.getText().toString()
                    apiInterface.userRegistration(email.getText().toString(),firstname.getText().toString(),gender,lastname.getText().toString(),mobile.getText().toString(),password.getText().toString()).enqueue(new Callback<UserRegistrationResponseModel>() {
                        @Override
                        public void onResponse(Call<UserRegistrationResponseModel> call, Response<UserRegistrationResponseModel> response) {
                            if (response.isSuccessful()){
                                System.out.println("response----------------------");
                                Toast.makeText(RegistationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                if(response.body().getError().equalsIgnoreCase("0")) {
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserRegistrationResponseModel> call, Throwable t) {
                            Toast.makeText(RegistationActivity.this, "User registration failed", Toast.LENGTH_LONG).show();
                        }
                    });

                    }
                }else {
                    Toast.makeText(RegistationActivity.this, "Please check your intenet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkValidation(){
        boolean result = false;
        if (firstname.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutFirstName.requestFocus();
            inputLayoutFirstName.setErrorEnabled(true);
            inputLayoutFirstName.setError("Please Enter First Name");
            result = false;

        }else if (lastname.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutLastName.requestFocus();
            inputLayoutLastName.setErrorEnabled(true);
            inputLayoutLastName.setError("Please Enter Last Name");
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

        }else if (gender==""){
            inputLayoutGender.requestFocus();
            inputLayoutGender.setErrorEnabled(true);
            inputLayoutGender.setError("Please Select Gender");
            result = false;

        }else if (password.getText().toString().equalsIgnoreCase("")){
            inputLayoutPassword.requestFocus();
            inputLayoutPassword.setErrorEnabled(true);
            inputLayoutPassword.setError("Please Enter Password");
            result = false;

        } else {
            result = true;
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(RegistationActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
    }
}