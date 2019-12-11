package com.inved.realestatemanager.view;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.WatermarkTransformation;

import java.io.File;


public class PropertyListViewHolder extends RecyclerView.ViewHolder {

    public static final String PROPERTY_ID = "PROPERTY_ID";
    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView surfaceAreaProperty;
    private TextView townProperty;
    private CardView mCardview;
    private ImageView photo;

    PropertyListViewHolder(View propertyItemView) {
        super(propertyItemView);

        typeProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = propertyItemView.findViewById(R.id.fragment_list_property_item_price);
        townProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_city);
        mCardview = propertyItemView.findViewById(R.id.fragment_list_property_item);
        surfaceAreaProperty = propertyItemView.findViewById(R.id.fragment_detail_property_surface_area_text);
        photo = propertyItemView.findViewById(R.id.fragment_list_property_item_image);

    }


    void updateWithProperty(Property property, PropertyListInterface callback) {

        UnitConversion unitConversion =new UnitConversion();

        //TYPE PROPERTY
        if (property.getTypeProperty() != null) {
            this.typeProperty.setText(property.getTypeProperty());
        } else {
            this.typeProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //PRICE IN DOLLARS
        if (property.getPricePropertyInDollar() != 0.0) {
            this.pricePropertyInDollar.setText(unitConversion.changeDoubleToStringWithThousandSeparator(property.getPricePropertyInDollar()));
        } else {
            this.pricePropertyInDollar.setText(MainApplication.getResourses().getString(R.string.list_property_no_price_indicated));
        }

        //TOWN PROPERTY
        if (property.getTownProperty() != null) {
            this.townProperty.setText(property.getTownProperty());
        } else {
            this.townProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }


        //SURFACE AREA
        if (property.getSurfaceAreaProperty() != 0.0) {
            this.surfaceAreaProperty.setText(MainApplication.getResourses().getString(R.string.price_in_meter,unitConversion.changeDoubleToStringWithThousandSeparator(property.getSurfaceAreaProperty())));
        } else {
            this.surfaceAreaProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        String urlNoImage = "https://semantic-ui.com/images/wireframe/image.png";
        //PHOTO URI 1

        if (property.getPhotoUri1() != null) {

            if(property.getDateOfSaleForProperty()==null || property.getDateOfSaleForProperty().isEmpty()){
                Uri fileUri = Uri.parse(property.getPhotoUri1());
                if (fileUri.getPath() != null) {
                    Glide.with(MainApplication.getInstance().getApplicationContext())
                            .load(new File(fileUri.getPath()))
                            .into((photo));
                }
            }else{
                Uri fileUri = Uri.parse(property.getPhotoUri1());
                if (fileUri.getPath() != null) {
                    Glide.with(MainApplication.getInstance().getApplicationContext())
                            .load(new File(fileUri.getPath()))
                            .transform(new WatermarkTransformation(MainApplication.getInstance().getApplicationContext()))
                            .into((photo));
                }
            }




        } else {

                Glide.with(MainApplication.getInstance().getApplicationContext())
                        .load(urlNoImage)
                        .into((photo));

        }



        //CLICK ON CARDVIEW
        mCardview.setOnClickListener(view -> {

            String propertyId = property.getPropertyId();
            callback.clickOnCardView(propertyId);

        });


    }

    public interface PropertyListInterface{
        void clickOnCardView(String propertyId);

    }

}