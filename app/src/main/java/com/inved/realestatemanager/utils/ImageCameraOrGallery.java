package com.inved.realestatemanager.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ImageCameraOrGallery {

    private Context context = MainApplication.getInstance().getApplicationContext();

    /**
     * Create file with JPEG_ name
     */
    public File createImageFile() throws IOException {
        // Create an image file name
        if (context != null) {
            String mFileName = "JPEG_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // Save a file: path for using again

            return File.createTempFile(mFileName, ".jpg", storageDir);
        }

        return null;
    }

    public String getCameraFilePath(File mFile){
        return "file://" + mFile.getAbsolutePath();
    }

    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = Objects.requireNonNull(context).getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
