package com.inved.realestatemanager.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.repositories.GeocodingRepository;
import com.inved.realestatemanager.retrofit.modelPojo.Result;

import java.util.List;

public class GeocodingViewModel extends AndroidViewModel {

    private GeocodingRepository geocodingRepository;



    public GeocodingViewModel(@NonNull Application application) {
        super(application);
        geocodingRepository = new GeocodingRepository();

    }

    // -------------
    // FOR RETROFIT
    // -------------

    public LiveData<List<Result>> getLatLngWithAddress(String address) {

        return geocodingRepository.getMutableLiveData(address);
    }

}
