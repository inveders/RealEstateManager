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


    @Test
    public void should_ReplaceAllSpace_With_Addition() {

        //Given
        String stringToConvert="42 bis rue principale 57840 Ottange";
        String strintToRetrieve="42+bis+rue+principale+57840+Ottange";

        //When
        String conversionResult = splitString.replaceAllSpacesByAddition(stringToConvert);

        //Then

        Assert.assertEquals(strintToRetrieve, conversionResult);

    }

    @Test
    public void should_GiveFiveLastString() {

        //Given
        String stringToConvert="Mariage";
        String strintToRetrieve="riage";

        //When
        String conversionResult = splitString.lastCharacters(stringToConvert,5);

        //Then

        Assert.assertEquals(strintToRetrieve, conversionResult);

    }


    @Test
    public void should_GiveFiveAllString_When_NumberCharacterMoreThanLenght() {

        //Given
        String stringToConvert="Mariage";
        String strintToRetrieve="Mariage";

        //When
        String conversionResult = splitString.lastCharacters(stringToConvert,8);

        //Then

        Assert.assertEquals(strintToRetrieve, conversionResult);

    }

}
