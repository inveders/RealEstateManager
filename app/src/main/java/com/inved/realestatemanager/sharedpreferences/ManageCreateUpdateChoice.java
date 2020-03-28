package com.inved.realestatemanager.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ManageCreateUpdateChoice {

    private static final String KEY_CREATE_UPDATE_CHOICE = "KEY_CREATE_UPDATE_CHOICE";
    private static final String KEY_CREATE_UPDATE_CHOICE_DATA = "KEY_CREATE_UPDATE_CHOICE_DATA";

    private static final String KEY_FIRST_PROPERTY_ID = "KEY_FIRST_PROPERTY_ID";
    private static final String KEY_FIRST_PROPERTY_ID_DATA = "KEY_FIRST_PROPERTY_ID_DATA";

    public static void saveCreateUpdateChoice(Context context, String createUpdateChoice) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CREATE_UPDATE_CHOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CREATE_UPDATE_CHOICE_DATA, createUpdateChoice);
        editor.apply();
    }

    public static String getCreateUpdateChoice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CREATE_UPDATE_CHOICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CREATE_UPDATE_CHOICE_DATA,null);
    }

    public static void saveFirstPropertyIdOnTablet(Context context, String firstPropertyIdOnTablet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_FIRST_PROPERTY_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_PROPERTY_ID_DATA, firstPropertyIdOnTablet);
        editor.apply();
    }

    public static String getFirstPropertyIdOnTablet(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_FIRST_PROPERTY_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRST_PROPERTY_ID_DATA,null);
    }
}
