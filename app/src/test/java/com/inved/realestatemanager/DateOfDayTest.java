package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.DateOfDay;
import com.inved.realestatemanager.domain.SplitString;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;
import java.util.Locale;

public class DateOfDayTest {

    private DateOfDay dateOfDay = Mockito.spy(new DateOfDay());

    @Test
    public void should_GiveDateOfDay() {

        //Given
        String dateToday= "03/02/2020";

        //When
        String date = dateOfDay.getDateOfDay();

        //Then

        Assert.assertEquals(dateToday, date);

    }
}
