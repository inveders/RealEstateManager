package com.inved.realestatemanager.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateOfDay {

    public String getDateOfDay(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return dateFormat.format(calendar.getTime());
    }
}
