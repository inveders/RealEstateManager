package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.DateOfDay;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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
