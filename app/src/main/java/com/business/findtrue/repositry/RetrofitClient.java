package com.business.findtrue.repositry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.business.findtrue.service.AppConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient.Builder okClient = new OkHttpClient.Builder();
        okClient.connectTimeout(60000, TimeUnit.MILLISECONDS);
        okClient.writeTimeout(60000, TimeUnit.MILLISECONDS);
        okClient.readTimeout(60000, TimeUnit.MILLISECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okClient.interceptors().add(interceptor);

        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                response.newBuilder()
                        .header("Cache-Control", "only-if-cached")
                        .build();
                return response;
            }
        });
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okClient.build())
                    .build();
        }
        return retrofit;
    }
}
