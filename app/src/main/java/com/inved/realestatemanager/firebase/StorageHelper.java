package com.inved.realestatemanager.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.inved.realestatemanager.utils.MainApplication;

public class StorageHelper extends BroadcastReceiver {

    private Context ctx = MainApplication.getInstance().getApplicationContext();

    public void uploadFromUri(Uri fileUri, String documentId, int number) {

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background

        ctx.startService(new Intent(ctx, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .putExtra(MyUploadService.EXTRA_DOCUMENT_ID, documentId)
                .putExtra(MyUploadService.EXTRA_PHOTO_NUMBER, number)
                .setAction(MyUploadService.ACTION_UPLOAD));


    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
