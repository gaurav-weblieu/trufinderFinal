package com.business.findtrue.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cashfree.pg.CFPaymentService;
import com.business.findtrue.R;
import com.business.findtrue.model.Res;
import com.business.findtrue.repositry.Api;
import com.business.findtrue.repositry.RetrofitApiClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

public class PaymentActivity extends AppCompatActivity {

    private Button payment;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Api api;
    String stage = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        payment = (Button)findViewById(R.id.payment);
        //api = RetrofitApiClient.getInstance().create(Api.class);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiatepayment();
                generateToken();
                Random r = new Random();
                int randomNumber = r.nextInt(999999);
//                Map<String,String> dataSend = new HashMap<>();
//                dataSend.put("appId", "68164feaec506366cba686f6c46186");
//                dataSend.put("orderId", "Order0999");
//                dataSend.put("orderAmount", String.valueOf(100));
//                dataSend.put("customerPhone", "7011351424");
//                dataSend.put("customerEmail", "test@gmail.com");
//                String generatedToken = "bs9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.vwQfikzN4I2N3kzY2ETOwYjI6ICdsF2cfJCL1UjM1MzNyIjNxojIwhXZiwiIS5USiojI5NmblJnc1NkclRmcvJCLiADMxIiOiQnb19WbBJXZkJ3biwiIxADMwIXZkJ3TiojIklkclRmcvJye.qU3eGNoYaOjl0-4I8cNXF9FdHapEvofVhT-Z7tRoHXhJBQEf_Tv_0RtZt6XsENSACu";
                //CFPaymentService.getCFPaymentServiceInstance().doPayment(PaymentActivity.this, dataSend, generatedToken, "TEST", "#FFC71520", "#FFFFFF", false);
//                cfPaymentService.doPayment(PaymentActivity.this, getInputParams(), generatedToken, "TEST", "#784BD2", "#FFFFFF", false);
                //cfPaymentService.upiPayment(PaymentActivity.this, getInputParams(), generatedToken, stage);
            }
        });
    }

    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "68164feaec506366cba686f6c46186";
        String orderId = "Order0001";
        String orderAmount = "100";
        String orderNote = "Test Order";
        String customerName = "John Doe";
        String customerPhone = "9900012345";
        String customerEmail = "test@gmail.com";

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        System.out.println("TAG--------------------------------------API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        System.out.println("hderffff-------------------"+key + " : " + bundle.getString(key));
                    }
                }
        }
    }

    private void generateToken(){

        Random r = new Random();
        int randomNumber = r.nextInt(999999);
        Map<String,String> dataSend = new HashMap<>();
        dataSend.put("appId", "68164feaec506366cba686f6c46186");
        dataSend.put("orderId", "Order0001");
        dataSend.put("orderAmount", "1");
        dataSend.put("customerPhone", "7011351424");
        dataSend.put("customerEmail", "test@gmail.com");

        Map<String, String> map = new HashMap<>();
        map.put("orderId", "Order0001");
        map.put("orderAmount", "1");
        map.put("orderCurrency", "INR");
        Call<Res> call = RetrofitApiClient.getClient().create(Api.class).generateToken(map);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                Log.d("response",response.body().getMessage());
                Log.d("token",response.body().getCftoken());
                CFPaymentService.getCFPaymentServiceInstance().doPayment(PaymentActivity.this, getInputParams(), response.body().getCftoken(), "TEST", "#FFC71520", "#FFFFFF", false);
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.d("response",t.getMessage());
            }


        });
    }

    private void initiatepayment(){
        Random r = new Random();
        int randomNumber = r.nextInt(999999);
        Map<String,String> dataSend = new HashMap<>();
        dataSend.put("appId", "68164feaec506366cba686f6c46186");
        dataSend.put("orderId", String.valueOf(randomNumber));
        dataSend.put("orderAmount", String.valueOf(100));
        dataSend.put("customerPhone", "7065263139");
        dataSend.put("customerEmail", "test@gmail.com");

       // String orderid = "1234565567";
        String amount = "100";
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "Order0702");
        map.put("orderAmount", "1");
        map.put("orderCurrency", "INR");



//        compositeDisposable.add(api.getToken(String.valueOf(randomNumber), amount, "INR").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Res>() {
//            @Override
//            public void accept(Res res) throws Exception {
//                if (res.getStatus().equals("OK")){
//                    System.out.println("-----------------------------"+res.getMessage());
//                    CFPaymentService.getCFPaymentServiceInstance(                                                                                                                  ).doPayment(PaymentActivity.this, dataSend, res.getCftoken(), "TEST", "#F8A31A", "#FFFFFF", false);
//                }else {
//                    Log.e("accept",res.getMessage());
//                    Toast.makeText(PaymentActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                Log.e("error",throwable.getMessage());
//                Toast.makeText(PaymentActivity.this, ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        }));

    }
}

