package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.SplitString;

import org.junit.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class SplitStringTest {

    private SplitString splitString = Mockito.spy(new SplitString());

    @Test
    public void should_GiveFirstname_When_SplitStringArray_With_Whitespace() {

        //Given
        String stringToSplit="Alexandra Gnimadi";

        //When
        String firstname = splitString.splitStringWithSpace(stringToSplit,0);

        //Then

        Assert.assertEquals("Alexandra", firstname);

    }

    @Test
    public void should_GiveLastname_When_SplitStringArray_With_Whitespace() {

        //Given
        String stringToSplit="Alexandra Gnimadi";

        //When
        String lastname = splitString.splitStringWithSpace(stringToSplit,1);

        //Then

        Assert.assertEquals("Gnimadi", lastname);

    }

    @Test
    public void should_GiveNull_When_SplitStringArray_With_Whitespace() {

        //Given
        String stringToSplit="Alexandra Gnimadi";

        //When
        String nullString = splitString.splitStringWithSpace(stringToSplit,3);

        //Then

        Assert.assertNull(nullString);

    }
}
