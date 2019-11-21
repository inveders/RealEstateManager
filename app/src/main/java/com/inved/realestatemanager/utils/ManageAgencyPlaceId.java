package com.inved.realestatemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageAgencyPlaceId {

    private static final String KEY_AGENCY_PLACE_ID_CHOICE = "KEY_AGENCY_PLACE_ID_CHOICE";
    private static final String KEY_AGENCY_PLACE_ID_DATA = "KEY_AGENCY_PLACE_ID_DATA";

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
}
