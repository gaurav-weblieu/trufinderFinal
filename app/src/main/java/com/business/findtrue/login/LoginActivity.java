package com.business.findtrue.login;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.business.findtrue.R;
import com.business.findtrue.adapter.LoginAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.fragment.ServiceProviderFragment;
import com.business.findtrue.fragment.UserLoginFragment;

public class LoginActivity extends BaseActivity {

    //private TextView tvUserLogin, tvSeviceProvider;
    private TabLayout tab_layout;
    private ViewPager view_pager;
    LoginAdapter loginAdapter;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initID();

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, new UserLoginFragment(), null);
//        fragmentTransaction.commit();

    }


    private void initID(){
        tab_layout = (TabLayout)findViewById(R.id.tab_layout);
        view_pager = (ViewPager)findViewById(R.id.view_pager);

        loginAdapter = new LoginAdapter(getSupportFragmentManager());
        loginAdapter.addFragment(new UserLoginFragment(), "  User");
        loginAdapter.addFragment(new ServiceProviderFragment(), "Provider");

        view_pager.setAdapter(loginAdapter);
        tab_layout.setupWithViewPager(view_pager);


        //tab_layout.setTranslationY(300);
        //tab_layout.setAlpha(v);
//        tvUserLogin = (TextView)findViewById(R.id.tvUserLogin);
//        tvSeviceProvider = (TextView)findViewById(R.id.tvSeviceProvider);
        //initAction();
    }

//    private void initAction(){
//        tvUserLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new UserLoginFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, fragment, null);
//                fragmentTransaction.commit();
//                tvUserLogin.setTextColor(Color.WHITE);
//                tvSeviceProvider.setTextColor(Color.BLACK);
//            }
//        });
//
//        tvSeviceProvider.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new ServiceProviderFragment();
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mFrameLayout, fragment, null);
//                fragmentTransaction.commit();
//                tvSeviceProvider.setTextColor(Color.WHITE);
//                tvUserLogin.setTextColor(Color.BLACK);
//            }
//        });
//    }
}