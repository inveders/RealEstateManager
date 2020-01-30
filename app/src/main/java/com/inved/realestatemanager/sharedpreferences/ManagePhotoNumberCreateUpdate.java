package com.inved.realestatemanager.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ManagePhotoNumberCreateUpdate {

    private static final String KEY_UPDATE_STATUS = "KEY_UPDATE_STATUS";
    private static final String KEY_PHOTO_NUMBER = "KEY_PHOTO_NUMBER";
    private static final String KEY_UPDATE_STATUS_DATA  = "KEY_UPDATE_STATUS_DATA";
    private static final String KEY_PHOTO_NUMBER_DATA  = "KEY_PHOTO_NUMBER_DATA";

    public static void saveUpdateStatus(Context context, String updateStatus) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_UPDATE_STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UPDATE_STATUS_DATA, updateStatus);
        editor.apply();
    }

    public static void savePhotoNumber(Context context, int photoNumber) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PHOTO_NUMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PHOTO_NUMBER_DATA, photoNumber);
        editor.apply();
    }

    public static String getUpdateStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_UPDATE_STATUS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_UPDATE_STATUS_DATA,"create");
    }

    public static int getPhotoNumber(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_PHOTO_NUMBER, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_PHOTO_NUMBER_DATA,0);
    }
}
