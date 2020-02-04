package com.inved.realestatemanager;

import com.inved.realestatemanager.domain.RandomString;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class RandomStringTest {

    private RandomString randomString = Mockito.spy(new RandomString());

    @Test
    public void should_GiveString_When_CreateRandomString_With_20Characters() {

        //Given
        int lenghtToRetrieve = 20;

        //When
        String myNewString = randomString.generateRandomString();
        int myNewStringLenght = myNewString.length();
        //Then

        Assert.assertEquals(lenghtToRetrieve, myNewStringLenght);

    }
}
