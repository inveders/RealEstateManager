package com.inved.realestatemanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

public class CreateUpdatePropertyFragmentOne extends Fragment implements AdapterView.OnItemSelectedListener {

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

    private CheckBox schoolCheckBox;
    private CheckBox restaurantsCheckBox;
    private CheckBox carParksCheckBox;
    private CheckBox shopsCheckBox;
    private CheckBox touristAttractionCheckBox;
    private CheckBox oilStationCheckBox;

    //String for spinners
    private String typeProperty = null;
    private int numberRoomsInProperty = 0;
    private int numberBathroomsInProperty = 0;
    private int numberBedroomsInProperty = 0;

    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_one, container, false);
        nextButton = v.findViewById(R.id.create_update_next_button);

        typePropertySpinner = v.findViewById(R.id.create_update_spinner_type_property);
        numberRoomSpinner = v.findViewById(R.id.create_update_spinner_number_rooms);
        numberBedroomSpinner = v.findViewById(R.id.create_update_spinner_number_bedroom);
        numberBathroomSpinner = v.findViewById(R.id.create_update_spinner_number_bathrooms);

        priceEditText= v.findViewById(R.id.create_updtate_price_edittext);
        surfaceEditText= v.findViewById(R.id.create_update_surface_edittext);
        streetNumberEditText= v.findViewById(R.id.create_update_street_number_edittext);
        streetNameEditText= v.findViewById(R.id.create_update_street_name_edittext);
        zipCodeEditText= v.findViewById(R.id.create_update_zip_code_edittext);
        townNameEditText= v.findViewById(R.id.create_update_town_name_edittext);

        schoolCheckBox= v.findViewById(R.id.create_update_checkbox_poi_schools);
        restaurantsCheckBox= v.findViewById(R.id.create_update_checkbox_poi_restaurants);
        carParksCheckBox= v.findViewById(R.id.create_update_checkbox_poi_car_parks);
        shopsCheckBox= v.findViewById(R.id.create_update_checkbox_poi_shops);
        touristAttractionCheckBox= v.findViewById(R.id.create_update_checkbox_poi_tourist_attraction);
        oilStationCheckBox= v.findViewById(R.id.create_update_checkbox_poi_oil_station);

        nextButton.setOnClickListener(view -> createProperty());
        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);


        this.configureViewModel();

        return v ;
    }

    // Create a new property A TERMINER AVEC LES CONTROLES, SURTOUT SUR L'APPUI DU BOUTON, RAJOUTER LES CHAMPS OBLIGATOIRES, VOIR COMMENT PASSER A UNE AUTRE PAGE
    //VOIR QUE FAIRE DES CHECKBOX
    private void createProperty() {

        double pricePropertyInDollar = Double.valueOf(priceEditText.getText().toString());
        double surfaceAreaProperty = Double.valueOf(surfaceEditText.getText().toString());
        String fullDescriptionProperty = "";
        String photoDescription = null;
        String streetNumber = streetNumberEditText.getText().toString();
        String streetName =streetNameEditText.getText().toString();
        String zipCode = zipCodeEditText.getText().toString();
        String townProperty = townNameEditText.getText().toString();
        String country = townNameEditText.getText().toString();
        String statusProperty = "";
        String dateOfEntryOnMarketForProperty = "";
        String dateOfSaleForPorperty = "";
        boolean selected = false;
        int realEstateAgentId = 1; //CHANGE AFTER

        Property newProperty = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty,
                photoDescription, streetNumber,streetName,zipCode,townProperty,country,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected, realEstateAgentId);

        RealEstateAgents newAgent= new RealEstateAgents(REAL_ESTATE_AGENT_ID,"Alexandra","Gnimadi","http://mikoumusique.com");

        this.propertyViewModel.createProperty(newProperty);
        this.propertyViewModel.createRealEstateAgent(newAgent);

        Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }


}
