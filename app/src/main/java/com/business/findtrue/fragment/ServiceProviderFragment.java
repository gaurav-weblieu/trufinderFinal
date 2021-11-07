package com.business.findtrue.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.business.findtrue.MainActivity;
import com.business.findtrue.R;
import com.business.findtrue.app.BaseFragment;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;
import com.business.findtrue.login.ForgotPasswordActivity;
import com.business.findtrue.login.LoginActivity;
import com.business.findtrue.model.LoginVendor;
import com.business.findtrue.model.VendorLogin;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;
import com.business.findtrue.vendor.NewAccountVendorActivity;
import com.business.findtrue.vendor.VendorMainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProviderFragment extends BaseFragment {

    private Unbinder unbinder;

    @BindView(R.id.username)
    RegularTextInputEditText editTextEmail;
    @BindView(R.id.password) RegularTextInputEditText editTextPassword;
    @BindView(R.id.inputLayoutUserName)
    RegularTextInputLayout inputLayoutUserName;
    @BindView(R.id.inputLayoutPassword) RegularTextInputLayout inputLayoutPassword;
    @BindView(R.id.login)
    RegularButton login;
    @BindView(R.id.signup)
    RegularTextView signup;
    @BindView(R.id.tvMayBeLater) RegularTextView tvMayBeLater;
    @BindView(R.id.forgetPassword) RegularTextView forgetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);



        LoginActivity loginActivity = new LoginActivity();

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
                if(inputLayoutUserName.isErrorEnabled()){
                    inputLayoutUserName.setErrorEnabled(false);
                }else if(inputLayoutPassword.isErrorEnabled()){
                    inputLayoutPassword.setErrorEnabled(false);
                }
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewAccountVendorActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                intent.putExtra("intentType",2);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
            }
        });

        editTextEmail.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);


        tvMayBeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
                getActivity().finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isOnline(getContext())){
                    if (checkValidation()){
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();
                        System.out.println("email------------------------"+email);
                        System.out.println("password------------------------"+password);

                        showDialog();
                        apiInterface.vendorLogin(email, password).enqueue(new Callback<VendorLogin>() {
                            @Override
                            public void onResponse(Call<VendorLogin> call, Response<VendorLogin> response) {
                                hideDialog();
                                //String message2 = response.body().getMessage();
                               Log.e("response",response.body().getMessage());
                                //System.out.println("messsage2----------------------------"+message2);
                                if (response.body().isStatus()==true){

                                    String message = response.body().getMessage();
                                    //System.out.println("type----------------------"+response.body().getData().getId());
                                    System.out.println("messsage----------------------------"+message);
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    LoginVendor result = response.body().getData();
                                    //for (Result result:results){
                                        System.out.println("id-------------------------"+result);

                                        CommonUtils.savePreferenceString(getContext(), AppConstant.VENDOR_ID, result.getUserId());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.VENDOR_NAME, result.getVendorName());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.OWNER_NAME, result.getVendorName());
                                        //CommonUtils.savePreferenceString(getContext(), AppConstant.CATEGORY_ID, result.getCategoryId());
                                        //CommonUtils.savePreferenceString(getContext(), AppConstant.SUB_CATEGORY_ID, result.getSubCategoryId());

                                        CommonUtils.savePreferenceString(getContext(), AppConstant.VENDOR_EMAIL_ID, result.getVendorEmail());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.VENDOR_CONTACT_NO, result.getContactno());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.TOKEN, result.getToken());
                                       // CommonUtils.savePreferenceString(getContext(), AppConstant.PROFILE_VERIFIED, result.getProfileVerifiedbyAdminid());
                                       // CommonUtils.savePreferenceString(getContext(), AppConstant.PROFILE_VERIFIED_TIME, result.getProfileVerifiedTime());

//                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_LOGGED_IN, "true");
//                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_USERS, "user");
//                                        Intent intent = new Intent(getContext(), MainActivity.class);
//                                        startActivity(intent);
//                                        getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
//                                        getActivity().finish();

                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_LOGGED_IN, "true");
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_USERS, "vendor");
                                        Intent intent = new Intent(getContext(), VendorMainActivity.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
                                        getActivity().finish();
                                    }
                                else {
                                    showErrorMessage("Oops", "Login Failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<VendorLogin> call, Throwable t) {
                                hideDialog();
                                Log.e("Error",t.getMessage());
                                showErrorMessage("Error", "Login Failed");

                            }
                        });
                    }

                }else {
                    Toast.makeText(getContext(), "Please check your intenet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkValidation(){
        boolean result = false;
        System.out.println("checkValidation-------------------------");
        if (!isEmailValid(editTextEmail.getText().toString())) {
            inputLayoutUserName.setErrorEnabled(true);
            inputLayoutUserName.setError(getString(R.string.enter_valid_email_id));
            result = false;

        }else if (editTextPassword.getText().toString().equalsIgnoreCase("")) {
            inputLayoutPassword.setErrorEnabled(true);
            inputLayoutPassword.setError(getString(R.string.enter_valid_email_id));
            result = false;
            result = false;
        }
        else {
            return true;
        }
        return result;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}