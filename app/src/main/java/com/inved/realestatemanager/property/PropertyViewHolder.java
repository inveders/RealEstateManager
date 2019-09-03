package com.inved.realestatemanager.property;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testimmo.R;
import com.example.testimmo.models.Property;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.fragment_property_description_item_typeProperty)
    TextView typeProperty;
    @BindView(R.id.fragment_property_description_item_pricePropertyInDollar)
    TextView pricePropertyInDollar;
    @BindView(R.id.fragment_property_description_item_surfaceAreaProperty)
    TextView surfaceAreaProperty;
    @BindView(R.id.fragment_property_description_item_numberRoomsInProperty)
    TextView numberRoomsInProperty;
    @BindView(R.id.fragment_property_description_item_numberBathroomsInProperty)
    TextView numberBathroomsInProperty;
    @BindView(R.id.fragment_property_description_item_numberBedroomsInProperty)
    TextView numberBedroomsInProperty;
    @BindView(R.id.fragment_property_description_item_fullDescriptionProperty)
    TextView fullDescriptionProperty;
    @BindView(R.id.fragment_property_description_item_photo)
    ImageView photo;
    @BindView(R.id.fragment_property_description_item_photoDescription)
    TextView photoDescription;
    @BindView(R.id.fragment_property_description_item_addressProperty)
    TextView addressProperty;
    @BindView(R.id.fragment_property_description_item_pointsOfInterestNearProperty)
    TextView pointsOfInterestNearProperty;
    @BindView(R.id.fragment_property_description_item_statusProperty)
    TextView statusProperty;
    @BindView(R.id.fragment_property_description_item_dateOfEntryOnMarketForProperty)
    TextView dateOfEntryOnMarketForProperty;
    @BindView(R.id.fragment_property_description_item_dateOfSaleForPorperty)
    TextView dateOfSaleForPorperty;
    @BindView(R.id.fragment_property_description_item_realEstateAgent)
    TextView realEstateAgent;

    // FOR DATA
    private WeakReference<PropertyAdapter.Listener> callbackWeakRef;

    public PropertyViewHolder(View propertyItemView) {
        super(propertyItemView);
        ButterKnife.bind(this, propertyItemView);
    }

    public void updateWithProperty(Property property, PropertyAdapter.Listener callback){
        this.callbackWeakRef = new WeakReference<>(callback);

        if(property.getPhoto().size() > 0)
        {
            int index = property.getPhoto().size() -1;
            Bitmap lastbitmap = property.getPhoto().get(index);

            photo.setImageBitmap(lastbitmap);
        } else{
            photo.setImageBitmap(property.getPhoto().get(0));
        }

        //Put double and long values in textview
        double surfaceAreDouble = property.getSurfaceAreaProperty();
        double pricePropertyInDollarDouble = property.getPricePropertyInDollar();
        long realEstateAgentIdLong = property.getRealEstateAgentId();

        //Put all points of interests in one textview
        StringBuilder builderPointOfInterest = new StringBuilder();
        for (String detailsPointOfInterest : property.getPointsOfInterestNearProperty()) {
            builderPointOfInterest.append(detailsPointOfInterest + ",");
        }


        this.typeProperty.setText(property.getTypeProperty());
        this.pricePropertyInDollar.setText(String.valueOf(pricePropertyInDollarDouble));
        this.surfaceAreaProperty.setText(String.valueOf(surfaceAreDouble));
        this.numberRoomsInProperty.setText(property.getNumberRoomsInProperty());
        this.numberBathroomsInProperty.setText(property.getNumberBathroomsInProperty());
        this.numberBedroomsInProperty.setText(property.getNumberBedroomsInProperty());
        this.fullDescriptionProperty.setText(property.getFullDescriptionProperty());
        this.photoDescription.setText(property.getPhotoDescription());
        this.addressProperty.setText(property.getAddressProperty());
        this.pointsOfInterestNearProperty.setText(builderPointOfInterest.toString());
        this.statusProperty.setText(property.getStatusProperty());
        this.dateOfEntryOnMarketForProperty.setText(property.getDateOfEntryOnMarketForProperty());
        this.dateOfSaleForPorperty.setText(property.getDateOfSaleForPorperty());
        this.realEstateAgent.setText(String.valueOf(realEstateAgentIdLong));


    }

    @Override
    public void onClick(View view) {
        PropertyAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
