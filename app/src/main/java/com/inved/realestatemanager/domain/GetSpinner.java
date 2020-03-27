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

    //private method of your class
    public int getIndexSpinnerInt(Spinner spinner, int myInt) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (Integer.parseInt(spinner.getItemAtPosition(i).toString()) == myInt) {
                return i;
            }
        }

        return 0;
    }

}
