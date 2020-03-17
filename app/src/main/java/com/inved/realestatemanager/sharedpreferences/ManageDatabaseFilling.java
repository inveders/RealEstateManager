package com.inved.realestatemanager.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageDatabaseFilling {

    private static final String KEY_DATABASE_FILLING = "KEY_DATABASE_FILLING";
    private static final String KEY_BOOLEAN_DATABASE_FILLING = "KEY_BOOLEAN_DATABASE_FILLING";

    public static void saveDatabaseFilledState(Context context, boolean state) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_DATABASE_FILLING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_BOOLEAN_DATABASE_FILLING, state);
        editor.apply();
    }

    public static boolean isDatabaseFilled(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_DATABASE_FILLING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_BOOLEAN_DATABASE_FILLING,false);
    }


}
