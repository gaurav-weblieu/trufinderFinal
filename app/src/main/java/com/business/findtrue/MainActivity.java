package com.business.findtrue;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marcoscg.dialogsheet.DialogSheet;
import com.business.findtrue.adapter.BannerImageAdapter;
import com.business.findtrue.adapter.CategoryAdapter;
import com.business.findtrue.adapter.HomeViewTypeAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.custom.RegularButton;
import com.business.findtrue.custom.RegularTextInputEditText;
import com.business.findtrue.custom.RegularTextInputLayout;
import com.business.findtrue.custom.RegularTextView;

import com.business.findtrue.listner.ProductListner;
import com.business.findtrue.login.LoginActivity;
import com.business.findtrue.model.AllAdsBannerResponseModel;
import com.business.findtrue.model.Category;
import com.business.findtrue.model.CityList;
import com.business.findtrue.model.CityModel;
import com.business.findtrue.model.UpdateDeviceToken;
import com.business.findtrue.notification.NotificationActivity;
import com.business.findtrue.profile.GetProfile;
import com.business.findtrue.profile.LoginData;
import com.business.findtrue.profile.ProfileActivity;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.user.AllCategoryActivity;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import de.mrapp.android.dialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context context = MainActivity.this;
    Menu menu;
    AlertDialog materialAlertDialogBuilder;
    private SpinnerCityAdapter spinnerCityAdapter;
    MaterialDialog.Builder builder;
    private AppCompatButton sendEnquiry;
    private DrawerLayout drawerLayout;
    private NavigationView navitationView;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView, rvBannerImage, rvAdsBannerImage;
    private HomeViewTypeAdapter mHomeViewTypeAdapter;
    List<Category> categoryItemList = null;
    List<CityList> cityLists = new ArrayList<>();
    private ApiInterface apiInterface;

    //SliderLayout imageSlider;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    public static TextView tvLocation;
    private ImageView ivArrowDown,bannerImage,bottomBannerImageOne,bottomBannerImageTwo,bottomBannerImageThree;
    private TextView editTextTextPersonName;
    private CircleImageView ivProfileImage;
    private TextView tvProfileImage, tvUserEmail;
    private RegularTextView showMore;
    private RelativeLayout relativeShowMore;
    private boolean expandOrNot = false;
    String USER_ID;
    Geocoder geocoder;

    int mYear, mMonth, mDay, mHour, mMinute;
    String vndrbox, category_id, city_id, areapinid, dateTime;
    RegularTextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutMobile, inputLayoutBookingDate;
    RegularTextInputEditText name, email, mobile, bookingDate;
    ImageView notification;

    ShimmerFrameLayout banner_shimmerFrameLayout,cat_shimmerFrameLayout,add_shimmerFrameLayout;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

                        // Get new FCM registration token
                        String token = task.getResult();
                        updateDeviceToken(token);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        //imageSlider.setImageList(imageList);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navitationView = (NavigationView) findViewById(R.id.navitationView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sendEnquiry = (AppCompatButton) findViewById(R.id.sendEnquiry);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        tvLocation = findViewById(R.id.tvLocation);
        mRecyclerView.setNestedScrollingEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ivArrowDown = (ImageView) findViewById(R.id.ivArrowDown);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        showMore = findViewById(R.id.showMore);
        relativeShowMore = findViewById(R.id.relativeShowMore);
        notification = findViewById(R.id.iv_notification);
        bannerImage = findViewById(R.id.bannerImage);
        bottomBannerImageOne = findViewById(R.id.bottomBannerImageOne);
        bottomBannerImageTwo = findViewById(R.id.bottomBannerImageTwo);
        bottomBannerImageThree = findViewById(R.id.bottomBannerImageThree);
        rvBannerImage = findViewById(R.id.rvBannerImage);
        rvAdsBannerImage = findViewById(R.id.rvAdsBannerImage);
        banner_shimmerFrameLayout = findViewById(R.id.banner_shimmerFrameLayout);
        cat_shimmerFrameLayout = findViewById(R.id.cat_shimmerFrameLayout);
        add_shimmerFrameLayout = findViewById(R.id.add_shimmerFrameLayout);
        banner_shimmerFrameLayout.startShimmerAnimation();
        cat_shimmerFrameLayout.startShimmerAnimation();
        add_shimmerFrameLayout.startShimmerAnimation();



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        /*-------------------------Toolbar----------------------------------*/
        //setSupportActionBar(toolbar);

        navitationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navitation_drawer_open, R.string.navitation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_baseline_format_align_left_24);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);


        navitationView.setNavigationItemSelectedListener(this);

        MenuItem logout = navitationView.getMenu().findItem(R.id.nav_logout);
        if (CommonUtils.getPreferencesString(context, AppConstant.IS_LOGGED_IN) == "") {
            logout.setTitle("Login");
            notification.setVisibility(View.INVISIBLE);
        } else {
            logout.setTitle("Logout");
            notification.setVisibility(View.VISIBLE);
        }

//        imageSlider = (SliderLayout) findViewById(R.id.imageSlider);
//        imageSlider.setPictureIndex(0);
//        List<Object> listJingDong = new ArrayList<>();
//        listJingDong.add(R.drawable.truefind_banner6);
//        listJingDong.add(R.drawable.truefind_banner7);
//        listJingDong.add(R.drawable.truefind_banner8);
//        imageSlider.setList(listJingDong);
        getLocalIpAddress();
        getPublicIPAddress(this);

        categoryItemList = new ArrayList<>();
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        rvBannerImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        LinearLayoutManager bannerLinnerView = new LinearLayoutManager(this);
        rvAdsBannerImage.setLayoutManager(bannerLinnerView);



        USER_ID = CommonUtils.getPreferencesString(context, AppConstant.USER_ID);
        System.out.println("USER_ID================================" + USER_ID);


        View headerLayout = navitationView.inflateHeaderView(R.layout.header);
        tvUserEmail = (TextView) headerLayout.findViewById(R.id.tvUserEmail);
        ivProfileImage = (CircleImageView) headerLayout.findViewById(R.id.ivProfileImage);
        tvProfileImage = (TextView) headerLayout.findViewById(R.id.tvProfileImage);

        getProfile();
        callCityList();
        callCategotyList();
        editTextTextPersonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        //check condition user login and not
        String isLoggedIn = CommonUtils.getPreferencesString(MainActivity.this, AppConstant.IS_LOGGED_IN);
        if (isLoggedIn.equals("true")) {
            sendEnquiry.setVisibility(View.INVISIBLE);
        } else {
            sendEnquiry.setVisibility(View.VISIBLE);
        }

        sendEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllCategoryActivity.class);
                startActivity(intent);

            }
        });


        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityList();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            }
        });

        callBannerImageAPI();
        callAdsBannerImageAPI();


    }

    private void callBannerImageAPI(){
        apiInterface.getAllbanner().enqueue(new Callback<AllAdsBannerResponseModel>() {
            @Override
            public void onResponse(Call<AllAdsBannerResponseModel> call, Response<AllAdsBannerResponseModel> response) {
                hideDialog();
                if (response.isSuccessful()){
                    Glide.with(MainActivity.this)
                            .load(response.body().getImagePath()+"/"+ response.body().getData().get(0).getBannerImage())
                            .into(bannerImage);
                    BannerImageAdapter bannerImageAdapter = new BannerImageAdapter(MainActivity.this,response.body().getImagePath(), response.body().getData());
                    rvBannerImage.setAdapter(bannerImageAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsBannerResponseModel> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void callAdsBannerImageAPI(){
        apiInterface.getAlladdsbanner().enqueue(new Callback<AllAdsBannerResponseModel>() {
            @Override
            public void onResponse(Call<AllAdsBannerResponseModel> call, Response<AllAdsBannerResponseModel> response) {
                hideDialog();
                if (response.isSuccessful()){
                    Glide.with(MainActivity.this)
                            .load(response.body().getImagePath()+"/"+ response.body().getData().get(0).getBannerImage())
                            .into(bottomBannerImageOne);
                    Glide.with(MainActivity.this)
                            .load(response.body().getImagePath()+"/"+ response.body().getData().get(1).getBannerImage())
                            .into(bottomBannerImageTwo);
                    Glide.with(MainActivity.this)
                            .load(response.body().getImagePath()+"/"+ response.body().getData().get(2).getBannerImage())
                            .into(bottomBannerImageThree);

                    BannerImageAdapter bannerImageAdapter = new BannerImageAdapter(MainActivity.this,response.body().getImagePath(), response.body().getData());
                    rvAdsBannerImage.setAdapter(bannerImageAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllAdsBannerResponseModel> call, Throwable t) {
                hideDialog();
            }
        });
    }

    private void updateDeviceToken(String deviceId){
        String user_id = CommonUtils.getPreferencesString(this, AppConstant.USER_ID);
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

    private void callCityList() {
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.getCity().enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.isSuccessful()) {
                    System.out.println("type=========------------------------" + response.body().getType());
                    List<CityList> models = response.body().getData();
                    for (CityList list : models) {
                        cityLists.add(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "city list not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCityList() {
        builder = new MaterialDialog.Builder(MainActivity.this);
        builder.setView(R.layout.change_city_dialog);
        builder.show();
        mRecyclerView = (RecyclerView) builder.create().findViewById(R.id.recyclerViewCityName);
        ProgressBar mProgressBar = (ProgressBar) builder.create().findViewById(R.id.mProgressBar);
        EditText editSearch = (EditText) builder.create().findViewById(R.id.editSearch);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setHasFixedSize(true);
        ImageView imageClose = (ImageView) builder.create().findViewById(R.id.imageViewClose);

        spinnerCityAdapter = new SpinnerCityAdapter(MainActivity.this, cityLists);
        if (cityLists.size() > 0) {
            spinnerCityAdapter = new SpinnerCityAdapter(MainActivity.this, cityLists);
            mRecyclerView.setAdapter(spinnerCityAdapter);
        }

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.create().dismiss();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerCityAdapter.getFilter().filter(s);
            }
        });
    }

    private void callCategotyList() {
        apiInterface.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    categoryItemList = response.body();
                    if (categoryItemList != null) {

                        relativeShowMore.setVisibility(View.VISIBLE);

                        rvBannerImage.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        rvAdsBannerImage.setVisibility(View.VISIBLE);


                        banner_shimmerFrameLayout.stopShimmerAnimation();
                        banner_shimmerFrameLayout.setVisibility(View.GONE);

                        cat_shimmerFrameLayout.stopShimmerAnimation();
                        cat_shimmerFrameLayout.setVisibility(View.GONE);

                        add_shimmerFrameLayout.stopShimmerAnimation();
                        add_shimmerFrameLayout.setVisibility(View.GONE);




                        CategoryAdapter categoryAdapter = new CategoryAdapter(MainActivity.this, categoryItemList, false, new ProductListner() {
                            @Override
                            public void productClick(int position, @NonNull Category category) {
                                boolean isMatch = false;
                                CityList currentCity = null;
                                for (CityList city : cityLists) {
                                    if (city.getCity().equalsIgnoreCase(AppConstant.CITY_NAME) || city.getCity_alias().equalsIgnoreCase(AppConstant.CITY_NAME)) {
                                        Log.e("city_name", city.getCity() + " : " + city.getCity_alias() + ": " + AppConstant.CITY_NAME);
                                        currentCity = city;
                                        isMatch = true;
                                    }
                                }
                                if (isMatch) {
                                    Intent intent = new Intent(MainActivity.this, CategoryDetailsActivity.class);
                                    intent.putExtra("CATEGORY_NAME", category.getId());
                                    intent.putExtra("CITY_NAME", currentCity.getCity());
                                    startActivity(intent);
                                }
                                else showCityList();


                            }
                        });
                        mRecyclerView.setAdapter(categoryAdapter);

                        //System.out.println("category+++++--------------------------" + categoryItemList);
                    } else {
                        relativeShowMore.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                relativeShowMore.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sendEnquiry() {
        DialogSheet dialogSheet = new DialogSheet(this, true);
        View view = View.inflate(this, R.layout.fragment_enquiry_dialog, null);
        dialogSheet.setView(view);
        dialogSheet.show();
        //ImageView btnClose = view.findViewById(R.id.btnClose);
        RegularButton btnSubmit = view.findViewById(R.id.submit);
        ImageView ivClose = (ImageView) view.findViewById(R.id.ivClose);
        inputLayoutName = view.findViewById(R.id.inputLayoutName);
        inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        inputLayoutMobile = view.findViewById(R.id.inputLayoutMobile);
        inputLayoutBookingDate = view.findViewById(R.id.inputLayoutBookingDate);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        bookingDate = view.findViewById(R.id.bookingDate);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.whatsupcheckBox);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSheet.dismiss();
            }
        });


        bookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                bookingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                //dateTime = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                System.out.println("dateTime----------------------" + dateTime);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    checked = "1";
//                    System.out.println("checked---------------------"+checked);
//                }else {
//                    //boolean check = false;
//                    checked = "0";
//                    System.out.println("checked---------------------"+checked);
//                }
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (validationCheck()){
                    String id = categoryDataList.get(position).getId();
                    String city_id = categoryDataList.get(position).getCityId();
                    String areapinid = categoryDataList.get(position).getPincode();
                    apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
                    SendEnquiry sendEnquiry = new SendEnquiry();
                    sendEnquiry.setVndrbox(id);
                    sendEnquiry.setCategory_id(category_id);
                    sendEnquiry.setCity_id(city_id);
                    sendEnquiry.setAreapinid(areapinid);
                    sendEnquiry.setName(name.getText().toString());
                    sendEnquiry.setEmail(email.getText().toString());
                    sendEnquiry.setMobile_country_code(91);
                    sendEnquiry.setContactno(mobile.getText().toString());
                    sendEnquiry.setBookingdate(dateTime);
                    sendEnquiry.setWhatsapp_message_status(checked);
                    sendEnquiry.setIpaddress(AppConstant.PUBLIC_IP_ADDRESS);

                    //System.out.println("output------------------"+sendEnquiry);
                    String country_code = "91";

                    apiInterface.sendEnquiry(vndrbox, category_id, city_id, areapinid, name.getText().toString(), email.getText().toString(), country_code, mobile.getText().toString(), dateTime, checked, AppConstant.PUBLIC_IP_ADDRESS).enqueue(new Callback<ResponseRequest>() {
                        @Override
                        public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {
                            if (response.isSuccessful()){
                                System.out.println("-------------------------------------"+response.body().getMessage());
                                Toast.makeText(mContext, "Enquiry send successfully", Toast.LENGTH_LONG).show();
                                dialogSheet.dismiss();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseRequest> call, Throwable t) {
                            Toast.makeText(mContext, "Enquiry failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }*/
                //dialogSheet.dismiss();
            }
        });
        //Navigation.createNavigateOnClickListener(R.id.action_categoryDetailsFragment_to_enquiryDialogFragment).onClick(holder.itemView);
    }

    private boolean validationCheck() {
        boolean result = false;
        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutName.setErrorEnabled(true);
            inputLayoutName.setError("Please enter your name");
            //Toast.makeText(mContext, "Please enter your name", Toast.LENGTH_LONG).show();
            result = false;

        } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutEmail.setErrorEnabled(true);
            inputLayoutEmail.setError("Please enter your email");
            result = false;

        } else if (mobile.getText().toString().trim().equalsIgnoreCase("")) {
            inputLayoutMobile.setErrorEnabled(true);
            inputLayoutMobile.setError("Please enter your mobile number");
            result = false;

        } else if (bookingDate.getText().toString().equalsIgnoreCase("")) {
            inputLayoutBookingDate.setErrorEnabled(true);
            inputLayoutBookingDate.setError("Please enter your mobile number");
            result = false;
        } else {

            return true;
        }
        return result;
    }


    private void getProfile() {
        apiInterface.getUserProfile(USER_ID).enqueue(new Callback<GetProfile>() {
            @Override
            public void onResponse(Call<GetProfile> call, Response<GetProfile> response) {
                if (response.isSuccessful()) {
                    //System.out.println("message========================="+response.body().getMessage());

                    List<LoginData> loginData = response.body().getData();
                    for (LoginData login : loginData) {
                        System.out.println("fname============================" + login.getFname());
                        tvProfileImage.setText(login.getFname() + " " + login.getLname());
                        tvUserEmail.setVisibility(View.VISIBLE);
                        tvUserEmail.setText(login.getEmail());
                        Glide.with(context).load(login.getNewPofileImage()).centerCrop().placeholder(R.drawable.user_placeholder).into(ivProfileImage);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfile> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.add_to_shortlist:
                if (CommonUtils.getPreferencesString(context, AppConstant.IS_LOGGED_IN) == "") {
                    showErrorMessage(getString(R.string.oops), getString(R.string.login_first));
                } else {
                    Intent intentFavorite = new Intent(context, AddToShortListActivity.class);
                    startActivity(intentFavorite);
                }

                break;

            case R.id.list_business:
                Intent intent2 = new Intent(MainActivity.this, FreeListeningActivity.class);
                startActivity(intent2);
                break;

           /* case R.id.nav_hiring:
                //Intent intent3 = new Intent(MainActivity.this, PaymentActivity.class);
                Intent intent3 = new Intent(MainActivity.this, HiringActivity.class);
                startActivity(intent3);
                break;*/

            case R.id.nav_login:
                break;

            case R.id.nav_profile:
                if (CommonUtils.getPreferencesString(context, AppConstant.IS_LOGGED_IN) == "") {
                    showErrorMessage(getString(R.string.oops), getString(R.string.login_first));
                } else {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                break;

            /*case R.id.forgetPassword:
                if(CommonUtils.getPreferencesString(context,AppConstant.IS_LOGGED_IN)==""){
                    showErrorMessage(getString(R.string.oops),getString(R.string.login_first));
                }else {
                    Intent intent1 = new Intent(context, ResetPasswordActivity.class);
                    startActivity(intent1);
                }

                break;*/

            case R.id.nav_logout:
//                final FlatDialog flatDialog = new FlatDialog(context);
//                flatDialog.setTitle("Logout")
//                        .setSubtitle("Are you sure you want to end the session")
//                        .setFirstButtonText("YES")
//                        .setSecondButtonText("NO")
//                        .withFirstButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                CommonUtils.savePreferenceString(context, AppConstant.IS_LOGGED_IN, "false");
//                                startNewActvity(MainActivity.this,LoginActivity.class);
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
//                      .setFirstTextFieldHint("email")
                //.setSecondTextFieldHint("password")
//                DialogSheet dialogSheet = new DialogSheet(context, true);
//                View view = View.inflate(context, R.layout.logout_dialog, null);
//                dialogSheet.setView(view);
//                dialogSheet.show();

                if (CommonUtils.getPreferencesString(context, AppConstant.IS_LOGGED_IN) == "") {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                } else {
                    logout();
                }


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

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
                    CommonUtils.clearPreferenceString(MainActivity.this);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    //get location
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            System.out.println("latitude-----------------" + location.getLatitude());
                            System.out.println("longitute-----------------" + location.getLongitude());
                            String latlong = location.getLatitude() + "," + location.getLongitude();
                            System.out.println("latlong 1-------------------" + latlong);


                            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String current_address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                                String city = addresses.get(0).getSubAdminArea();
                                if(city.equals(""))
                                    city = addresses.get(0).getAdminArea();
                                System.out.println("city--------------------------" + city);
                                tvLocation.setText(city);
                                String str = city.trim();
                                //String cityName = str.substring(0, 1).toLowerCase() + str.substring(1);
                                String cityName = str.replaceAll(" ", "-").toLowerCase();
                                System.out.println("city--------------------------" + cityName);
                                AppConstant.CITY_NAME = cityName;

                                // System.out.println("city_name-------------------"+AppConstant.CITY_NAME);

                            } catch (IOException e) {

                            }
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            System.out.println("mLatitude-----------------------" + mLastLocation.getLatitude());
            System.out.println("mLongitude-----------------------" + mLastLocation.getLongitude());
            //latTextView.setText(mLastLocation.getLatitude()+"");
            //lonTextView.setText(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        System.out.println("ipaddress------------------------------" + ip);
                        Log.i(TAG, "***** IP=" + inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();

                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //get public ip address create function
    public static String getPublicIPAddress(Context context) {
        //final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if ((info != null && info.isAvailable()) && (info.isConnected())) {
                    StringBuilder response = new StringBuilder();

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) (
                                new URL("http://checkip.amazonaws.com/").openConnection());
                        urlConnection.setRequestProperty("User-Agent", "Android-device");
                        //urlConnection.setRequestProperty("Connection", "close");
                        urlConnection.setReadTimeout(15000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-type", "application/json");
                        urlConnection.connect();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }

                        }
                        urlConnection.disconnect();
                        String ipaddress = response.toString();
                        System.out.println("ipaddress-----------------------" + ipaddress);
                        AppConstant.PUBLIC_IP_ADDRESS = ipaddress;
                        return response.toString();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.w(TAG, "No network available INTERNET OFF!");
                    return null;
                }
                return null;
            }
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


    public class SpinnerCityAdapter extends RecyclerView.Adapter<SpinnerCityAdapter.ViewHolder> implements Filterable {
        Context context;
        List<CityList> cityLists;
        private List<CityList> cityListFull;

        public SpinnerCityAdapter(Context context, List<CityList> cityLists) {
            this.context = context;
            this.cityLists = cityLists;
            cityListFull = new ArrayList<>(cityLists);
        }

        @NonNull
        @Override
        public SpinnerCityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.city_list, parent, false);
            SpinnerCityAdapter.ViewHolder viewHolder = new SpinnerCityAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SpinnerCityAdapter.ViewHolder holder, int position) {
            CityList cityList = cityLists.get(position);
            System.out.println("city_name-------------------------" + cityList.getCity());
            holder.tvCityName.setText(cityList.getCity());

            //city name in on click event
            holder.tvCityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, cityList.getCity_alias(), Toast.LENGTH_LONG).show();
                    builder.create().dismiss();
                    AppConstant.CITY_NAME = cityList.getCity_alias();
                    AppConstant.SEARCH_CITY_NAME = cityList.getCity_alias();
                    tvLocation.setText(cityList.getCity());
                    //System.out.println("AppCityName------------------"+AppConstant.CITY_NAME);


                }
            });
        }

        @Override
        public int getItemCount() {
            return cityLists.size();
        }

        @Override
        public Filter getFilter() {
            return cityNameFilter;
        }

        private Filter cityNameFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<CityList> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(cityListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (CityList item : cityListFull) {
                        if (item.getCity().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cityLists.clear();
                cityLists.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvCityName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCityName = (TextView) itemView.findViewById(R.id.tvCityName);
            }
        }
    }
}