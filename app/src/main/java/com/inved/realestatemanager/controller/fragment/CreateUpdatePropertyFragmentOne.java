package com.inved.realestatemanager.controller.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.CreatePropertyActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.CreateUpdatePropertyViewModel;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateUpdatePropertyFragmentOne extends Fragment implements AdapterView.OnItemSelectedListener {

    private CreateUpdateChangePageInterface callbackChangePage;

    private PropertyViewModel propertyViewModel;
    private CreateUpdatePropertyViewModel createUpdatePropertyViewModel;

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

    private String addressCompl = null;

    private Context context;


    public interface CreateUpdateChangePageInterface {
        void changeFragmentPage();
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

        //We check if it's a new add property or just a modification
        if (getActivity() != null) {
            context = getActivity();
        } else {
            context = MainApplication.getInstance().getApplicationContext();
        }

        this.configureViewModel();



        Log.d("debagp","Manage choice is :" +ManageCreateUpdateChoice.getCreateUpdateChoice(context));
        if (ManageCreateUpdateChoice.getCreateUpdateChoice(context) != null) {
            String propertyId = ManageCreateUpdateChoice.getCreateUpdateChoice(context);
            this.updateUIwithDataFromDatabase(propertyId);
        }


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            this.createUpdatePropertyViewModel = ViewModelProviders.of(getActivity()).get(CreateUpdatePropertyViewModel.class);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        ManageCreateUpdateChoice.saveCreateUpdateChoice(context, null);

    }

    @SuppressLint("SetTextI18n")
    private void updateUIwithDataFromDatabase(String propertyId) {
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
            typePropertySpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
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
        Log.d("debago","in splitPOI inCheckbox "+poiList);
        if (poiList.contains(getString(R.string.fullscreen_dialog__poi_school))) {
            Log.d("debago","school checkbox is ok");
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
            double pricePropertyInDollar = Double.valueOf(priceEditText.getText().toString());
            double surfaceAreaProperty = Double.valueOf(surfaceEditText.getText().toString());
            String streetNumber = streetNumberEditText.getText().toString();
            String streetName = streetNameEditText.getText().toString();
            String zipCode = zipCodeEditText.getText().toString();
            String townProperty = townNameEditText.getText().toString();
            String country = countryEditText.getText().toString();
            String pointOfInterest = fillPoiCheckboxList();

            List<Object> myList = new ArrayList<>();
            myList.add(typeProperty);
            myList.add(numberRoomsInProperty);
            myList.add(numberBathroomsInProperty);
            myList.add(numberBedroomsInProperty);
            myList.add(pricePropertyInDollar);
            myList.add(surfaceAreaProperty);
            myList.add(streetNumber);
            myList.add(streetName);
            myList.add(zipCode);
            myList.add(townProperty);
            myList.add(country);
            myList.add(pointOfInterest);
            myList.add(addressCompl);
            myList.add(ManageCreateUpdateChoice.getCreateUpdateChoice(context));

            createUpdatePropertyViewModel.setMyData(myList);

            callbackChangePage.changeFragmentPage();

        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            callbackChangePage = (CreatePropertyActivity) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCreatePropertyInterface");
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

        String myList;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myList = String.join(",", pointsOfInterestList);
        }else{
            myList=joinMethod(pointsOfInterestList);
        }

        return myList;
    }

    private static String joinMethod(List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.size(); i++) {

            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(",");
            }

        }

        return sb.toString();

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
