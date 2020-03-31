package com.inved.realestatemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.inved.realestatemanager.sharedpreferences.ManageCurrency;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class UtilsTest {

    private Utils utils;
    private SharedPreferences sharedPrefs;


    @Before
    public void before() {
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
        Context context = Mockito.mock(Context.class);
        this.utils = Mockito.spy(new Utils());
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
    }

    @Test
    public void should_ConvertInDollars_With_Euro() {

        //Given
        double dollarsRates = 1.11;
        double euroToConvert = 10;

        //When
        String dollarsConversion = utils.convertEuroToDollars(dollarsRates,euroToConvert);

        //Then
        Assert.assertEquals("11", dollarsConversion);


    }


    @Test
    public void should_ConvertInDollars_When_IntFormat_With_Euro() {

        //Given
        double dollarsRates = 1.11;
        double euroToConvert = 10;

        //When
        int dollarsConversion = utils.convertEuroToDollarsInIntFormat(dollarsRates,euroToConvert);

        //Then

        Assert.assertEquals(11, dollarsConversion);


    }

    @Test
    public void should_ConvertInEuro_With_Dollars() {

        //Given
        double dollarsToConvert = 10;
        double euroToRetrieve = 10;

        //When
        Mockito.when(sharedPrefs.getFloat("KEY_CURRENCY_VALUE", 2)).thenReturn((float) 2.2);
        double dollarsConversion = utils.convertDollarToEuro(ManageCurrency.getRate(MainApplication.getInstance()
                .getApplicationContext()),dollarsToConvert);

        //Then
        Assert.assertEquals(String.valueOf(euroToRetrieve), String.valueOf(dollarsConversion));
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


}
