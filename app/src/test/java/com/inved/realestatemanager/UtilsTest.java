package com.inved.realestatemanager;

import com.inved.realestatemanager.Utils.Utils;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsTest {

    private Utils utils = Mockito.spy(new Utils());


    @Test
    public void should_ConvertInDollars_With_Euro() {

        //Given
        double dollarsRates = 1.11;
        double euroToConvert = 10;

        //When
        String dollarsConversion = utils.convertEuroToDollars(dollarsRates,euroToConvert);

        //Then

        Assert.assertEquals("11,10", dollarsConversion);


    }


    @Test
    public void should_ShowCurrentDateInFormat_15012018() {

        //Given
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());


        //When
        String goodFormatDate = utils.getTodayDate();

        //Then
        Assert.assertEquals(currentDate, goodFormatDate);

    }

    /*  @Test
    public void should_ReturnTrue_With_WifiAvailaible() {

        //Given
        final Context context = mock(Context.class);

        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
       // Mockito.when(ConnectivityManager.TYPE_WIFI).thenReturn(networkInfo);




        //When
        boolean isWifiAvailable = conversionEuro.checkNetwork();

        //Then
        Assert.assertTrue(isWifiAvailable);

    }*/
}
