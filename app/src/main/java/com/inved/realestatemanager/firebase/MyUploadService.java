package com.inved.realestatemanager.firebase;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyUploadService extends Service {

    private static final String TAG = "debago";

    /** Intent Actions **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /** Intent Extras **/
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
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            String documentId = intent.getStringExtra(EXTRA_DOCUMENT_ID);
            int numberPhoto = intent.getIntExtra(EXTRA_PHOTO_NUMBER,0);
            mStorageRef = FirebaseStorage.getInstance().getReference(documentId);
            Log.d(TAG, "onStartCommand in in documentId:" + documentId + " and number photo is " + numberPhoto);
            // Make sure we have permission to read the data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(
                        fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            uploadFromUri(fileUri,documentId,numberPhoto);
        }

        return START_REDELIVER_INTENT;
    }

    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri,String documentId, int numberPhoto) {
        Log.d(TAG, "uploadFromUri:src: upload" + fileUri.toString()+" Photo number is: "+numberPhoto);


        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        if(fileUri.getLastPathSegment()!=null){
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


            // Upload file to Firebase Storage
            Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());
            photoRef.putFile(fileUri)
                    .continueWithTask(task -> {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            if(task.getException()!=null){
                                throw task.getException();
                            }

                        }

                        Log.d(TAG, "uploadFromUri: upload success");

                        // Request the public download URL
                        return photoRef.getDownloadUrl();
                    })
                    .addOnSuccessListener(downloadUri -> {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri: getDownloadUri success");

                    })
                    .addOnFailureListener(exception -> {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                    });
        }




    }
    // [END upload_from_uri]

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }
}
