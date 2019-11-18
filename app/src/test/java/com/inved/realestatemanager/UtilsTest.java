package com.inved.realestatemanager;

import com.inved.realestatemanager.utils.Utils;

import org.junit.Assert;

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


}
