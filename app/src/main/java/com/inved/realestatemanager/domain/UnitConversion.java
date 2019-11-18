package com.inved.realestatemanager.domain;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class UnitConversion {

    public String changeDoubleToStringWithThousandSeparator(double doubleToChange){


        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);


        return formatter.format(Math.round(doubleToChange));
    }


    public String changeIntToStringWithThousandSeparator(int intToChange){


        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);


        return formatter.format(Math.round(intToChange));
    }
}
