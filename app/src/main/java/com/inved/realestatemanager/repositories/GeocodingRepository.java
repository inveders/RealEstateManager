package com.inved.realestatemanager.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.retrofit.GoogleGeocodingApi;
import com.inved.realestatemanager.retrofit.RetrofitServiceGeocoding;
import com.inved.realestatemanager.retrofit.modelPojo.Geocoding;
import com.inved.realestatemanager.retrofit.modelPojo.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeocodingRepository {

    private List<Result> results = new ArrayList<>();
    private MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();
    private static final String MATRIX_API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY;


    public GeocodingRepository() {
    }

    public MutableLiveData<List<Result>> getMutableLiveData(String address) {

        GoogleGeocodingApi googleGeocodingApi = RetrofitServiceGeocoding.googleGeocodingApi();

        Call<Geocoding> call = googleGeocodingApi.getLatLngWithAddress(address, MATRIX_API_KEY);

        call.enqueue(new Callback<Geocoding>() {
            @Override
            public void onResponse(@NonNull Call<Geocoding> call, @NonNull Response<Geocoding> response) {

                if(response.body()!=null){
                    results=response.body().getResults();
                    mutableLiveData.setValue(results);
                }

            }

            @Override
            public void onFailure(@NonNull Call<Geocoding> call, @NonNull Throwable t) {
            }
        });


        return mutableLiveData;


    }
}
