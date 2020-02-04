package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.UnitConversion;

import org.junit.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class UnitConversionTest {

    private UnitConversion unitConversion = Mockito.spy(new UnitConversion());

    @Test
    public void should_ConvertInString_With_Double_120000() {

        //Given
        double doubleToConvert = 120000.0;


        //When
        String stringToRetrieve = unitConversion.changeDoubleToStringWithThousandSeparator(doubleToConvert);

        //Then

        Assert.assertEquals("120 000", stringToRetrieve);


    }

    @Test
    public void should_ConvertInString_With_Double_1250000_5() {

        //Given
        double doubleToConvert = 1250000.5;


        //When
        String stringToRetrieve = unitConversion.changeDoubleToStringWithThousandSeparator(doubleToConvert);

        //Then

        Assert.assertEquals("1 250 001", stringToRetrieve);


    }


    @Test
    public void should_ConvertInString_With_Int_3000000() {

        //Given
        int intToConvert = 3000000;


        //When
        String stringToRetrieve = unitConversion.changeIntToStringWithThousandSeparator(intToConvert);

        //Then

        Assert.assertEquals("3 000 000", stringToRetrieve);


    }

}
