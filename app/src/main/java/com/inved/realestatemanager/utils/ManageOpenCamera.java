package com.inved.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageOpenCamera {

    private static final String KEY_CAMERA_OPEN = "KEY_CAMERA_OPEN";
    private static final String KEY_CAMERA_OPEN_BOOLEAN = "KEY_CAMERA_OPEN_BOOLEAN";

    public static void saveIfCameraOpen(Context context, boolean isOpen) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CAMERA_OPEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_CAMERA_OPEN_BOOLEAN, isOpen);
        editor.apply();
    }

    public static boolean getIfCameraOpen(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CAMERA_OPEN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_CAMERA_OPEN_BOOLEAN,false);
    }
}
