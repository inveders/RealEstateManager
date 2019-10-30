package com.inved.realestatemanager.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.ArrayList;
import java.util.List;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

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
    private String numberBedroomsInProperty = "0";

    public interface CreateUpdateInterface {
        void clickOnNextButton(String typeProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                               String numberBedroomsInProperty, double pricePropertyInDollar, double surfaceAreaProperty,
                               String streetNumber, String streetName, String zipCode, String townProperty, String country,
                               String pointOfInterest, String addressProperty, long realEstateAgentId);

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

        schoolCheckBox = v.findViewById(R.id.create_update_checkbox_poi_schools);
        restaurantsCheckBox = v.findViewById(R.id.create_update_checkbox_poi_restaurants);
        carParksCheckBox = v.findViewById(R.id.create_update_checkbox_poi_car_parks);
        shopsCheckBox = v.findViewById(R.id.create_update_checkbox_poi_shops);
        touristAttractionCheckBox = v.findViewById(R.id.create_update_checkbox_poi_tourist_attraction);
        oilStationCheckBox = v.findViewById(R.id.create_update_checkbox_poi_oil_station);

        nextButton.setOnClickListener(view -> createProperty());
        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);


        this.configureViewModel();

        return v;
    }

    // Create a new property A TERMINER AVEC LES CONTROLES, SURTOUT SUR L'APPUI DU BOUTON, RAJOUTER LES CHAMPS OBLIGATOIRES, VOIR COMMENT PASSER A UNE AUTRE PAGE
    //VOIR QUE FAIRE DES CHECKBOX
    private void createProperty() {

        double pricePropertyInDollar = Double.valueOf(priceEditText.getText().toString());
        double surfaceAreaProperty = Double.valueOf(surfaceEditText.getText().toString());
        String streetNumber = streetNumberEditText.getText().toString();
        String streetName = streetNameEditText.getText().toString();
        String zipCode = zipCodeEditText.getText().toString();
        String townProperty = townNameEditText.getText().toString();
        String country = townNameEditText.getText().toString();
        String pointOfInterest = fillPoiCheckboxList();
        String addressProperty = streetNumber + " " + streetName + " " + zipCode + " " + townProperty + "," + country;
        long realEstateAgentId = 2; /**CHANGE AFTER*/


        Log.d("debago","callback before ");
        callback.clickOnNextButton(typeProperty, numberRoomsInProperty, numberBathroomsInProperty, numberBedroomsInProperty, pricePropertyInDollar, surfaceAreaProperty, streetNumber, streetName, zipCode, townProperty, country,
                pointOfInterest, addressProperty, realEstateAgentId);

        Toast.makeText(getContext(), "NextPage", Toast.LENGTH_SHORT).show();
        startSecondPage();

    }

    private void startSecondPage() {



    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            callback = (CreatePropertyActivity) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement OnImageClickListener");
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
            numberBedroomsInProperty = numberBedroomSpinner.getSelectedItem().toString();
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
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);

    }




}
