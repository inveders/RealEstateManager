package com.inved.realestatemanager.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CreateUpdatePropertyViewModel extends ViewModel {

    private MutableLiveData<List<Object>> data = new MutableLiveData<>();

    public void setMyData(List<Object> myData) {
        data.setValue(myData);
    }

    public LiveData<List<Object>> getMyData() {
        return data;
    }

}
