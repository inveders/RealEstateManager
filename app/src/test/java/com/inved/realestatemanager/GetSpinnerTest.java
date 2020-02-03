package com.inved.realestatemanager;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.utils.MainApplication;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class GetSpinnerTest {

    private GetSpinner getSpinner = Mockito.spy(new GetSpinner());

    @Test
    public void should_findOne_When_StringA() {

        //Given
        String[] spinnerArray = {"A", "B", "C"};

        Spinner spinner = new Spinner(MainApplication.getInstance().getApplicationContext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (MainApplication.getInstance().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML

        spinner.setAdapter(spinnerArrayAdapter);

        //When
        int getIndexSpinnerFromString = getSpinner.getIndexSpinner(spinner,"A");

        //Then

        Assert.assertEquals(1,getIndexSpinnerFromString);

    }



    @Test
    public void should_find2_When_Int14() {

        //Given
        String[] spinnerArray = {"12", "14", "16"};

        Spinner spinner = new Spinner(MainApplication.getInstance().getApplicationContext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (MainApplication.getInstance().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML

        spinner.setAdapter(spinnerArrayAdapter);

        //When
        int getIndexSpinnerFromString = getSpinner.getIndexSpinnerInt(spinner,14);

        //Then

        Assert.assertEquals(2,getIndexSpinnerFromString);

    }


}
