package com.business.findtrue.vendor;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.business.findtrue.R;
import com.business.findtrue.adapter.PackageAdapter;
import com.business.findtrue.app.BaseActivity;
import com.business.findtrue.listner.PackageListner;
import com.business.findtrue.model.GetPackage;
import com.business.findtrue.model.PackageDetails;
import com.business.findtrue.model.ordertransactionResponseModel;
import com.business.findtrue.repositry.ApiInterface;
import com.business.findtrue.repositry.RetrofitClient;
import com.business.findtrue.utils.AppConstant;
import com.business.findtrue.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageActivity extends BaseActivity implements PaymentResultListener {

    private Context context = PackageActivity.this;
    private ImageView back;
    private RecyclerView recyclerPackage;
    private PackageAdapter packageAdapter;
    List<PackageDetails> listPackageDetails;
    private ApiInterface apiInterface;
    PackageDetails packageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        Checkout.preload(getApplicationContext());
        back = (ImageView) findViewById(R.id.back);
        recyclerPackage = (RecyclerView) findViewById(R.id.recyclerPackage);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerPackage.setLayoutManager(linearLayoutManager);
        listPackageDetails = new ArrayList<>();

        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        getPackage();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getPackage() {
        showDialog();
        apiInterface.getPackage().enqueue(new Callback<GetPackage>() {
            @Override
            public void onResponse(Call<GetPackage> call, Response<GetPackage> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    List<PackageDetails> results = response.body().getData();
                    listPackageDetails.addAll(results);
                    packageAdapter = new PackageAdapter(context, listPackageDetails, new PackageListner() {
                        @Override
                        public void packageClick(int position, PackageDetails packageDetail) {
                            makepayment(packageDetail);
                        }
                    });
                    recyclerPackage.setAdapter(packageAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetPackage> call, Throwable t) {
                hideDialog();
            }
        });

    }

    private void makepayment(PackageDetails packageDetails) {
        this.packageDetails = packageDetails;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_DYaCOsDQzuG2h7");

        checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", CommonUtils.getPreferencesString(context, AppConstant.VENDOR_NAME));
            options.put("description", packageDetails.getPackageName());
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#CD222C");
            options.put("currency", "INR");
            options.put("amount", 100 * (Integer.parseInt(packageDetails.getPrice())));//300 X 100
            options.put("prefill.email", CommonUtils.getPreferencesString(context, AppConstant.VENDOR_EMAIL_ID));
            options.put("prefill.contact", CommonUtils.getPreferencesString(context, AppConstant.VENDOR_CONTACT_NO));
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        String packageId = packageDetails.getPackageId();
        String vendorId = CommonUtils.getPreferencesString(this, AppConstant.VENDOR_ID);
        apiInterface.packageOrder(packageId, vendorId).enqueue(new Callback<ordertransactionResponseModel>() {
            @Override
            public void onResponse(Call<ordertransactionResponseModel> call, Response<ordertransactionResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getType().equalsIgnoreCase("success")) {
                        hideDialog();
                        showSuccessMessage(response.body().getType(),"Your package is activate soon.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ordertransactionResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}