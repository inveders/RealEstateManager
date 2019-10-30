package com.inved.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageCreateUpdateChoice {

    private static final String KEY_CREATE_UPDATE_CHOICE = "KEY_CREATE_UPDATE_CHOICE";
    private static final String KEY_CREATE_UPDATE_CHOICE_DATA = "KEY_CREATE_UPDATE_CHOICE_DATA";

    public static void saveCreateUpdateChoice(Context context, long createUpdateChoice) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CREATE_UPDATE_CHOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_CREATE_UPDATE_CHOICE_DATA, createUpdateChoice);
        editor.apply();
    }

    public static long getCreateUpdateChoice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CREATE_UPDATE_CHOICE, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(KEY_CREATE_UPDATE_CHOICE_DATA,0);
    }
}
