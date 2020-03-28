package com.inved.realestatemanager.controller.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.lifecycle.ViewModelProvider;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.CreatePropertyActivity;
import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.domain.JoinCheckBoxInString;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.CreateUpdatePropertyViewModel;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CreateUpdatePropertyFragmentOne extends Fragment implements AdapterView.OnItemSelectedListener,TextWatcher {

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
    private EditText additionnalEditText;
    private AutoCompleteTextView countryEditText;

    private CheckBox schoolCheckBox;
    private CheckBox restaurantsCheckBox;
    private CheckBox carParksCheckBox;
    private CheckBox shopsCheckBox;
    private CheckBox touristAttractionCheckBox;
    private CheckBox oilStationCheckBox;

    private JoinCheckBoxInString joinCheckBoxInString;
    //String for spinners
    private String typeProperty = null;
    private String numberRoomsInProperty = "0";
    private String numberBathroomsInProperty = "0";
    private int numberBedroomsInProperty = 0;
    private GetSpinner getSpinner=new GetSpinner();

    private Context context;
    private Utils utils;
    private boolean tabletSize;



    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_one, container, false);
        Button nextButton = v.findViewById(R.id.create_update_next_button);
        utils = new Utils();
        if (getContext() != null)
            tabletSize = getContext().getResources().getBoolean(R.bool.isTablet);
        joinCheckBoxInString = new JoinCheckBoxInString();
        typePropertySpinner = v.findViewById(R.id.create_update_spinner_type_property);
        numberRoomSpinner = v.findViewById(R.id.create_update_spinner_number_rooms);
        numberBedroomSpinner = v.findViewById(R.id.create_update_spinner_number_bedroom);
        numberBathroomSpinner = v.findViewById(R.id.create_update_spinner_number_bathrooms);

        priceEditText = v.findViewById(R.id.create_updtate_price_edittext);
        surfaceEditText = v.findViewById(R.id.create_update_surface_edittext);
        streetNumberEditText = v.findViewById(R.id.create_update_street_number_edittext);
        additionnalEditText = v.findViewById(R.id.create_update_street_compl_edittext);
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

        nextButton.setOnClickListener(view -> createProperty());

        priceEditText.setHint(getString(R.string.create_update_by_price_hint,utils.goodCurrencyUnit()));
        this.spinnerInitialization();
        this.textWatcherInitialization();

        this.configureViewModel();

        //We check if it's a new add property or just a modification
        if (getActivity() != null) {
            context = getActivity();
        } else if(getContext()!=null) {
            context = getContext();
        }else {
            context = MainApplication.getInstance().getApplicationContext();
        }



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ManageCreateUpdateChoice.getCreateUpdateChoice(context) != null) {
            String propertyId = ManageCreateUpdateChoice.getCreateUpdateChoice(context);
            this.updateUIwithDataFromDatabase(propertyId);
        }
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(context);
        this.propertyViewModel = new ViewModelProvider(this, mViewModelFactory).get(PropertyViewModel.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            this.createUpdatePropertyViewModel = new ViewModelProvider(getActivity()).get(CreateUpdatePropertyViewModel.class);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(!tabletSize){
            ManageCreateUpdateChoice.saveCreateUpdateChoice(context, null);
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

    // --------------
    // Initialization
    // --------------

    private void textWatcherInitialization(){
        //Texwatcher initialization
        priceEditText.addTextChangedListener(this);
        surfaceEditText.addTextChangedListener(this);
        streetNumberEditText.addTextChangedListener(this);
        additionnalEditText.addTextChangedListener(this);
        streetNameEditText.addTextChangedListener(this);
        zipCodeEditText.addTextChangedListener(this);
        townNameEditText.addTextChangedListener(this);
        countryEditText.addTextChangedListener(this);
    }

    private void spinnerInitialization(){
        //Spinner initialization
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterCountry =
                new ArrayAdapter<>(MainApplication.getInstance().getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        countryEditText.setAdapter(adapterCountry);


        typePropertySpinner.setOnItemSelectedListener(this);
        numberRoomSpinner.setOnItemSelectedListener(this);
        numberBedroomSpinner.setOnItemSelectedListener(this);
        numberBathroomSpinner.setOnItemSelectedListener(this);
    }

    // --------------
    // CREATE PROPERTY
    // --------------

    private void createProperty() {

        if (streetNumberEditText.getText().toString().trim().isEmpty() || Integer.parseInt(streetNumberEditText.getText().toString()) > 99999) {
            streetNumberEditText.setError(getString(R.string.set_error_street_number));
        } else if (priceEditText.getText().toString().trim().isEmpty() || Double.parseDouble(priceEditText.getText().toString()) > 999999999.0) {
            priceEditText.setError(getString(R.string.set_error_price));
        } else if (surfaceEditText.getText().toString().trim().isEmpty() || Double.parseDouble(surfaceEditText.getText().toString()) > 1000000.0) {
            surfaceEditText.setError(getString(R.string.set_error_surface_area));
        } else if (townNameEditText.getText().toString().trim().isEmpty()) {
            townNameEditText.setError(getString(R.string.set_error_town));
        } else if (zipCodeEditText.getText().toString().trim().isEmpty()) {
            zipCodeEditText.setError(getString(R.string.set_error_zip_code));
        } else if (streetNameEditText.getText().toString().trim().isEmpty()) {
            streetNameEditText.setError(getString(R.string.set_error_street_name));
        } else {
            double price = Double.parseDouble(priceEditText.getText().toString());
            double surfaceAreaProperty = Double.parseDouble(surfaceEditText.getText().toString());
            String streetNumber = streetNumberEditText.getText().toString();
            String streetName = streetNameEditText.getText().toString();
            String zipCode = zipCodeEditText.getText().toString();
            String townProperty = townNameEditText.getText().toString();
            String country = countryEditText.getText().toString();
            String pointOfInterest = joinCheckBoxInString.fillPoiCheckboxList(shopsCheckBox,restaurantsCheckBox,schoolCheckBox,carParksCheckBox,touristAttractionCheckBox,oilStationCheckBox);
            String additionnal = additionnalEditText.getText().toString();

            List<Object> myList = new ArrayList<>();
            myList.add(typeProperty);
            myList.add(numberRoomsInProperty);
            myList.add(numberBathroomsInProperty);
            myList.add(numberBedroomsInProperty);
            myList.add(utils.savePriceInEuro(price));
            myList.add(surfaceAreaProperty);
            myList.add(streetNumber);
            myList.add(streetName);
            myList.add(zipCode);
            myList.add(townProperty);
            myList.add(country);
            myList.add(pointOfInterest);
            myList.add(additionnal);
            myList.add(ManageCreateUpdateChoice.getCreateUpdateChoice(context));

            createUpdatePropertyViewModel.setMyData(myList);

            callbackChangePage.changeFragmentPage();

        }

    }

    // --------------
    // UPDATE PROPERTY
    // --------------

    @SuppressLint("SetTextI18n")
    private void updateUIwithDataFromDatabase(String propertyId) {

        propertyViewModel.getOneProperty(propertyId).observe(getViewLifecycleOwner(), property -> {

            priceEditText.setText(utils.getPriceInGoodCurrencyDoubleType(property.getPricePropertyInEuro()));
            surfaceEditText.setText(Double.toString(property.getSurfaceAreaProperty()));
            streetNumberEditText.setText(property.getStreetNumber());
            streetNameEditText.setText(property.getStreetName());
            zipCodeEditText.setText(property.getZipCode());
            townNameEditText.setText(property.getTownProperty());
            countryEditText.setText(property.getCountry());
            joinCheckBoxInString.splitPoiInCheckbox(property.getPointOfInterest(),shopsCheckBox,restaurantsCheckBox,schoolCheckBox,carParksCheckBox,touristAttractionCheckBox,oilStationCheckBox);
            typePropertySpinner.setSelection(getSpinner.getIndexSpinner(typePropertySpinner, property.getTypeProperty()));
            typePropertySpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            numberRoomSpinner.setSelection(getSpinner.getIndexSpinner(numberRoomSpinner, property.getNumberRoomsInProperty()));
            numberBedroomSpinner.setSelection(getSpinner.getIndexSpinnerInt(numberBedroomSpinner, property.getNumberBedroomsInProperty()));
            numberBathroomSpinner.setSelection(getSpinner.getIndexSpinner(numberBathroomSpinner, property.getNumberBathroomsInProperty()));

        });
    }




    // --------------
    // SPINNER
    // --------------


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if (parent.getId() == R.id.create_update_spinner_type_property) {
            typeProperty = typePropertySpinner.getSelectedItem().toString();
            if(typePropertySpinner.getSelectedItem().toString().equals(getString(R.string.select_type_property))){
                typePropertySpinner.setBackgroundResource(R.drawable.edit_text_design);
            }else{
                typePropertySpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            }
        } else if (parent.getId() == R.id.create_update_spinner_number_rooms) {


            numberRoomsInProperty = numberRoomSpinner.getSelectedItem().toString();

            if(numberRoomSpinner.getSelectedItem().toString().equals("0")){
                numberRoomSpinner.setBackgroundResource(R.drawable.edit_text_design);
            }else{
                numberRoomSpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            }

        } else if (parent.getId() == R.id.create_update_spinner_number_bedroom) {

            if (numberBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                numberBedroomsInProperty = 7;
                numberBedroomSpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            } else if(numberBedroomSpinner.getSelectedItem().toString().equals("0")){
                numberBedroomSpinner.setBackgroundResource(R.drawable.edit_text_design);
            }else {
                numberBedroomsInProperty = Integer.parseInt(numberBedroomSpinner.getSelectedItem().toString());
                numberBedroomSpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            }

        } else if (parent.getId() == R.id.create_update_spinner_number_bathrooms) {

            numberBathroomsInProperty = numberBathroomSpinner.getSelectedItem().toString();
            if(numberBathroomSpinner.getSelectedItem().toString().equals("0")){
                numberBathroomSpinner.setBackgroundResource(R.drawable.edit_text_design);
            }else{
                numberBathroomSpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            }

        }
    }



    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    // --------------
    // TEXTWATCHER
    // --------------

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if(i2==0){
            //put backgroung grey when we remove all text

            if (priceEditText.getText().hashCode() == charSequence.hashCode()){
                priceEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (surfaceEditText.getText().hashCode() == charSequence.hashCode()){
                surfaceEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (streetNumberEditText.getText().hashCode() == charSequence.hashCode()){
                streetNumberEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (additionnalEditText.getText().hashCode() == charSequence.hashCode()){
                additionnalEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (streetNameEditText.getText().hashCode() == charSequence.hashCode()){
                streetNameEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (zipCodeEditText.getText().hashCode() == charSequence.hashCode()){
                zipCodeEditText.setBackgroundResource(R.drawable.edit_text_design);
            }else if (townNameEditText.getText().hashCode() == charSequence.hashCode()){
                townNameEditText.setBackgroundResource(R.drawable.edit_text_design);
            }
            else if (countryEditText.getText().hashCode() == charSequence.hashCode()){
                countryEditText.setBackgroundResource(R.drawable.edit_text_design);
            }
        }


    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && !editable.toString().equalsIgnoreCase("")){
            // Checking editable.hashCode() to understand which edittext is using right now

            if (priceEditText.getText().hashCode() == editable.hashCode()){

                String value = editable.toString();
                priceEditText.removeTextChangedListener(this);
                // priceEditText.setText(value);
                if (value.length() == 0) {
                    // put backgroung grey when there is no action
                    priceEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {
                    // put backgroung white when there write
                    priceEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                priceEditText.addTextChangedListener(this);
            }else if (surfaceEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                surfaceEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    surfaceEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    surfaceEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                surfaceEditText.addTextChangedListener(this);
            }else if (streetNumberEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                streetNumberEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    streetNumberEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    streetNumberEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                streetNumberEditText.addTextChangedListener(this);
            }else if (additionnalEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                additionnalEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    additionnalEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    additionnalEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                additionnalEditText.addTextChangedListener(this);
            }else if (streetNameEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                streetNameEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    streetNameEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    streetNameEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                streetNameEditText.addTextChangedListener(this);
            }else if (zipCodeEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                zipCodeEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    zipCodeEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    zipCodeEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                zipCodeEditText.addTextChangedListener(this);
            }else if (townNameEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                townNameEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    townNameEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    townNameEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                townNameEditText.addTextChangedListener(this);
            }
            else if (countryEditText.getText().hashCode() == editable.hashCode()){
                String value = editable.toString();
                countryEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    countryEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    countryEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                countryEditText.addTextChangedListener(this);
            }
        }
    }


    // --------------
    // INTERFACE
    // --------------

    public interface CreateUpdateChangePageInterface {
        void changeFragmentPage();
    }


}
