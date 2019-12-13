package com.inved.realestatemanager.domain;

import android.util.Log;
import android.widget.Spinner;

public class GetSpinner {

    public int getIndexSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {

                Log.d("debago","spinner at position "+i+" is : "+spinner.getItemAtPosition(i));

                return i;
            }
        }

        return 0;
    }

    //private method of your class
    public int getIndexSpinnerInt(Spinner spinner, int myInt) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (Integer.valueOf(spinner.getItemAtPosition(i).toString()) == myInt) {
                return i;
            }
        }

        return 0;
    }

}
