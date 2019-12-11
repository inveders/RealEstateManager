package com.inved.realestatemanager.controller.dialogs;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.view.PropertyListAdapter;

import java.util.List;

import javax.security.auth.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.apptik.widget.MultiSlider;

public class SearchFullScreenDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

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

    //Interface
    private OnClickSearchInterface callback;
    private UnitConversion unitConversion = new UnitConversion();


    //View Model
    private PropertyViewModel propertyViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_search_full_screen_dialog, container, false);
        ButterKnife.bind(this, view);

        callback = (OnClickSearchInterface) getTargetFragment();

        this.configureViewModel();
        this.seekbarChangements();
        if (getDialog() != null) {
            getDialog().setTitle(getString(R.string.page_name_search_dialog));
        }

        cancelSearchButton.setOnClickListener(v -> {
            getDialog().cancel();
            callback.cancelButton();
        });
        searchActionButton.setOnClickListener(v -> this.startSearchProperty());
        return view;
    }

    private void seekbarChangements() {

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

    // --------------
    // UI
    // --------------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);
    }

    // Update the list of Real Estate item
    private void updateRealEstateItemsList(List<Property> properties) {

        callback.searchButton(properties);

    }

    // --------------
    // Action
    // --------------

    private void startSearchProperty() {

        if (getDialog() != null) {

            if (mMinBedroom > mMaxBedroom) {
                Toast.makeText(getContext(), getString(R.string.Minbedroom_Maxbedoorm_control), Toast.LENGTH_SHORT).show();

            } else {
                String town = !townPropertyAutocomplete.getText().toString().equals("") ? townPropertyAutocomplete.getText().toString() : null;
                String country = !countryAutocomplete.getText().toString().equals("") ? countryAutocomplete.getText().toString() : null;


                this.propertyViewModel.searchProperty(mTypeProperty, town, minSurface, maxSurface, minPrice, maxPrice,
                        mMinBedroom, mMaxBedroom, country, mStatus, mRealEstateAgentName)
                        .observe(this, this::updateRealEstateItemsList);

                getDialog().dismiss();
            }


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
           /** mRealEstateAgentName = realEstateAgentNameSpinner.getSelectedItemId() + 1;*/
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

  /*  public void setCallback(OnClickSearchInterface callback) {
        this.callback = callback;
    }*/

    public interface OnClickSearchInterface {

        void searchButton(List<Property> properties);
        void cancelButton();
    }


}