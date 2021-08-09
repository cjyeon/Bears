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
    Call<LoginModel> getLoginCheck(@Field("BusID") String BusID,
                                   @Field("BusPwd") String BusPwd);


    @POST("/")
    Call<LoginModel> NoticeBusStop(@Field("busnumber") String busnumber,
                                   @Field("id") String arsId,
                                   @Field("password") String busRoutedId);

}
