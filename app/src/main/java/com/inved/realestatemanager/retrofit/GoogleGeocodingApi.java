package com.inved.realestatemanager.retrofit;

import com.inved.realestatemanager.retrofit.modelPojo.Geocoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleGeocodingApi {

    @GET("geocode/json")
    Call<Geocoding> getLatLngWithAddress(
            @Query("address") String origins,
            @Query("key") String api_key
    );
}
