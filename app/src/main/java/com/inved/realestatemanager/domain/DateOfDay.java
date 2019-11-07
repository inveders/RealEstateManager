package com.inved.realestatemanager.domain;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateOfDay {

    public String getDateOfDay(){
        Calendar calendar = Calendar.getInstance();

    /*    int dd = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int yyyy = calendar.get(Calendar.DAY_OF_MONTH);*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Log.d("debago","date of day "+dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime());
    }
}
