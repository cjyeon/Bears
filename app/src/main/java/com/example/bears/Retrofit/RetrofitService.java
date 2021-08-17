package com.example.bears.Retrofit;

import com.example.bears.Model.LoginModel;
import com.example.bears.Model.BeaconModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("/drivers/login")
    Call<LoginModel> getLoginCheck(@Field("BusId") String BusID,
                                   @Field("BusPwd") String BusPwd);

    @FormUrlEncoded
    @POST("/user/boarding")
    Call<BeaconModel> NoticeBusStop(@Field("BusStopName") String BusStopName,
                                    @Field("vehId") String vehId);

}
