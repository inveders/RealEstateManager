package com.inved.realestatemanager.property;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Property;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    @BindView(R.id.fragment_list_property_item_type)
    TextView typeProperty;
    @BindView(R.id.fragment_list_property_item_price)
    TextView pricePropertyInDollar;
    @BindView(R.id.fragment_detail_property_surface_area_text)
    TextView surfaceAreaProperty;
    @BindView(R.id.fragment_detail_property_number_of_room_text)
    TextView numberRoomsInProperty;
    @BindView(R.id.fragment_detail_property_number_of_bathroom_text)
    TextView numberBathroomsInProperty;
    @BindView(R.id.fragment_detail_property_number_of_bedroom_text)
    TextView numberBedroomsInProperty;
    @BindView(R.id.fragment_detail_property_description_text)
    TextView fullDescriptionProperty;
    @BindView(R.id.fragment_detail_property_image)
    ImageView photo;
 /*   @BindView(R.id.fragment_detail_property_image_description)
    TextView photoDescription;*/
    @BindView(R.id.fragment_detail_property_location_text)
    TextView addressProperty;
    @BindView(R.id.fragment_detail_property_points_of_interest_text)
    TextView pointsOfInterestNearProperty;
    @BindView(R.id.fragment_detail_property_status_text)
    TextView statusProperty;
    @BindView(R.id.fragment_detail_property_date_of_entry_on_market_text)
    TextView dateOfEntryOnMarketForProperty;
    @BindView(R.id.fragment_detail_property_date_of_sale_text)
    TextView dateOfSaleForPorperty;
    @BindView(R.id.fragment_detail_property_real_estate_agent_text)
    TextView realEstateAgent;

    // FOR DATA
    private WeakReference<PropertyAdapter.Listener> callbackWeakRef;

    public PropertyViewHolder(View propertyItemView) {
        super(propertyItemView);
        ButterKnife.bind(this, propertyItemView);
    }

    void updateWithProperty(Property property, PropertyAdapter.Listener callback){
        this.callbackWeakRef = new WeakReference<>(callback);

      /*  if(property.getPhoto().size() > 0)
        {
            int index = property.getPhoto().size() -1;
            Bitmap lastbitmap = property.getPhoto().get(index);

            photo.setImageBitmap(lastbitmap);
        } else{
            photo.setImageBitmap(property.getPhoto().get(0));
        }*/

        //Put double and long values in textview
        double surfaceAreDouble = property.getSurfaceAreaProperty();
        double pricePropertyInDollarDouble = property.getPricePropertyInDollar();
        long realEstateAgentIdLong = property.getRealEstateAgentId();

        //Put all points of interests in one textview
        StringBuilder builderPointOfInterest = new StringBuilder();
      /*  for (String detailsPointOfInterest : property.getPointsOfInterestNearProperty()) {
            builderPointOfInterest.append(detailsPointOfInterest + ",");
        }*/


        this.typeProperty.setText(property.getTypeProperty());
        this.pricePropertyInDollar.setText(String.valueOf(pricePropertyInDollarDouble));
        this.surfaceAreaProperty.setText(String.valueOf(surfaceAreDouble));
        this.numberRoomsInProperty.setText(property.getNumberRoomsInProperty());
        this.numberBathroomsInProperty.setText(property.getNumberBathroomsInProperty());
        this.numberBedroomsInProperty.setText(property.getNumberBedroomsInProperty());
        this.fullDescriptionProperty.setText(property.getFullDescriptionProperty());
      //  this.photoDescription.setText(property.getPhotoDescription());
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
