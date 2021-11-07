package com.business.findtrue.repositry;

import com.business.findtrue.model.Res;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
//    @Headers({"Content-Type:application/json",
//            "x-client-id:54709fd88ae1ff17a3811256f90745",
//            "x-client-secret:fa07c20143622f376ccb79eb7e3a545d2878b92b"})
//    @POST("cftoken/order")
//    Observable<Res> getToken(@Query("orderId") String orderId, @Query("orderAmount") String orderAmount, @Query("orderCurrency") String orderCurrency);

    @Headers({"Content-Type:application/json",
            "x-client-id:54709fd88ae1ff17a3811256f90745",
            "x-client-secret:fa07c20143622f376ccb79eb7e3a545d2878b92b"})
    @POST("cftoken/order")
    Call<Res> generateToken(@Body Map<String, String> body);
}
