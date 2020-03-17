package com.inved.realestatemanager.firebase;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.utils.ImageCameraOrGallery;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;

public class MyUploadService extends Service {

    /**
     * Intent Actions
     **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /**
     * Intent Extras
     **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOCUMENT_ID = "extra_document_id";
    public static final String EXTRA_PHOTO_NUMBER = "extra_photo_number";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    // [START declare_ref]
    private StorageReference mStorageRef;
    // [END declare_ref]

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            String documentId = intent.getStringExtra(EXTRA_DOCUMENT_ID);
            int numberPhoto = intent.getIntExtra(EXTRA_PHOTO_NUMBER, 0);
            mStorageRef = FirebaseStorage.getInstance().getReference(documentId);

            uploadFromUri(fileUri, documentId, numberPhoto);

        }

        return START_REDELIVER_INTENT;
    }

    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri, String documentId, int numberPhoto) {

        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        if (fileUri.getLastPathSegment() != null) {
            final StorageReference photoRef = mStorageRef.child("Pictures")
                    .child(fileUri.getLastPathSegment());


            String downloadURL = fileUri.getLastPathSegment();
            //We have to put here the url in firestore
            switch (numberPhoto) {
                case 1:
                    PropertyHelper.updatePhotoUri1(downloadURL, documentId);
                    break;
                case 2:
                    PropertyHelper.updatePhotoUri2(downloadURL, documentId);
                    break;
                case 3:
                    PropertyHelper.updatePhotoUri3(downloadURL, documentId);
                    break;
                case 4:
                    PropertyHelper.updatePhotoUri4(downloadURL, documentId);
                    break;
                case 5:
                    PropertyHelper.updatePhotoUri5(downloadURL, documentId);
                    break;
                case 6:
                    RealEstateAgentHelper.updateUrlPicture(downloadURL);
                    break;
                default:
                    // code block
            }

            //File from external
            ImageCameraOrGallery imageCameraOrGallery = new ImageCameraOrGallery();
            Uri localFile = Uri.fromFile(new File(fileUri.toString()));
            File fileExternal = imageCameraOrGallery.getFile(MainApplication.getInstance().getApplicationContext(),localFile); //file external

            //File from internal
            File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String mFileName = "/" + fileExternal.getName();
            File fileInternal = new File(storageDir, mFileName); //file internal

            if (fileInternal.exists()) {
                uploadInternalFile(photoRef,fileUri);

            } else if (fileExternal.exists()) {
                uploadExternalFile(photoRef,fileExternal);

            }

        }


    }
    // [END upload_from_uri]

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

    private void uploadInternalFile(StorageReference photoRef,Uri internalFile){
        // Upload file to Firebase Storage
        //Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
        photoRef.putFile(internalFile)
                .continueWithTask(task -> {
                    // Forward any exceptions
                    if (!task.isSuccessful()) {
                        if (task.getException() != null) {
                            throw task.getException();
                        }

                    }

                    // Request the public download URL
                    return photoRef.getDownloadUrl();
                })
                .addOnSuccessListener(downloadUri -> {

                })
                .addOnFailureListener(exception -> {

                });

    }

    private void uploadExternalFile(StorageReference photoRef,File externalFile){

        photoRef.putFile(Uri.fromFile(externalFile)).continueWithTask(task -> {
            // Forward any exceptions
            if (!task.isSuccessful()) {
                if (task.getException() != null) {
                    throw task.getException();
                }

            }


            // Request the public download URL
            return photoRef.getDownloadUrl();
        })
                .addOnSuccessListener(downloadUri -> {

                })
                .addOnFailureListener(exception2 -> {
                });
    }
}

