package com.inved.realestatemanager.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageCurrency {

    private static final String KEY_CURRENCY_CHOICE = "KEY_CURRENCY_CHOICE";
    private static final String KEY_CURRENCY_DATA = "KEY_CURRENCY_DATA";
    private static final String KEY_CURRENCY_RETRIEVE_RETROFIT = "KEY_CURRENCY_RETRIEVE_RETROFIT";
    private static final String KEY_CURRENCY_VALUE = "KEY_CURRENCY_VALUE";

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

    public static void saveRate(Context context, float rate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CURRENCY_RETRIEVE_RETROFIT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_CURRENCY_VALUE,rate);
        editor.apply();
    }

    public static float getRate(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_CURRENCY_RETRIEVE_RETROFIT, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(KEY_CURRENCY_VALUE,2);
    }

}
