package com.inved.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.sharedpreferences.ManageCurrency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollarsToConvert
     * @return
     */
    public double convertDollarToEuro(double dollarsRates,double dollarsToConvert) {

        double conversion = dollarsToConvert/dollarsRates;
        BigDecimal bd = new BigDecimal(conversion).setScale(2,RoundingMode.HALF_UP);
        double newInput = bd.doubleValue();

        return (int)newInput;
    }

    //Convert euro in dollars
    public String convertEuroToDollars(double dollarsRates, double euroToConvert) {
        UnitConversion unitConversion = new UnitConversion();
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return unitConversion.changeDoubleToStringWithThousandSeparator(dollarsRates * euroToConvert);
    }

    //Convert euro in dollars in String
    private String convertEuroToDollarsInDoubleFormat(double dollarsRates, double euroToConvert) {

        double conversion = dollarsRates*euroToConvert;
        BigDecimal bd = new BigDecimal(conversion).setScale(2,RoundingMode.HALF_UP);
        double newInput = bd.doubleValue();

        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return String.valueOf(newInput);
    }


    //Convert euro in dollars in Int
    public int convertEuroToDollarsInIntFormat(double dollarsRates, double euroToConvert) {

        double conversion = dollarsRates*euroToConvert;
        BigDecimal bd = new BigDecimal(conversion).setScale(2,RoundingMode.HALF_UP);
        double newInput = bd.doubleValue();

        return (int)newInput;
    }


    //Get price in good currency
    public String getPriceInGoodCurrency(double priceInEuro) {

        UnitConversion unitConversion = new UnitConversion();
        if (ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")) {
            return unitConversion.changeDoubleToStringWithThousandSeparator(priceInEuro);
        } else {
            return convertEuroToDollars(ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()), priceInEuro);
        }
    }

    //Get price in good currency Double type
    public String getPriceInGoodCurrencyDoubleType(double priceInEuro) {

        if (ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")) {
            return String.valueOf(priceInEuro);
        } else {
            return convertEuroToDollarsInDoubleFormat(ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()),priceInEuro);
        }
    }


    //Get price in good currency int type
    public int getPriceInGoodCurrencyIntType(double priceInEuro) {

        if (ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")) {
            return (int) priceInEuro;
        } else {
            return convertEuroToDollarsInIntFormat(ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()),priceInEuro);
        }
    }

    //Save price in good currency
    public double savePriceInEuro(double price) {

        if (ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")) {
            return price;
        } else {
            return convertDollarToEuro(ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()),price);
        }
    }

    public String goodCurrencyUnit() {

        if (ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")) {
            return MainApplication.getResourses().getString(R.string.list_property_unit_price_euro);
        } else {
            return MainApplication.getResourses().getString(R.string.list_property_unit_price_dollars);
        }
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        return dateFormat.format(new Date());
    }


    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context) {

        //We get the context
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        boolean wifi = false;
        boolean mobile = false;
        boolean isNetworkAvailable;

        //What network is available?

        if (connectivityManager != null) {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;//Value 1
                mobile = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;//Value 0
                if (wifi) {
                    Log.d("NetworkState", "L'interface de connexion active est du Wifi : " + wifi);
                } else if (mobile) {
                    Log.d("NetworkState", "L'interface de connexion active est du réseau mobile : " + mobile);
                }

            } else {
                Log.d("NetworkState", "No wifi or mobile network");
            }

            isNetworkAvailable = (wifi || mobile);

            return isNetworkAvailable;
        }

        return false;
    }



}

