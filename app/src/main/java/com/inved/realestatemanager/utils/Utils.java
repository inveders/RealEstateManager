package com.inved.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.retrofit.RetrofitServiceApiExchange;

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
     * @param dollars
     * @return
     */
    private static double convertDollarToEuro(double dollars) {
        
        return (int) Math.round(dollars * ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()));
    }

    //Convert euro in dollars
    public String convertEuroToDollars(double dollarsRates, double euroToConvert) {
        UnitConversion unitConversion = new UnitConversion();
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        //format.format(dollarsRates * euroToConvert);
        return unitConversion.changeDoubleToStringWithThousandSeparator(dollarsRates*euroToConvert);
    }

    //Get price in good currency
    public String getPriceInGoodCurrency(double priceInEuro){

        UnitConversion unitConversion = new UnitConversion();
        if(ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")){
            return unitConversion.changeDoubleToStringWithThousandSeparator(priceInEuro);
        }else{
            return convertEuroToDollars(ManageCurrency.getRate(MainApplication.getInstance().getApplicationContext()),priceInEuro);
        }
    }

    //Save price in good currency
    public double savePriceInEuro(double price){

        if(ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")){
            return price;
        }else{
            return convertDollarToEuro(price);
        }
    }

    public String goodCurrencyUnit(){

        if(ManageCurrency.getCurrency(MainApplication.getInstance().getApplicationContext()).equals("EUR")){
            return MainApplication.getResourses().getString(R.string.list_property_unit_price_euro);
        }else{
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

    //VolleyRequest to get USD rates and convert euro in dollars
  /*  public void volleyRequest() {


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.exchangeratesapi.io/latest?symbols=USD";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("rates");
                    double dollarsRates = jsonObject.getDouble("USD");

                    String myDollars = dollarsConversion(dollarsRates,10);
                    Log.d("Debago","myDollars is "+myDollars);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Debago","That didn't work!");

            }
        });

        // Add the request to the RequestQueue.
        queue.add(objectRequest);

    }*/




