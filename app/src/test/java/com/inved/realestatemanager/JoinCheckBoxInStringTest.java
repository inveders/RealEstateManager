package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.JoinCheckBoxInString;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class JoinCheckBoxInStringTest {

    private JoinCheckBoxInString joinCheckBoxInString = Mockito.spy(new JoinCheckBoxInString());

    @Test
    public void shouldShowStringWithListString() {

        //Given
        List<String> list = new ArrayList<>();
        list.add("Paris");
        list.add("Luxembourg");
        list.add("Monaco");
        list.add("Washington");

        //When
        String convertedList = joinCheckBoxInString.joinMethod(list);

        //Then
        Assert.assertEquals("Paris,Luxembourg,Monaco,Washington",convertedList);

    }

}
