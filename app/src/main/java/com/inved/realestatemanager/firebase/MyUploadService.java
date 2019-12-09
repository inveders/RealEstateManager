package com.inved.realestatemanager.firebase;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyUploadService extends MyBaseTaskService {

    private static final String TAG = "MyUploadService";

    /** Intent Actions **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /** Intent Extras **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOCUMENT_ID = "extra_document_id";
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

            mStorageRef = FirebaseStorage.getInstance().getReference(documentId);

            // Make sure we have permission to read the data
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContentResolver().takePersistableUriPermission(
                        fileUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }*/

            uploadFromUri(fileUri);
        }

        return START_REDELIVER_INTENT;
    }

    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // [START_EXCLUDE]
        taskStarted();

        // [END_EXCLUDE]

        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        if(fileUri.getLastPathSegment()!=null){
            final StorageReference photoRef = mStorageRef.child("Pictures")
                    .child(fileUri.getLastPathSegment());

            // [END get_child_ref]

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

                        // [START_EXCLUDE]
                        broadcastUploadFinished(downloadUri, fileUri);
                        taskCompleted();
                        // [END_EXCLUDE]
                    })
                    .addOnFailureListener(exception -> {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        // [START_EXCLUDE]
                        broadcastUploadFinished(null, fileUri);
                        taskCompleted();
                        // [END_EXCLUDE]
                    });
        }




    }
    // [END upload_from_uri]

    /**
     * Broadcast finished upload (success or failure).
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }


    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }
}
