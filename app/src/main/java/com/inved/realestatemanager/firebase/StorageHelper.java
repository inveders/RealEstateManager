package com.inved.realestatemanager.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;
import java.io.IOException;

public class StorageHelper {

    private Context ctx=MainApplication.getInstance().getApplicationContext();

    private final String TAG = "debago";

    private BroadcastReceiver mBroadcastReceiver;

    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;



   /***Find where we stop the service
    @Override
    public void onStop() {
        super.onStop();

        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
*/

    private void setmBroadcastReceiver(){

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(ctx);
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);

                if(intent.getAction()!=null){
                    switch (intent.getAction()) {

                        case MyUploadService.UPLOAD_COMPLETED:
                        case MyUploadService.UPLOAD_ERROR:
                            onUploadResultIntent(intent);
                            break;
                    }
                }


            }
        };
    }

    public void uploadFromUri(Uri fileUri,String documentId,int number) {
        setmBroadcastReceiver();
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any
        mDownloadUrl = null;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background

        ctx.startService(new Intent(ctx, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .putExtra(MyUploadService.EXTRA_DOCUMENT_ID, documentId)
                .putExtra(MyUploadService.EXTRA_PHOTO_NUMBER, number)
                .setAction(MyUploadService.ACTION_UPLOAD));


    }

    public void beginDownload(String getLastPathFromFirebase,String documentId) throws IOException {
        Log.d("debago", "beginDownload:");
        StorageReference fileReference = FirebaseStorage.getInstance().getReference(documentId).child("Pictures")
                .child(getLastPathFromFirebase);

        File localFile = File.createTempFile("Pictures", "jpg");

        fileReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            Log.d("debago", "beginDownload the file is :"+localFile.getAbsolutePath());
            // Local temp file has been created
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });

    }
/*
    private File createImageFile() throws IOException {
        // Create an image file name

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
            // Save a file: path for using again
           // filePath = "file://" + mFile.getAbsolutePath();
            return mFile;


        return null;
    }*/

    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
        Log.d(TAG, "onUploadResultIntent, mDonwloadUrl is:" + mDownloadUrl+" mFileUri is : "+mFileUri);

    }


}
