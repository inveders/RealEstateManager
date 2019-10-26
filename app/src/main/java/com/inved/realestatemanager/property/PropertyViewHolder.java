package com.inved.realestatemanager.property;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Photos;
import com.inved.realestatemanager.models.PointsOfInterest;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;

import java.lang.ref.WeakReference;


public class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView surfaceAreaProperty;
    private TextView townProperty;

    private TextView numberRoomsInProperty;
    private TextView numberBathroomsInProperty;
    private TextView numberBedroomsInProperty;
    private TextView fullDescriptionProperty;
    private ImageView photo;
    private TextView addressProperty;

    private TextView pointsOfInterestNearProperty;
    private TextView statusProperty;
    private TextView dateOfEntryOnMarketForProperty;
    private TextView dateOfSaleForPorperty;
    private TextView realEstateAgent;

    // FOR DATA
    private WeakReference<PropertyAdapter.Listener> callbackWeakRef;

    PropertyViewHolder(View propertyItemView) {
        super(propertyItemView);

        typeProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = propertyItemView.findViewById(R.id.fragment_list_property_item_price);
        surfaceAreaProperty = propertyItemView.findViewById(R.id.fragment_detail_property_surface_area_text);
        numberRoomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_room_text);
        numberBedroomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_bedroom_text);
        numberBathroomsInProperty = propertyItemView.findViewById(R.id.fragment_detail_property_number_of_bathroom_text);
        fullDescriptionProperty = propertyItemView.findViewById(R.id.fragment_detail_property_description_text);
        photo = propertyItemView.findViewById(R.id.fragment_detail_property_image);
        addressProperty = propertyItemView.findViewById(R.id.fragment_detail_property_location_text);
        townProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_city);
        pointsOfInterestNearProperty = propertyItemView.findViewById(R.id.fragment_detail_property_points_of_interest_text);
        statusProperty = propertyItemView.findViewById(R.id.fragment_detail_property_status_text);
        dateOfEntryOnMarketForProperty = propertyItemView.findViewById(R.id.fragment_detail_property_date_of_entry_on_market_text);
        dateOfSaleForPorperty = propertyItemView.findViewById(R.id.fragment_detail_property_date_of_sale_text);
        realEstateAgent = propertyItemView.findViewById(R.id.fragment_detail_property_real_estate_agent_text);
    }



    void updateWithProperty(Property property, PropertyAdapter.Listener callback) {
        this.callbackWeakRef = new WeakReference<>(callback);

        Log.d("debago","city :"+property.getTownProperty()+" type"+property.getTownProperty());
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
/*
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
        }*/
/*
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

        //PHOTO DESCRIPTION
      /*  if (property.getPhotoDescription() != null) {
            this.fullDescriptionProperty.setText(property.getPhotoDescription());
        } else {
            this.fullDescriptionProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }*/

    }

    void updateWithPointsOfInterest(PointsOfInterest pointsOfInterest, PropertyAdapter.Listener callback) {
        //this.callbackWeakRef = new WeakReference<>(callback);

        //Put all points of interests in one textview
        //  StringBuilder builderPointOfInterest = new StringBuilder();
      /*  for (String detailsPointOfInterest : property.getPointsOfInterestNearProperty()) {
            builderPointOfInterest.append(detailsPointOfInterest + ",");
        }*/

        //POINT OF INTEREST
      /*  if (pointsOfInterest.getPointsOfInterest() != null) {
            this.pointsOfInterestNearProperty.setText(pointsOfInterest.getPointsOfInterest().toString());
        } else {
            this.pointsOfInterestNearProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }*/


    }

    void updateWithPhoto(Photos photos, PropertyAdapter.Listener callback) {
        //this.callbackWeakRef = new WeakReference<>(callback);


      /*  if(property.getPhoto().size() > 0)
        {
            int index = property.getPhoto().size() -1;
            Bitmap lastbitmap = property.getPhoto().get(index);

            photo.setImageBitmap(lastbitmap);
        } else{
            photo.setImageBitmap(property.getPhoto().get(0));
        }*/

        //PHOTOS
       /* if (photos.getPhotos() != null) {
            this.photo.setImageBitmap(photos.getPhotos().get(0).getBitmap());
        } else {
            //this.photo.setText(MainApplication.getResourses().getString(R.string.none));
        }*/


    }


    void updateWithRealEstateAgent(RealEstateAgents realEstateAgents, PropertyAdapter.Listener callback) {
        //this.callbackWeakRef = new WeakReference<>(callback);

      /*  //REAL ESTATE AGENT
        if (realEstateAgents.getFirstname() != null) {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.detail_property_real_estate_agent_text,realEstateAgents.getFirstname(),realEstateAgents.getLastname()));
        } else {
            this.realEstateAgent.setText(MainApplication.getResourses().getString(R.string.none));
        }*/


    }

    @Override
    public void onClick(View view) {
        PropertyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }

}