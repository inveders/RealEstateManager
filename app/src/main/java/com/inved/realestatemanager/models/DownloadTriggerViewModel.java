package com.inved.realestatemanager.models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.inved.realestatemanager.firebase.StorageHelper;

public class DownloadTriggerViewModel extends AndroidViewModel {

    //private MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
    private final StorageHelper storageHelper;
    private MutableLiveData<String> stringToSend;
    private LiveData<String> downloadFinished;

    public DownloadTriggerViewModel(@NonNull Application application) {
        super(application);

        Log.d("debago","View Model Initialization");

        stringToSend = new MutableLiveData<>();
        storageHelper = new StorageHelper();

        downloadFinished = Transformations.switchMap(
                stringToSend, input -> storageHelper.getEndOfDownload());
    }

    // -------------
    // FOR BEGGINING - RESULT DOWNLOAD
    // ------------



    public LiveData<String> getDownloadFinished() {
        Log.d("debago","download finished called in view model : "+downloadFinished.getValue()+" and does observers are active? "+downloadFinished.hasActiveObservers());
        return downloadFinished;
    }

    public void setDownloadFinished() {
        stringToSend.setValue("Ok");
    }


}
