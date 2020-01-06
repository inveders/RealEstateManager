package com.inved.realestatemanager.models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.inved.realestatemanager.firebase.StorageDownload;

public class DownloadTriggerViewModel extends AndroidViewModel {

    private MediatorLiveData<String> mediatorLiveData = new MediatorLiveData<>();
    private final StorageDownload storageDownload=new StorageDownload();
    private LiveData<String> downloadFinished;
    private MutableLiveData<String> stringToSend = new MutableLiveData<>();

    public DownloadTriggerViewModel(@NonNull Application application) {
        super(application);

        Log.d("debago","View Model Initialization");

        /*downloadFinished = Transformations.switchMap(
                stringToSend, input -> storageDownload.getEndOfDownload());*/
        mediatorLiveData.addSource(storageDownload.getEndOfDownload(),value-> mediatorLiveData.setValue(value));

    }

    // -------------
    // FOR BEGGINING - RESULT DOWNLOAD
    // ------------

    public LiveData<String> getDownloadFinished() {
        Log.d("debago","download finished called in view model : "+mediatorLiveData.getValue()+" and storage download? "+storageDownload.getEndOfDownload().getValue());
        return mediatorLiveData;
    }


}
