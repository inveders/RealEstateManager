package com.inved.realestatemanager.firebase;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyDownloadService extends MyBaseTaskService{

    private static final String TAG = "Storage#DownloadService";

    /** Actions **/
    public static final String ACTION_DOWNLOAD = "action_download";
    public static final String DOWNLOAD_COMPLETED = "download_completed";
    public static final String DOWNLOAD_ERROR = "download_error";

    /** Extras **/
    public static final String EXTRA_DOWNLOAD_PATH = "extra_download_path";
    public static final String EXTRA_DOWNLOAD_DOCUMENT_ID = "extra_download_document_id";
    public static final String EXTRA_BYTES_DOWNLOADED = "extra_bytes_downloaded";

    private StorageReference mStorageRef;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);

        if (ACTION_DOWNLOAD.equals(intent.getAction())) {
            // Get the path to download from the intent
            String downloadPath = intent.getStringExtra(EXTRA_DOWNLOAD_PATH);
            String documentId = intent.getStringExtra(EXTRA_DOWNLOAD_DOCUMENT_ID);
            // Initialize Storage
            mStorageRef = FirebaseStorage.getInstance().getReference(documentId);
            downloadFromPath(downloadPath);
        }

        return START_REDELIVER_INTENT;
    }

    private void downloadFromPath(final String downloadPath) {
        Log.d(TAG, "downloadFromPath:" + downloadPath);

        // Mark task started
        taskStarted();

        // Download and get total bytes
        mStorageRef.child(downloadPath).getStream(
                (taskSnapshot, inputStream) -> {

                    // Close the stream at the end of the Task
                    inputStream.close();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "download:SUCCESS");

                    // Send success broadcast with number of bytes downloaded
                    broadcastDownloadFinished(downloadPath, taskSnapshot.getTotalByteCount());

                    // Mark task completed
                    taskCompleted();
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG, "download:FAILURE", exception);

                    // Send failure broadcast
                    broadcastDownloadFinished(downloadPath, -1);

                    // Mark task completed
                    taskCompleted();
                });
    }

    /**
     * Broadcast finished download (success or failure).
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastDownloadFinished(String downloadPath, long bytesDownloaded) {
        boolean success = bytesDownloaded != -1;
        String action = success ? DOWNLOAD_COMPLETED : DOWNLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_PATH, downloadPath)
                .putExtra(EXTRA_BYTES_DOWNLOADED, bytesDownloaded);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DOWNLOAD_COMPLETED);
        filter.addAction(DOWNLOAD_ERROR);

        return filter;
    }

}
