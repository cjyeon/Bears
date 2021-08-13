package com.example.bears.Retrofit;

import com.example.bears.Model.LoginModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("/drivers/login")
    Call<LoginModel> getLoginCheck(@Field("BusId") String BusID,
                                   @Field("BusPwd") String BusPwd);

    @FormUrlEncoded
    @POST("/user/boarding")
    Call<LoginModel> NoticeBusStop(@Field("arsId") String arsId,
                                   @Field("vehId") String vehId);

}
