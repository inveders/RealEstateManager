package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;

public class UpdatePropertyActivity extends BaseActivity {

    private static long REAL_ESTATE_AGENT_ID = 1;

    Spinner typePropertySpinner;
    Spinner numberRoomSpinner;
    Spinner numberBedroomSpinner;
    Spinner numberBathroomSpinner;

    EditText priceEditText;
    EditText surfaceEditText;
    EditText streetNumberEditText;
    EditText streetNameEditText;
    EditText zipCodeEditText;
    EditText townNameEditText;
    EditText countryEditText;

    CheckBox schoolCheckBox;
    CheckBox restaurantsCheckBox;
    CheckBox carParksCheckBox;
    CheckBox shopsCheckBox;
    CheckBox touristAttractionCheckBox;
    CheckBox oilStationCheckBox;

    Button createButton;

    //String for spinners
    String typeProperty;
    int numberRoomsInProperty;
    int numberBathroomsInProperty;
    int numberBedroomsInProperty;
    double priceDollars;
    double surfaceText;
    String numberStreet;
    String NameStreet;
    String zipCode;
    String townName;
    String country;

    private PropertyViewModel propertyViewModel;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_update_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolBar();
        this.configureViewModel();

        createButton = findViewById(R.id.create_update_button);

        typePropertySpinner = findViewById(R.id.create_update_spinner_type_property);
        numberRoomSpinner = findViewById(R.id.create_update_spinner_number_rooms);
        numberBedroomSpinner = findViewById(R.id.create_update_spinner_number_bedroom);
        numberBathroomSpinner = findViewById(R.id.create_update_spinner_number_bathrooms);

        priceEditText= findViewById(R.id.create_updtate_price_edittext);
        surfaceEditText= findViewById(R.id.create_update_surface_edittext);
        streetNumberEditText= findViewById(R.id.create_update_street_number_edittext);
        streetNameEditText= findViewById(R.id.create_update_street_name_edittext);
        zipCodeEditText= findViewById(R.id.create_update_zip_code_edittext);
        townNameEditText= findViewById(R.id.create_update_town_name_edittext);
        countryEditText= findViewById(R.id.create_update_country_name_edittext);

        schoolCheckBox= findViewById(R.id.create_update_checkbox_poi_schools);
        restaurantsCheckBox= findViewById(R.id.create_update_checkbox_poi_restaurants);
        carParksCheckBox= findViewById(R.id.create_update_checkbox_poi_car_parks);
        shopsCheckBox= findViewById(R.id.create_update_checkbox_poi_shops);
        touristAttractionCheckBox= findViewById(R.id.create_update_checkbox_poi_tourist_attraction);
        oilStationCheckBox= findViewById(R.id.create_update_checkbox_poi_oil_station);

        createButton.setText(getString(R.string.update_button));
    /*    createButton.setOnClickListener(v -> updateProperty());
        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);*/



    }

    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.page_name_activity_udpdate_property);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
        getProperties(REAL_ESTATE_AGENT_ID);
    }


    // 3 - Update an property (selected or not)
    private void updateProperty(Property property) {
        property.setSelected(!property.isSelected());
        this.propertyViewModel.updateProperty(property);
    }


    // 3 - Get all properties for a real estate agent
    private void getProperties(long realEstateAgentId) {
        this.propertyViewModel.getProperties(realEstateAgentId).observe(this, properties -> {

            /**CREATE SQL FUNCTION WHO RETRIEVE ONLY ONE PROPERTY WITH IS ID*/
            /**ONLY HAVE STREET NAME, NUMBER ZIP CODE AND OWN IN DATABASE*/
            /**CREATE LISTENER ON CHANGE OF THIS ELEMENTS*/

            typeProperty=properties.get(0).getTypeProperty();
            numberRoomsInProperty=properties.get(0).getNumberRoomsInProperty();
            numberBathroomsInProperty=properties.get(0).getNumberBathroomsInProperty();
            numberBedroomsInProperty=properties.get(0).getNumberBedroomsInProperty();
            priceDollars=properties.get(0).getPricePropertyInDollar();
            surfaceText=properties.get(0).getSurfaceAreaProperty();
            numberStreet=properties.get(0).getStreeNumber();
            NameStreet=properties.get(0).getStreetName();
            zipCode=properties.get(0).getZipCode();
            townName=properties.get(0).getTownProperty();
            country=properties.get(0).getCountry();

        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if(parent.getId() == R.id.create_update_spinner_type_property)
        {
            typeProperty = typePropertySpinner.getSelectedItem().toString();
        }
        else if(parent.getId() == R.id.create_update_spinner_number_rooms)
        {
            numberRoomsInProperty = Integer.valueOf(numberRoomSpinner.getSelectedItem().toString());
        }
        else if(parent.getId() == R.id.create_update_spinner_number_bedroom)
        {
            numberBedroomsInProperty = Integer.valueOf(numberBedroomSpinner.getSelectedItem().toString());
        }
        else if(parent.getId() == R.id.create_update_spinner_number_bathrooms)
        {
            numberBathroomsInProperty = Integer.valueOf(numberBathroomSpinner.getSelectedItem().toString());
        }
    }
}
