package com.inved.realestatemanager.firebase;

import android.os.Environment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;

class StorageDownload {

    String beginDownload(String getLastPathFromFirebase, String documentId) {
        StorageReference fileReference = FirebaseStorage.getInstance().getReference(documentId).child("Pictures")
                .child(getLastPathFromFirebase);

        String mFileName = "/" + getLastPathFromFirebase;
        File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File localFile = new File(storageDir + mFileName);
        // Save a file: path for using again
        String filePath = "file://" + storageDir + mFileName;

        if (!localFile.exists()) {
            fileReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {}).addOnFailureListener(Throwable::toString);
        }

        return filePath;

    }

}
