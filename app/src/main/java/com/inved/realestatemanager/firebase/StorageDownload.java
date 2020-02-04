package com.inved.realestatemanager.firebase;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;

public class StorageDownload {

    public String beginDownload(String getLastPathFromFirebase, String documentId) {
        //Log.d("debago", "BEGIN DOWNLOAD");
        StorageReference fileReference = FirebaseStorage.getInstance().getReference(documentId).child("Pictures")
                .child(getLastPathFromFirebase);

        String mFileName = "/" + getLastPathFromFirebase;
        File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File localFile = new File(storageDir + mFileName);
        // Save a file: path for using again
        String filePath = "file://" + storageDir + mFileName;

        if (!localFile.exists()) {
            // Log.d("debago", "file doesn't exist we download it "+localFile.exists());
            fileReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> Log.d("debago", ";local tem file created  created ")).addOnFailureListener(exception -> Log.d("debago", ";local tem file not created  created " + exception.toString()));
        }

        else {
            Log.d("debago", "file already exist");
        }

        return filePath;

    }

}
