package com.inved.realestatemanager.controller.fragment;

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

import static com.inved.realestatemanager.property.PropertyListViewHolder.PROPERTY_ID;

public class DetailPropertyFragment extends Fragment {

    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView surfaceAreaProperty;
    private TextView townProperty;

    private TextView numberRoomsInProperty;
    private TextView numberBathroomsInProperty;
    private TextView numberBedroomsInProperty;
    private TextView fullDescriptionProperty;
    private ImageSwitcher imageSwitcher;

    private TextView addressProperty;

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

        typeProperty = mView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = mView.findViewById(R.id.fragment_list_property_item_price);
        surfaceAreaProperty = mView.findViewById(R.id.fragment_detail_property_surface_area_text);
        numberRoomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_room_text);
        numberBedroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bedroom_text);
        numberBathroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bathroom_text);
        fullDescriptionProperty = mView.findViewById(R.id.fragment_detail_property_description_text);
        imageSwitcher = mView.findViewById(R.id.fragment_detail_property_image_switcher);
        addressProperty = mView.findViewById(R.id.fragment_detail_property_location_text);
        townProperty = mView.findViewById(R.id.fragment_list_property_item_city);
        pointsOfInterestNearProperty = mView.findViewById(R.id.fragment_detail_property_points_of_interest_text);
        statusProperty = mView.findViewById(R.id.fragment_detail_property_status_text);
        dateOfEntryOnMarketForProperty = mView.findViewById(R.id.fragment_detail_property_date_of_entry_on_market_text);
        dateOfSaleForPorperty = mView.findViewById(R.id.fragment_detail_property_date_of_sale_text);
        realEstateAgent = mView.findViewById(R.id.fragment_detail_property_real_estate_agent_text);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            long myPropertyId = bundle.getLong(PROPERTY_ID, 0);
            configureViewModel(myPropertyId);
            propertyViewModel.getOneProperty(myPropertyId).observe(this, this::updateWithProperty);
            getCurrentAgent();
        }

        return mView;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel(long propertyId) {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(propertyId);

    }

    private void updateWithProperty(Property property) {

        Log.d("debago", "city :" + property.getTownProperty() + " type" + property.getTownProperty());
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

        //TOWN PROPERTY
        if (property.getTownProperty() != null) {
            this.townProperty.setText(String.valueOf(property.getTypeProperty()));
        } else {
            this.townProperty.setText(MainApplication.getResourses().getString(R.string.none));
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

        if(myImages.get(0)!=null){
            imageSwitcher.setImageURI(Uri.parse(myImages.get(0)));
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

        //ADDRESS PROPERTY
        if (property.getAddressProperty() != null) {
            this.addressProperty.setText(property.getAddressProperty());
        } else {
            this.addressProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

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

    // 3 - Get Current Agent
    private void getCurrentAgent() {
        this.propertyViewModel.getRealEstateAgent().observe(this,realEstateAgents -> {
            String firstname = realEstateAgents.getFirstname();
            String lastname = realEstateAgents.getLastname();
            //REAL ESTATE AGENT
            if (firstname != null) {
                this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text,firstname,lastname));
            } else {
                this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.none));
            }
        });
    }










}
