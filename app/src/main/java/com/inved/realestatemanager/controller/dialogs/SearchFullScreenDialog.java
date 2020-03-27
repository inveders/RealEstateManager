package com.inved.realestatemanager.controller.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.apptik.widget.MultiSlider;

public class SearchFullScreenDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    //Widget

    private Spinner typePropertySpinner;
    private AutoCompleteTextView townPropertyAutocomplete;
    private MultiSlider surfaceSeekbar;
    private TextView surfaceMinValue;
    private TextView surfaceMaxValue;
    private MultiSlider priceSeekbar;
    private TextView priceMinValue;
    private TextView priceMaxValue;
    private Spinner minBedroomSpinner;
    private Spinner maxBedroomSpinner;
    private AutoCompleteTextView countryAutocomplete;
    private Spinner statusSpinner;
    private Spinner realEstateAgentNameSpinner;

    //String for Data
    private static final int MAX_PRICE_PROPERTY = 2000000;
    private static final int MAX_SURFACE_PROPERTY = 500;

    private String mTypeProperty = null;
    private int mMinBedroom = 0;
    private int mMaxBedroom = 7;
    private String mStatus = null;
    private String mRealEstateAgentName = null;
    private double minSurface = 0;
    private double maxSurface = 99999;
    private double minPrice = 0;
    private double maxPrice = 3000000;
    private List<String> spinnerAgentList = new ArrayList<>();
    private String realEstateAgentId = null;
    //Interface
    private OnClickSearchInterface callback;

    private UnitConversion unitConversion = new UnitConversion();


    //View Model
    private PropertyViewModel propertyViewModel;

    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_search_full_screen_dialog, container, false);

        //declarations
        typePropertySpinner = view.findViewById(R.id.dialog_spinner_type_property);
        townPropertyAutocomplete = view.findViewById(R.id.dialog_town_autocompleteText);
        surfaceSeekbar = view.findViewById(R.id.dialog_seekbar_surface);
        surfaceMinValue = view.findViewById(R.id.dialog_surface_left_value);
        surfaceMaxValue = view.findViewById(R.id.dialog_surface_right_value);
        priceSeekbar = view.findViewById(R.id.dialog_seekbar_price);
        priceMinValue = view.findViewById(R.id.dialog_price_left_value);
        priceMaxValue = view.findViewById(R.id.dialog_price_right_value);
        minBedroomSpinner = view.findViewById(R.id.dialog_spinner_number_bedroom_min);
        maxBedroomSpinner = view.findViewById(R.id.dialog_spinner_number_bedroom_max);
        countryAutocomplete = view.findViewById(R.id.dialog_country_autocompleteText);
        statusSpinner = view.findViewById(R.id.dialog_spinner_status);
        realEstateAgentNameSpinner = view.findViewById(R.id.dialog_spinner_agent_name);
        TextView searchActionButton = view.findViewById(R.id.fullscreen_dialog_launch_search);
        ImageButton cancelSearchButton = view.findViewById(R.id.fullscreen_dialog_close);

        callback = (OnClickSearchInterface) getTargetFragment();
        spinnerAgentList.add(getString(R.string.select_agent));


        //Spinner step 1/3 Initialize all spinners to be selected
        realEstateAgentNameSpinner.setOnItemSelectedListener(this);
        statusSpinner.setOnItemSelectedListener(this);
        typePropertySpinner.setOnItemSelectedListener(this);
        minBedroomSpinner.setOnItemSelectedListener(this);
        maxBedroomSpinner.setOnItemSelectedListener(this);

        this.configureViewModel();
        this.retriveRealEstateAgentsForSpinner();
        this.seekbarChangements();
        if (getDialog() != null) {
            getDialog().setTitle(getString(R.string.page_name_search_dialog));
        }

        cancelSearchButton.setOnClickListener(v -> {
            getDialog().cancel();


            if(callback!=null){
                callback.cancelButton();
            }else{
                if(getActivity()!=null){
                    ((ListPropertyActivity) getActivity()).cancelDialog();
                }
            }
        });
        searchActionButton.setOnClickListener(v -> this.startSearchProperty());
        return view;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }


    // --------------
    // UI
    // --------------

    private void seekbarChangements() {

        Utils utils = new Utils();

        new Thread(() -> {
            double maxPriceFromRoom = propertyViewModel.getMaxPrice();
            int maxPriceConvertInInt = utils.getPriceInGoodCurrencyIntType(maxPriceFromRoom);
            priceSeekbar.setMax(maxPriceConvertInInt);
        }).start();

        new Thread(() -> {
            int maxSurfaceFromRoom = propertyViewModel.getMaxSurface();
            surfaceSeekbar.setMax(maxSurfaceFromRoom);
        }).start();

        priceSeekbar.setMax(MAX_PRICE_PROPERTY);
        surfaceSeekbar.setMax(MAX_SURFACE_PROPERTY);
        surfaceSeekbar.getThumb(1).setValue(surfaceSeekbar.getMax());
        priceSeekbar.getThumb(1).setValue(priceSeekbar.getMax());

        priceMinValue.setText(unitConversion.changeIntToStringWithThousandSeparator(priceSeekbar.getThumb(0).getValue()));
        priceMaxValue.setText(unitConversion.changeIntToStringWithThousandSeparator(priceSeekbar.getMax()));

        surfaceMinValue.setText(unitConversion.changeIntToStringWithThousandSeparator(surfaceSeekbar.getThumb(0).getValue()));
        surfaceMaxValue.setText(unitConversion.changeIntToStringWithThousandSeparator(surfaceSeekbar.getMax()));

        surfaceSeekbar.setOnThumbValueChangeListener((multiSlider, thumb, thumbIndex, value) -> {
            if (thumbIndex == 0) {
                surfaceMinValue.setText(unitConversion.changeIntToStringWithThousandSeparator(value));
                minSurface = value;
            } else {
                surfaceMaxValue.setText(unitConversion.changeIntToStringWithThousandSeparator(value));
                maxSurface = value;
            }
        });

        priceSeekbar.setOnThumbValueChangeListener((multiSlider, thumb, thumbIndex, value) -> {
            if (thumbIndex == 0) {
                minPrice = value;
                priceMinValue.setText(unitConversion.changeIntToStringWithThousandSeparator(value));
            } else {
                priceMaxValue.setText(unitConversion.changeIntToStringWithThousandSeparator(value));
                maxPrice = value;
            }
        });
    }

    // Update the list of Real Estate item
    private void updateRealEstateItemsList(List<Property> properties) {

        if(callback!=null){
            callback.searchButton(properties);
        }else{
            if(getActivity()!=null){
                ((ListPropertyActivity) getActivity()).refreshFragmentAfterSearch(properties);
            }
        }
    }

    // --------------
    // SPINNERS
    // --------------

    //Spinner step 2/3 : implement methods onItemSelected and onNothingSelected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using

        if (parent.getId() == R.id.dialog_spinner_type_property) {
            mTypeProperty = typePropertySpinner.getSelectedItem().toString();

            if (mTypeProperty.equals(getString(R.string.select_type_property))) {
                mTypeProperty = null;
            }
        } else if (parent.getId() == R.id.dialog_spinner_status) {
            mStatus = statusSpinner.getSelectedItem().toString();

            if (mStatus.equals(getString(R.string.select_status))) {
                mStatus = null;
            }

        } else if (parent.getId() == R.id.dialog_spinner_number_bedroom_min) {

            if (minBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                mMinBedroom = 7;
            } else {
                mMinBedroom = Integer.parseInt(minBedroomSpinner.getSelectedItem().toString());
            }

        } else if (parent.getId() == R.id.dialog_spinner_number_bedroom_max) {

            if (maxBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                mMaxBedroom = 7;
            } else {
                mMaxBedroom = Integer.parseInt(maxBedroomSpinner.getSelectedItem().toString());
            }

        } else if (parent.getId() == R.id.dialog_spinner_agent_name) {

            mRealEstateAgentName = realEstateAgentNameSpinner.getSelectedItem().toString();
            if (mRealEstateAgentName.equals(getString(R.string.select_agent))) {
                mRealEstateAgentName = null;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Spinner step 3/3 : retrieve all agents in database and fill spinner with them
    private void retriveRealEstateAgentsForSpinner() {
        if (propertyViewModel.getAllRealEstateAgents() != null) {
            propertyViewModel.getAllRealEstateAgents().observe(getViewLifecycleOwner(), realEstateAgents -> {
                for (RealEstateAgents list : realEstateAgents) {
                    String firstname = list.getFirstname();
                    String lastname = list.getLastname();
                    String agentFirstnameLastname = firstname + " " + lastname;
                    spinnerAgentList.add(agentFirstnameLastname);
                }


            });

            //I fill agent spinner with firstname and lastname of the database programatically

            if (getActivity() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_spinner_item, spinnerAgentList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                realEstateAgentNameSpinner.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

        }
    }

    // --------------
    // SEARCH BUTTON ACTIONS
    // --------------

    private void startSearchProperty() {

        if (getDialog() != null) {

            if (mMinBedroom > mMaxBedroom) {
                Toast.makeText(getContext(), getString(R.string.Minbedroom_Maxbedoorm_control), Toast.LENGTH_SHORT).show();

            } else {
                String town = !townPropertyAutocomplete.getText().toString().equals("") ? townPropertyAutocomplete.getText().toString() : null;
                String country = !countryAutocomplete.getText().toString().equals("") ? countryAutocomplete.getText().toString() : null;

                if (mRealEstateAgentName != null) {
                    SplitString splitString = new SplitString();
                    String firstname = splitString.splitStringWithSpace(mRealEstateAgentName, 0);
                    String lastname = splitString.splitStringWithSpace(mRealEstateAgentName, 1);
                    propertyViewModel.getRealEstateAgentByName(firstname, lastname).observe(getViewLifecycleOwner(), realEstateAgents -> {
                        realEstateAgentId = realEstateAgents.getRealEstateAgentId();

                        this.propertyViewModel.searchProperty(mTypeProperty, town, minSurface, maxSurface, minPrice, maxPrice,
                                mMinBedroom, mMaxBedroom, country, mStatus, realEstateAgentId)
                                .observe(getViewLifecycleOwner(), this::updateRealEstateItemsList);
                    });


                } else {
                    this.propertyViewModel.searchProperty(mTypeProperty, town, minSurface, maxSurface, minPrice, maxPrice,
                            mMinBedroom, mMaxBedroom, country, mStatus, realEstateAgentId)
                            .observe(getViewLifecycleOwner(), this::updateRealEstateItemsList);
                }


                getDialog().dismiss();
            }


        }

    }

    // --------------
    // INTERFACE
    // --------------

    public interface OnClickSearchInterface {

        void searchButton(List<Property> properties);

        void cancelButton();
    }


}