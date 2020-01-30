package com.inved.realestatemanager.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageAgency {

    private static final String KEY_AGENCY_PLACE_ID_CHOICE = "KEY_AGENCY_CHOICE";
    private static final String KEY_AGENCY_PLACE_ID_DATA = "KEY_AGENCY_PLACE_ID_DATA";
    private static final String KEY_AGENCY_NAME_DATA = "KEY_AGENCY_NAME_DATA";

    public static void saveAgencyPlaceId(Context context, String agencyPlaceId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_AGENCY_PLACE_ID_CHOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AGENCY_PLACE_ID_DATA, agencyPlaceId);
        editor.apply();
    }

    public static String getAgencyPlaceId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_AGENCY_PLACE_ID_CHOICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AGENCY_PLACE_ID_DATA,null);
    }

    public static void saveAgencyName(Context context, String agencyName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_AGENCY_PLACE_ID_CHOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AGENCY_NAME_DATA, agencyName);
        editor.apply();
    }

    public static String getAgencyName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_AGENCY_PLACE_ID_CHOICE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AGENCY_NAME_DATA,null);
    }
}
