package com.business.findtrue.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.business.findtrue.model.Login;
import com.business.findtrue.model.Result;
import com.business.findtrue.registation.RegistationActivity;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserLoginFragment extends BaseFragment {
    private Unbinder unbinder;

    @BindView(R.id.username) RegularTextInputEditText editTextEmail;
    @BindView(R.id.password) RegularTextInputEditText editTextPassword;
    @BindView(R.id.inputLayoutUserName) RegularTextInputLayout inputLayoutUserName;
    @BindView(R.id.inputLayoutPassword) RegularTextInputLayout inputLayoutPassword;
    @BindView(R.id.login) RegularButton login;
    @BindView(R.id.signup) RegularTextView signup;
    @BindView(R.id.forgetPassword) RegularTextView forgetPassword;
    @BindView(R.id.tvMayBeLater) RegularTextView tvMayBeLater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_login, container, false);
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
                Intent intent = new Intent(getContext(), RegistationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                intent.putExtra("intentType",1);
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
                        apiInterface.userLogin(email, password).enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                hideDialog();
                                String message2 = response.body().getMessage();
                                System.out.println("type----------------------"+response.body().getType());
                                System.out.println("messsage2----------------------------"+message2);
                                if (response.isSuccessful()){
                                    String message = response.body().getMessage();
                                    System.out.println("type----------------------"+response.body().getType());
                                    System.out.println("messsage----------------------------"+message);
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    List<Result> results = response.body().getData();
                                    for (Result result:results){
                                        System.out.println("id-------------------------"+result.getId());
                                        System.out.println("fname-------------------------"+result.getFname());
                                        System.out.println("lname-------------------------"+result.getLname());
                                        System.out.println("email-------------------------"+result.getEmail());
                                        System.out.println("mobileNo-------------------------"+result.getMobileno());

                                        CommonUtils.savePreferenceString(getContext(), AppConstant.USER_ID, result.getId());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.F_NAME, result.getFname());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.L_NAME, result.getLname());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.EMAIL, result.getEmail());
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.MOBILE_NO, result.getMobileno());

                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_LOGGED_IN, "true");
                                        CommonUtils.savePreferenceString(getContext(), AppConstant.IS_USERS, "user");
                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
                                        getActivity().finish();
                                    }
                                }else {
                                    showErrorMessage("Oops", "Login Failed");
                                }
                            }
                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                hideDialog();
                                showErrorMessage("Oops", "Login Failed");
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