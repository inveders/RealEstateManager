package com.inved.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;



public class Utils {


    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812);
    }

    //Convert euro in dollars
    public String convertEuroToDollars(double dollarsRates, double euroToConvert) {

        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);

        return format.format(dollarsRates * euroToConvert);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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




