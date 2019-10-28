package com.inved.realestatemanager.property;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

public class PropertyDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

    // FOR DATA
    private WeakReference<PropertyListAdapter.Listener> callbackWeakRef;

    PropertyDetailViewHolder(View propertyItemView) {
        super(propertyItemView);

        typeProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = propertyItemView.findViewById(R.id.fragment_list_property_item_price);
        surfaceAreaProperty = propertyItemView.findViewById(R.id.fragment_detail_property_surface_area_text);
        numberRoomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_room_text);
        numberBedroomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_bedroom_text);
        numberBathroomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_bathroom_text);
        fullDescriptionProperty = propertyItemView.findViewById(R.id.fragment_detail_property_description_text);
        imageSwitcher = propertyItemView.findViewById(R.id.fragment_detail_property_image_switcher);
        addressProperty = propertyItemView.findViewById(R.id.fragment_detail_property_location_text);
        townProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_city);
        pointsOfInterestNearProperty = propertyItemView.findViewById(R.id.fragment_detail_property_points_of_interest_text);
        statusProperty = propertyItemView.findViewById(R.id.fragment_detail_property_status_text);
        dateOfEntryOnMarketForProperty = propertyItemView.findViewById(R.id.fragment_detail_property_date_of_entry_on_market_text);
        dateOfSaleForPorperty = propertyItemView.findViewById(R.id.fragment_detail_property_date_of_sale_text);
        realEstateAgent = propertyItemView.findViewById(R.id.fragment_detail_property_real_estate_agent_text);
    }


    void updateWithProperty(Property property) {

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

        long agentId = property.getRealEstateAgentId();


        //REAL ESTATE AGENT
     /*   if (realEstateAgents.getFirstname() != null) {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text,realEstateAgents.getFirstname(),realEstateAgents.getLastname()));
        } else {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.none));
        }*/

    }


  /*  void updateWithRealEstateAgent(RealEstateAgents realEstateAgents) {



        //REAL ESTATE AGENT
        if (realEstateAgents.getFirstname() != null) {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text,realEstateAgents.getFirstname(),realEstateAgents.getLastname()));
        } else {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.none));
        }


    }*/

    @Override
    public void onClick(View view) {
        PropertyListAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }

    // 2 - Configuring ViewModel
   /* private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }*/
}
