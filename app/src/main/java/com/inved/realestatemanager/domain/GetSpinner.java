package com.inved.realestatemanager.domain;

import android.widget.Spinner;

public class GetSpinner {

    public int getIndexSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {


                return i;
            }
        }

        return 0;
    }
}
