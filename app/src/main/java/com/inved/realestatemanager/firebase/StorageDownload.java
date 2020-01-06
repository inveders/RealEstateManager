package com.inved.realestatemanager.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.controller.activity.AgentManagementActivity;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.RandomString;

import java.io.File;

public class StorageDownload {

    private OnDownloadFinishedInterface callback;

 //   MutableLiveData<String> result=new MutableLiveData<>();

    public void setCallback(OnDownloadFinishedInterface callback){
        this.callback = callback;
    }


    public interface OnDownloadFinishedInterface {
        void onDownloadFinished();
    }

    public String beginDownload(String getLastPathFromFirebase, String documentId) {
        Log.d("debago", "BEGIN DOWNLOAD");
        StorageReference fileReference = FirebaseStorage.getInstance().getReference(documentId).child("Pictures")
                .child(getLastPathFromFirebase);

        String mFileName = "/" + getLastPathFromFirebase;
        File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File localFile = File.createTempFile(mFileName, ".jpg", storageDir);
        File localFile = new File(storageDir + mFileName);
        // Save a file: path for using again
        String filePath = "file://" + storageDir + mFileName;

        if (!localFile.exists()) {
            // Log.d("debago", "file doesn't exist we download it "+localFile.exists());
            fileReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {

                if(callback!=null){
                    Log.d("debago", ";local tem file created  created " + callback);
                    Intent intent = new Intent (MainApplication.getInstance().getApplicationContext(), ListPropertyActivity.class);
                    MainApplication.getInstance().getApplicationContext().startActivity(intent);
                  //  callback.onDownloadFinished();
                }else{
                    Log.d("debago", "callback is null");
                }

                 // getEndOfDownload();
                //  updateDb(timestamp,localFile.toString(),position);
            }).addOnFailureListener(exception -> Log.d("debago", ";local tem file not created  created " + exception.toString()));
        }

        else {
            Log.d("debago", "file already exist");
        }

        return filePath;

    }


  /*  public MutableLiveData<String> getEndOfDownload(){

        RandomString randomString = new RandomString();
        result.postValue(randomString.generateRandomString());
        Log.d("debago", "in get end of downlaod in storage helper, value to send is : "+result.getValue());

        return result;
    }*/
}
