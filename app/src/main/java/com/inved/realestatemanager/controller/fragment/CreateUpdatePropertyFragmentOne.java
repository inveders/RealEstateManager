package com.inved.realestatemanager.controller.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.CreatePropertyActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateUpdatePropertyFragmentOne extends Fragment implements AdapterView.OnItemSelectedListener {

    private CreateUpdateInterface callback;

    private PropertyViewModel propertyViewModel;

    private Spinner typePropertySpinner;
    private Spinner numberRoomSpinner;
    private Spinner numberBedroomSpinner;
    private Spinner numberBathroomSpinner;

    private EditText priceEditText;
    private EditText surfaceEditText;
    private EditText streetNumberEditText;
    private EditText streetNameEditText;
    private EditText zipCodeEditText;
    private EditText townNameEditText;
    private AutoCompleteTextView countryEditText;


    private CheckBox schoolCheckBox;
    private CheckBox restaurantsCheckBox;
    private CheckBox carParksCheckBox;
    private CheckBox shopsCheckBox;
    private CheckBox touristAttractionCheckBox;
    private CheckBox oilStationCheckBox;

    //String for spinners
    private String typeProperty = null;
    private String numberRoomsInProperty = "0";
    private String numberBathroomsInProperty = "0";
    private int numberBedroomsInProperty = 0;

    private double pricePropertyInDollar = 0.0;
    private double surfaceAreaProperty = 0.0;
    private String streetNumber = null;
    private String streetName = null;
    private String zipCode = null;
    private String townProperty = null;
    private String country = null;
    private String pointOfInterest = null;
    private String addressCompl = null;

    private Context context;

    private long propertyId;


    public interface CreateUpdateInterface {
        void clickOnNextButton(String typeProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                               int numberBedroomsInProperty, double pricePropertyInDollar, double surfaceAreaProperty,
                               String streetNumber, String streetName, String zipCode, String townProperty, String country,
                               String pointOfInterest, String addressCompl, long propertyId);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_one, container, false);
        Button nextButton = v.findViewById(R.id.create_update_next_button);

        typePropertySpinner = v.findViewById(R.id.create_update_spinner_type_property);
        numberRoomSpinner = v.findViewById(R.id.create_update_spinner_number_rooms);
        numberBedroomSpinner = v.findViewById(R.id.create_update_spinner_number_bedroom);
        numberBathroomSpinner = v.findViewById(R.id.create_update_spinner_number_bathrooms);

        priceEditText = v.findViewById(R.id.create_updtate_price_edittext);
        surfaceEditText = v.findViewById(R.id.create_update_surface_edittext);
        streetNumberEditText = v.findViewById(R.id.create_update_street_number_edittext);
        streetNameEditText = v.findViewById(R.id.create_update_street_name_edittext);
        zipCodeEditText = v.findViewById(R.id.create_update_zip_code_edittext);
        townNameEditText = v.findViewById(R.id.create_update_town_name_edittext);
        countryEditText = v.findViewById(R.id.create_update_country_name_autocompleteText);

        schoolCheckBox = v.findViewById(R.id.create_update_checkbox_poi_schools);
        restaurantsCheckBox = v.findViewById(R.id.create_update_checkbox_poi_restaurants);
        carParksCheckBox = v.findViewById(R.id.create_update_checkbox_poi_car_parks);
        shopsCheckBox = v.findViewById(R.id.create_update_checkbox_poi_shops);
        touristAttractionCheckBox = v.findViewById(R.id.create_update_checkbox_poi_tourist_attraction);
        oilStationCheckBox = v.findViewById(R.id.create_update_checkbox_poi_oil_station);


        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterCountry =
                new ArrayAdapter<>(MainApplication.getInstance().getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        countryEditText.setAdapter(adapterCountry);

        nextButton.setOnClickListener(view -> createProperty());
        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);

        this.configureViewModel();

        //We check if it's a new add property or just a modification
        if(getActivity()!=null){
            context=getActivity();
        }else{
            context=MainApplication.getInstance().getApplicationContext();
        }

        if (ManageCreateUpdateChoice.getCreateUpdateChoice(context) != 0) {
            propertyId = ManageCreateUpdateChoice.getCreateUpdateChoice(context);
            this.updateUIwithDataFromDatabase(propertyId);
        }


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        ManageCreateUpdateChoice.saveCreateUpdateChoice(context, 0);
        //Log.d("debago","6. on detach fragment one "+propertyId);
    }

    @SuppressLint("SetTextI18n")
    private void updateUIwithDataFromDatabase(long propertyId) {
        propertyViewModel.getOneProperty(propertyId).observe(this, property -> {

            priceEditText.setText(Double.toString(property.getPricePropertyInDollar()));
            surfaceEditText.setText(Double.toString(property.getSurfaceAreaProperty()));
            streetNumberEditText.setText(property.getStreetNumber());
            streetNameEditText.setText(property.getStreetName());
            zipCodeEditText.setText(property.getZipCode());
            townNameEditText.setText(property.getTownProperty());
            countryEditText.setText(property.getCountry());
            splitPoiInCheckbox(property.getPointOfInterest());

            typePropertySpinner.setSelection(getIndexSpinner(typePropertySpinner, property.getTypeProperty()));
            numberRoomSpinner.setSelection(getIndexSpinner(numberRoomSpinner, property.getNumberRoomsInProperty()));
            numberBedroomSpinner.setSelection(getIndexSpinnerInt(numberBedroomSpinner, property.getNumberBedroomsInProperty()));
            numberBathroomSpinner.setSelection(getIndexSpinner(numberBathroomSpinner, property.getNumberBathroomsInProperty()));


        });
    }

    //private method of your class
    private int getIndexSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    //private method of your class
    private int getIndexSpinnerInt(Spinner spinner, int myInt) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (Integer.valueOf(spinner.getItemAtPosition(i).toString()) == myInt) {
                return i;
            }
        }

        return 0;
    }

    private void splitPoiInCheckbox(String propertyPointOfInterest) {

        ArrayList<String> poiList = new ArrayList<>(Arrays.asList(propertyPointOfInterest.split(",")));

        if (poiList.contains(getString(R.string.fullscreen_dialog__poi_school))) {
            schoolCheckBox.setChecked(true);
        }
        if (poiList.contains(getString(R.string.fullscreen_dialog__poi_restaurants))) {
            restaurantsCheckBox.setChecked(true);
        }
        if (poiList.contains(getString(R.string.fullscreen_dialog__poi_shops))) {
            shopsCheckBox.setChecked(true);
        }
        if (poiList.contains(getString(R.string.fullscreen_dialog_poi_tourist_attraction))) {
            touristAttractionCheckBox.setChecked(true);
        }
        if (poiList.contains(getString(R.string.fullscreen_dialog_poi_car_parks))) {
            carParksCheckBox.setChecked(true);
        }
        if (poiList.contains(getString(R.string.fullscreen_dialog_poi_oil_station))) {
            oilStationCheckBox.setChecked(true);
        }

    }

    // Create a new property A TERMINER AVEC LES CONTROLES, SURTOUT SUR L'APPUI DU BOUTON, RAJOUTER LES CHAMPS OBLIGATOIRES, VOIR COMMENT PASSER A UNE AUTRE PAGE
    //VOIR QUE FAIRE DES CHECKBOX
    private void createProperty() {

       if (streetNumberEditText.getText().toString().trim().isEmpty() || Integer.parseInt(streetNumberEditText.getText().toString()) > 99999) {
            streetNumberEditText.setError(getString(R.string.set_error_street_number));
        } else if (priceEditText.getText().toString().trim().isEmpty() || Double.parseDouble(priceEditText.getText().toString()) > 999999999.0) {
            priceEditText.setError(getString(R.string.set_error_surface_area));
        } else if (surfaceEditText.getText().toString().trim().isEmpty() || Double.parseDouble(surfaceEditText.getText().toString()) > 1000000.0) {
            surfaceEditText.setError(getString(R.string.set_error_price));
        } else if (townNameEditText.getText().toString().trim().isEmpty()) {
            townNameEditText.setError(getString(R.string.set_error_town));
        } else if (zipCodeEditText.getText().toString().trim().isEmpty()) {
            zipCodeEditText.setError(getString(R.string.set_error_zip_code));
        } else if (streetNameEditText.getText().toString().trim().isEmpty()) {
            streetNameEditText.setError(getString(R.string.set_error_street_name));
        } else {
            pricePropertyInDollar = Double.valueOf(priceEditText.getText().toString());
            surfaceAreaProperty = Double.valueOf(surfaceEditText.getText().toString());
            streetNumber = streetNumberEditText.getText().toString();
            streetName = streetNameEditText.getText().toString();
            zipCode = zipCodeEditText.getText().toString();
            townProperty = townNameEditText.getText().toString();
            country = townNameEditText.getText().toString();
            pointOfInterest = fillPoiCheckboxList();

            callback.clickOnNextButton(typeProperty, numberRoomsInProperty, numberBathroomsInProperty, numberBedroomsInProperty, pricePropertyInDollar, surfaceAreaProperty, streetNumber, streetName, zipCode, townProperty, country,
                    pointOfInterest, addressCompl, ManageCreateUpdateChoice.getCreateUpdateChoice(context));

            startSecondPage();
        }


    }

    private void startSecondPage() {


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            callback = (CreatePropertyActivity) context;
            //dataPasser=(CreatePropertyActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnImageClickListener");
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if (parent.getId() == R.id.create_update_spinner_type_property) {
            typeProperty = typePropertySpinner.getSelectedItem().toString();
        } else if (parent.getId() == R.id.create_update_spinner_number_rooms) {
            numberRoomsInProperty = numberRoomSpinner.getSelectedItem().toString();
        } else if (parent.getId() == R.id.create_update_spinner_number_bedroom) {

            if (numberBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                numberBedroomsInProperty = 7;
            } else {
                numberBedroomsInProperty = Integer.valueOf(numberBedroomSpinner.getSelectedItem().toString());
            }


        } else if (parent.getId() == R.id.create_update_spinner_number_bathrooms) {
            numberBathroomsInProperty = numberBathroomSpinner.getSelectedItem().toString();
        }
    }

    private String fillPoiCheckboxList() {

        List<String> pointsOfInterestList = new ArrayList<>();
        //Delete all elements of the List before to verify is checkbox are checked.
        pointsOfInterestList.clear();

        // Check which checkbox was clicked
        if (shopsCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog__poi_shops));
        if (restaurantsCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog__poi_restaurants));
        if (schoolCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog__poi_school));
        if (carParksCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog_poi_car_parks));
        if (touristAttractionCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog_poi_tourist_attraction));
        if (oilStationCheckBox.isChecked())
            pointsOfInterestList.add(getString(R.string.fullscreen_dialog_poi_oil_station));

        return pointsOfInterestList.toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(context);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);

    }


}
