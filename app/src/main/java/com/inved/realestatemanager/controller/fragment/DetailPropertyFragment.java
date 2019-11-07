package com.inved.realestatemanager.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.ArrayList;

import static com.inved.realestatemanager.controller.activity.DetailActivity.PROPERTY_ID_INTENT;
import static com.inved.realestatemanager.property.PropertyListViewHolder.PROPERTY_ID;


public class DetailPropertyFragment extends Fragment {

    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView surfaceAreaProperty;
    private TextView streetNumber;
    private TextView streetName;
    private TextView complAddress;
    private TextView townProperty;
    private TextView zipCode;
    private TextView country;

    private TextView numberRoomsInProperty;
    private TextView numberBathroomsInProperty;
    private TextView numberBedroomsInProperty;
    private TextView fullDescriptionProperty;
    private ImageSwitcher imageSwitcher;

    private TextView pointsOfInterestNearProperty;
    private TextView statusProperty;
    private TextView dateOfEntryOnMarketForProperty;
    private TextView dateOfSaleForPorperty;
    private TextView realEstateAgent;

    private PropertyViewModel propertyViewModel;


    public DetailPropertyFragment() {


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_detail_property, container, false);

        typeProperty = mView.findViewById(R.id.fragment_detail_property_type_text);
        pricePropertyInDollar = mView.findViewById(R.id.fragment_detail_property_price_text);
        surfaceAreaProperty = mView.findViewById(R.id.fragment_detail_property_surface_area_text);
        numberRoomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_room_text);
        numberBedroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bedroom_text);
        numberBathroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bathroom_text);
        fullDescriptionProperty = mView.findViewById(R.id.fragment_detail_property_description_text);
        imageSwitcher = mView.findViewById(R.id.fragment_detail_property_image_switcher);
        streetNumber=mView.findViewById(R.id.fragment_detail_property_street_number_text);
        streetName=mView.findViewById(R.id.fragment_detail_property_street_name_text);
        complAddress=mView.findViewById(R.id.fragment_detail_property_complement_address_text);
        townProperty=mView.findViewById(R.id.fragment_detail_property_town_text);
        zipCode=mView.findViewById(R.id.fragment_detail_property_zip_code_text);
        country=mView.findViewById(R.id.fragment_detail_property_country_text);
        pointsOfInterestNearProperty = mView.findViewById(R.id.fragment_detail_property_points_of_interest_text);
        statusProperty = mView.findViewById(R.id.fragment_detail_property_status_text);
        dateOfEntryOnMarketForProperty = mView.findViewById(R.id.fragment_detail_property_date_of_entry_on_market_text);
        dateOfSaleForPorperty = mView.findViewById(R.id.fragment_detail_property_date_of_sale_text);
        realEstateAgent = mView.findViewById(R.id.fragment_detail_property_real_estate_agent_text);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long myPropertyId = bundle.getLong(PROPERTY_ID, 0);
            configureViewModel();
            propertyViewModel.getOneProperty(myPropertyId).observe(this, this::updateWithProperty);

        }

        return mView;
    }



    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);

    }

    private void updateWithProperty(Property property) {


        //TYPE PROPERTY
        if (property.getTypeProperty() != null) {
            this.typeProperty.setText(property.getTypeProperty());
        } else {
            this.typeProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //PRICE IN DOLLARS
        if (property.getPricePropertyInDollar() != 0.0) {
            this.pricePropertyInDollar.setText(String.valueOf(property.getPricePropertyInDollar()));
        } else {
            this.pricePropertyInDollar.setText(MainApplication.getResourses().getString(R.string.list_property_no_price_indicated));
        }

        //ADDRESS PROPERTY

        if (property.getStreetNumber() != null) {
            this.streetNumber.setText(property.getStreetNumber());
        } else {
            this.streetNumber.setText(MainApplication.getResourses().getString(R.string.none));
        }

        if (property.getStreetName() != null) {
            this.streetName.setText(property.getStreetName());
        } else {
            this.streetName.setText(MainApplication.getResourses().getString(R.string.none));
        }

        if (property.getAddressProperty() != null) {
            this.complAddress.setText(property.getAddressProperty());
        } else {
            this.complAddress.setText(MainApplication.getResourses().getString(R.string.none));
        }

        if (property.getTownProperty() != null) {
            this.townProperty.setText(property.getTownProperty());
        } else {
            this.townProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        if (property.getZipCode() != null) {
            this.zipCode.setText(property.getZipCode());
        } else {
            this.zipCode.setText(MainApplication.getResourses().getString(R.string.none));
        }

        if (property.getCountry() != null) {
            this.country.setText(property.getCountry());
        } else {
            this.country.setText(MainApplication.getResourses().getString(R.string.none));
        }


        //SURFACE AREA
        if (property.getSurfaceAreaProperty() != 0.0) {
            this.surfaceAreaProperty.setText(String.valueOf(property.getSurfaceAreaProperty()));
        } else {
            this.surfaceAreaProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //IMAGE SWITCHER

        imageSwitcher.setFactory(() -> {
            // TODO Auto-generated method stub

            // Create a new ImageView and set it's properties
            ImageView imageView = new ImageView(MainApplication.getInstance().getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return imageView;
        });

        Animation in = AnimationUtils.loadAnimation(MainApplication.getInstance().getApplicationContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(MainApplication.getInstance().getApplicationContext(), android.R.anim.slide_out_right);
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);


        ArrayList<String> myImages = new ArrayList<>();

        if (property.getPhotoUri1() != null) {
            myImages.add(property.getPhotoUri1());
            //   photoDescription1.setText(property.getPhotoDescription1());

        }
        if (property.getPhotoUri2() != null) {
            myImages.add(property.getPhotoUri2());
            //   photoDescription1.setText(property.getPhotoDescription1());

        }
        if (property.getPhotoUri3() != null) {
            myImages.add(property.getPhotoUri3());
            //   photoDescription1.setText(property.getPhotoDescription1());

        }
        if (property.getPhotoUri4() != null) {
            myImages.add(property.getPhotoUri4());
            //   photoDescription1.setText(property.getPhotoDescription1());

        }
        if (property.getPhotoUri5() != null) {
            myImages.add(property.getPhotoUri5());
            //   photoDescription1.setText(property.getPhotoDescription1());

        }

        if(myImages.size()!=0){
            imageSwitcher.setImageURI(Uri.parse(myImages.get(0)));
        }else{
            imageSwitcher.setImageResource(R.mipmap.ic_logo_appli_round);
        }


        //POINT OF INTEREST
        if (property.getPointOfInterest() != null) {
            this.pointsOfInterestNearProperty.setText(property.getPointOfInterest());
        } else {
            this.pointsOfInterestNearProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }


        //FULL DESCRIPTION PROPERTY
        if (property.getFullDescriptionProperty() != null) {
            this.fullDescriptionProperty.setText(property.getFullDescriptionProperty());
        } else {
            this.fullDescriptionProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //NUMBER OF ROOM, BEDROOM, BATHROOM
        this.numberRoomsInProperty.setText(String.valueOf(property.getNumberRoomsInProperty()));
        this.numberBathroomsInProperty.setText(String.valueOf(property.getNumberBathroomsInProperty()));
        this.numberBedroomsInProperty.setText(String.valueOf(property.getNumberBedroomsInProperty()));

        //STATUS PROPERTY
        if (property.getStatusProperty() != null) {
            this.statusProperty.setText(property.getStatusProperty());
        } else {
            this.statusProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //DATE OF ENTRY
        if (property.getDateOfEntryOnMarketForProperty() != null) {
            this.dateOfEntryOnMarketForProperty.setText(property.getDateOfEntryOnMarketForProperty());
        } else {
            this.dateOfEntryOnMarketForProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //DATE OF SALE
        if (property.getDateOfSaleForPorperty() != null) {
            this.dateOfSaleForPorperty.setText(property.getDateOfSaleForPorperty());
        } else {
            this.dateOfSaleForPorperty.setText(MainApplication.getResourses().getString(R.string.none));
        }




    }












}
