package com.example.bears.Retrofit;

import com.example.bears.Model.StationByPosModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("getStationByPos?")
    Call<StationByPosModel> getStationByPos(@Query("serviceKey") String serviceKey,
                                            @Query("tmX") String tmX,
                                            @Query("tmY") String tmY,
                                            @Query("radius") String radius);
}
