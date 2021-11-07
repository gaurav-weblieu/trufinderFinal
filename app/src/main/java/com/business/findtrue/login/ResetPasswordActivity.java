package com.business.findtrue.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.business.findtrue.R;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.profile.UpdateProfile;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {



    String USER_ID;
    @BindView(R.id.newPassword)
    RegularTextInputEditText newPassword;
    @BindView(R.id.confirmPassword)
    RegularTextInputEditText confirmPassword;
    @BindView(R.id.inputLayoutNewPassword)
    RegularTextInputLayout inputLayoutNewPassword;
    @BindView(R.id.inputLayoutConfirmPassword)
    RegularTextInputLayout inputLayoutConfirmPassword;
    @BindView(R.id.submit)
    RegularButton submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);


        USER_ID = CommonUtils.getPreferencesString(ResetPasswordActivity.this, AppConstant.USER_ID);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    System.out.println("==============================validate");
                    apiInterface.updatePassword(USER_ID, confirmPassword.getText().toString()).enqueue(new Callback<UpdateProfile>() {
                        @Override
                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                            if (response.isSuccessful()){
                                System.out.println("----------------------------"+response.body().getMessage());
                                Toast.makeText(ResetPasswordActivity.this, "Password Change Successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateProfile> call, Throwable t) {
                        }
                    });

                }else {
                    System.out.println("==============================not validate");
                }
            }
        });

    }

    private boolean validate() {
        boolean temp=true;
        String pass=newPassword.getText().toString();
        String cpass=confirmPassword.getText().toString();
        if(!pass.equals(cpass)) {
            inputLayoutConfirmPassword.setErrorEnabled(true);
            inputLayoutConfirmPassword.setError(getString(R.string.password_not_match));
            temp = false;
        }
        return temp;
    }
}