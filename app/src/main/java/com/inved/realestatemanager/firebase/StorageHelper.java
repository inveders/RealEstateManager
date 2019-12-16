package com.inved.realestatemanager.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;

public class StorageHelper extends Fragment {

    private Context ctx = MainApplication.getInstance().getApplicationContext();
    private final String TAG = "debago";

    private BroadcastReceiver mBroadcastReceiver;

    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;

    private void setmBroadcastReceiver() {

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(ctx);
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);

                if (intent.getAction() != null) {
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

    public void uploadFromUri(Uri fileUri, String documentId, int number) {
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

    public String beginDownload(String getLastPathFromFirebase, String documentId) {

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
             //   Log.d("debago", ";local tem file created  created " + localFile.toString());
                viewModelActions(documentId);
                //  updateDb(timestamp,localFile.toString(),position);
            }).addOnFailureListener(exception -> Log.d("debago", ";local tem file not created  created " + exception.toString()));
        }

        else {
            Log.d("debago", "file already exist");
        }



        return filePath;

    }

    private void viewModelActions(String documentId){

        Log.d("debago", "storage helper view Model Actions");
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        PropertyViewModel propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);
        propertyViewModel.updateSelected(true,documentId);
    }


    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
        Log.d(TAG, "onUploadResultIntent, mDonwloadUrl is:" + mDownloadUrl + " mFileUri is : " + mFileUri);

    }


}
