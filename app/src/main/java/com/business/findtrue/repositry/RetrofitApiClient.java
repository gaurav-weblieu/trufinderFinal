package com.business.findtrue.repositry;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
//    private static Retrofit instance;
//
//    public static Retrofit getInstance() {
//
//        return instance == null ? instance = new Retrofit.Builder()
//                .baseUrl("https://test.cashfree.com/api/v2/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build() : instance;
//
//    }

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://test.cashfree.com/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
