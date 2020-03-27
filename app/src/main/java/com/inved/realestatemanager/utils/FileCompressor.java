package com.inved.realestatemanager.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileCompressor {
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private String destinationDirectoryPath;

    public FileCompressor() {

        String mFileName = "/";
        File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
      
        // Save a file: path for using again

        destinationDirectoryPath = "file://" + storageDir + mFileName;
    }

    public File compressToFile(File imageFile) throws IOException {
       return compressToFile(imageFile, imageFile.getName());
    }

    private File compressToFile(File imageFile, String compressedFileName) throws IOException {
        int quality = 80;
        int maxHeight = 816;
        //max width and height values of the compressed image is taken as 612x816
        int maxWidth = 612;
        return ImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }


}