package com.inved.realestatemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.utils.MainApplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class GetSpinnerTest{

 /*   private GetSpinner getSpinner = Mockito.spy(new GetSpinner());
    private Context context = mock(Context.class);
    private AbsSpinner absSpinner = mock(AbsSpinner.class);
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;


    @Before
    public void setUp() throws Exception {
        spinner = new Spinner(context);
        String [] testItems = {"A", "B", "C"};
        arrayAdapter = new MyArrayAdapter(this.context, testItems);
    }

    @Test
    public void should_findOne_When_StringA() {

        //Given
        String[] spinnerArray = {"A", "B", "C"};

        Spinner spinner2 = new Spinner(context);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML

        spinner2.setAdapter(spinnerArrayAdapter);


        //When
        int getIndexSpinnerFromString = getSpinner.getIndexSpinner(spinner2,"A");

        //Then
        Assert.assertEquals(1,getIndexSpinnerFromString);

    }



    @Test
    public void should_find2_When_Int14() {

        //Given
        String[] spinnerArray = {"12", "14", "16"};

        Spinner spinner2 = new Spinner(context);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML

        spinner2.setAdapter(spinnerArrayAdapter);

        //When
        int getIndexSpinnerFromString = getSpinner.getIndexSpinnerInt(spinner2,14);

        //Then

        Assert.assertEquals(2,getIndexSpinnerFromString);

    }




    @Test
    public void checkSetAdapter() {
        spinner.setAdapter(arrayAdapter);
    }

    @Test
    public void getSelectedItemShouldReturnCorrectValue(){
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        assertThat((String) spinner.getSelectedItem()).isEqualTo("foo");
        //  assertThat((String) spinner.getSelectedItem()).isNotEqualTo("bar");

        spinner.setSelection(1);
        // assertThat((String) spinner.getSelectedItem()).isEqualTo("bar");
        //  assertThat((String) spinner.getSelectedItem()).isNotEqualTo("foo");
    }

    @Test
    public void getSelectedItemShouldReturnNull_NoAdapterSet(){
        //When
        int getIndexSpinnerFromString = getSpinner.getIndexSpinner(spinner,"A");

        //Then
        Assert.assertEquals(1,getIndexSpinnerFromString);
    }

    @Test
    public void setSelectionWithAnimatedTransition() {
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0, true);

        //   assertThat((String) spinner.getSelectedItem()).isEqualTo("foo");
        //  assertThat((String) spinner.getSelectedItem()).isNotEqualTo("bar");

    }

    private static class MyArrayAdapter extends ArrayAdapter<String> {
        public MyArrayAdapter(Context context, String[] testItems) {
            super(context, android.R.layout.simple_spinner_item, testItems);
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            return new View(getContext());
        }
    }*/


}
