package com.inved.realestatemanager.controller.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.property.PropertyViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.apptik.widget.MultiSlider;

public class SearchFullScreenDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static final int USER_ID = 1;
    private static final String TAG = "CustomSearchDialog";


    //Widget
    @BindView(R.id.dialog_spinner_type_property)
    Spinner typePropertySpinner;

    @BindView(R.id.dialog_town_autocompleteText)
    AutoCompleteTextView townPropertyAutocomplete;

    @BindView(R.id.dialog_seekbar_surface)
    MultiSlider surfaceSeekbar;

    @BindView(R.id.dialog_surface_left_value)
    TextView surfaceMinValue;

    @BindView(R.id.dialog_surface_right_value)
    TextView surfaceMaxValue;

    @BindView(R.id.dialog_seekbar_price)
    MultiSlider priceSeekbar;

    @BindView(R.id.dialog_price_left_value)
    TextView priceMinValue;

    @BindView(R.id.dialog_price_right_value)
    TextView priceMaxValue;

    @BindView(R.id.dialog_spinner_number_bedroom_min)
    Spinner minBedroomSpinner;

    @BindView(R.id.dialog_spinner_number_bedroom_max)
    Spinner maxBedroomSpinner;

    @BindView(R.id.dialog_country_autocompleteText)
    AutoCompleteTextView countryAutocomplete;

    @BindView(R.id.dialog_spinner_status)
    Spinner statusSpinner;

    @BindView(R.id.dialog_spinner_agent_name)
    Spinner realEstateAgentNameSpinner;

    @BindView(R.id.fullscreen_dialog_launch_search)
    TextView searchActionButton;

    @BindView(R.id.fullscreen_dialog_close)
    ImageButton cancelSearchButton;

    //String for Data
    private String mTypeProperty = "%";
    private int mMinBedroom = 0;
    private int mMaxBedroom = 0;
    private String mStatus = "%";
    private long mRealEstateAgentName = 1;
    private double minSurface = 0;
    private double maxSurface = 99999;
    private double minPrice = 0;
    private double maxPrice=30000;

    //View Model
    private PropertyViewModel propertyViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_search_full_screen_dialog, container, false);
        ButterKnife.bind(this, view);

        this.configureViewModel();
        this.getRealEstateItems(USER_ID);
        this.seekbarChangements();
        if(getDialog()!=null){
            getDialog().setTitle("Search property");
        }

        cancelSearchButton.setOnClickListener(v -> getDialog().cancel());
        searchActionButton.setOnClickListener(v -> this.startSearchProperty());
        return view;
    }

    private void seekbarChangements() {

        priceMinValue.setText(String.valueOf(priceSeekbar.getThumb(0).getValue()));
        priceMaxValue.setText(String.valueOf(priceSeekbar.getThumb(1).getValue()));

        surfaceMinValue.setText(String.valueOf(surfaceSeekbar.getThumb(0).getValue()));
        surfaceMaxValue.setText(String.valueOf(surfaceSeekbar.getThumb(1).getValue()));

        surfaceSeekbar.setOnThumbValueChangeListener((multiSlider, thumb, thumbIndex, value) -> {
            if (thumbIndex == 0) {
                surfaceMinValue.setText(String.valueOf(value));
                minSurface=value;
            } else {
                surfaceMaxValue.setText(String.valueOf(value));
                maxSurface=value;
            }
        });

        priceSeekbar.setOnThumbValueChangeListener((multiSlider, thumb, thumbIndex, value) -> {
            if (thumbIndex == 0) {
                minPrice=value;
                priceMinValue.setText(String.valueOf(value));
            } else {
                priceMaxValue.setText(String.valueOf(value));
                maxPrice=value;
            }
        });
    }

    // --------------
    // UI
    // --------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);
    }

    // Get all items for a user
    private void getRealEstateItems(long userId) {
        this.propertyViewModel.getProperties(userId).observe(this, this::updateRealEstateItemsList);
    }

    // Update the list of Real Estate item
    private void updateRealEstateItemsList(List<com.inved.realestatemanager.models.Property> properties) {
        ListPropertyFragment.adapter.updateData(properties);

        Log.d(TAG, "updateRealEstateItemsList: data size = " + properties.size());

    }

    // --------------
    // Action
    // --------------

    private void startSearchProperty() {

        if(getDialog()!=null){
            String town = !townPropertyAutocomplete.getText().toString().equals("") ? townPropertyAutocomplete.getText().toString() : "%";
            String country = !countryAutocomplete.getText().toString().equals("") ?countryAutocomplete.getText().toString() : "%";

            this.propertyViewModel.searchProperty(mTypeProperty,town, minSurface,maxSurface,minPrice, maxPrice,
                    mMinBedroom, mMaxBedroom, country, mStatus,mRealEstateAgentName)
                    .observe(this, this::updateRealEstateItemsList);

            getDialog().dismiss();
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if (parent.getId() == R.id.dialog_spinner_type_property) {
            mTypeProperty = typePropertySpinner.getSelectedItem().toString();
        } else if (parent.getId() == R.id.dialog_spinner_status) {
            mStatus = statusSpinner.getSelectedItem().toString();
        } else if (parent.getId() == R.id.dialog_spinner_number_bedroom_min) {

            if (minBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                mMinBedroom = 7;
            } else {
                mMinBedroom = (int) minBedroomSpinner.getSelectedItem();
            }

        } else if (parent.getId() == R.id.dialog_spinner_number_bedroom_max) {

            if (maxBedroomSpinner.getSelectedItem().toString().equals("7+")) {
                mMaxBedroom = 7;
            } else {
                mMaxBedroom = (int) maxBedroomSpinner.getSelectedItem();
            }

        } else if (parent.getId() == R.id.dialog_spinner_agent_name) {
            mRealEstateAgentName = realEstateAgentNameSpinner.getSelectedItemId()+1;/**VERIFIER QUE C'EST BON*/
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}