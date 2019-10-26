package com.inved.realestatemanager.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

public class CreatePropertyActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

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

    CheckBox schoolCheckBox;
    CheckBox restaurantsCheckBox;
    CheckBox carParksCheckBox;
    CheckBox shopsCheckBox;
    CheckBox touristAttractionCheckBox;
    CheckBox oilStationCheckBox;

    //String for spinners
    String typeProperty=null;
    int numberRoomsInProperty=0;
    int numberBathroomsInProperty=0;
    int numberBedroomsInProperty=0;

    Button createButton;

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

        schoolCheckBox= findViewById(R.id.create_update_checkbox_poi_schools);
        restaurantsCheckBox= findViewById(R.id.create_update_checkbox_poi_restaurants);
        carParksCheckBox= findViewById(R.id.create_update_checkbox_poi_car_parks);
        shopsCheckBox= findViewById(R.id.create_update_checkbox_poi_shops);
        touristAttractionCheckBox= findViewById(R.id.create_update_checkbox_poi_tourist_attraction);
        oilStationCheckBox= findViewById(R.id.create_update_checkbox_poi_oil_station);
        
        createButton.setText(getString(R.string.create_button));
        createButton.setOnClickListener(v -> createProperty());
        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);

    }

    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.page_name_activity_create_property);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    // Create a new property A TERMINER AVEC LES CONTROLES, SURTOUT SUR L'APPUI DU BOUTON, RAJOUTER LES CHAMPS OBLIGATOIRES, VOIR COMMENT PASSER A UNE AUTRE PAGE
    //VOIR QUE FAIRE DES CHECKBOX
    private void createProperty() {

        double pricePropertyInDollar = Double.valueOf(priceEditText.getText().toString());
        double surfaceAreaProperty = Double.valueOf(surfaceEditText.getText().toString());
        String fullDescriptionProperty = "";
        String photoDescription = null;
        String addressProperty = streetNumberEditText.getText().toString() + " " + streetNameEditText.getText().toString() + " " + zipCodeEditText.getText().toString() + " " + townNameEditText.getText().toString();
        String townProperty = townNameEditText.getText().toString();
        String statusProperty = "";
        String dateOfEntryOnMarketForProperty = "";
        String dateOfSaleForPorperty = "";
        boolean selected = false;
        int realEstateAgentId = 1; //CHANGE AFTER

        Property newProperty = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty,
                photoDescription, addressProperty,townProperty,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected, realEstateAgentId);

        this.propertyViewModel.createProperty(newProperty);

        Toast.makeText(this, getString(R.string.create_update_creation_confirmation), Toast.LENGTH_SHORT).show();
        startMainActivity();

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

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }

    // CHECKBOX CLICK
    public void onCheckboxClicked(View view) {

        ((CheckBox) view).setOnCheckedChangeListener((compoundButton, b) -> {

        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
