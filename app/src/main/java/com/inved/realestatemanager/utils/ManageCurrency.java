package com.inved.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageCurrency {

    private static final String KEY_CURRENCY_CHOICE = "KEY_CURRENCY_CHOICE";
    private static final String KEY_CURRENCY_DATA = "KEY_CURRENCY_DATA";

    public static void saveCurrency(Context context, String currency) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CURRENCY_CHOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CURRENCY_DATA,currency);
        editor.apply();
    }

    public static String getCurrency(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CURRENCY_CHOICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CURRENCY_DATA,"EUR");
    }


}
