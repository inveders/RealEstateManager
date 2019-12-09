package com.inved.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageCreateUpdateChoice {

    private static final String KEY_CREATE_UPDATE_CHOICE = "KEY_CREATE_UPDATE_CHOICE";
    private static final String KEY_CREATE_UPDATE_CHOICE_DATA = "KEY_CREATE_UPDATE_CHOICE_DATA";

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
}
