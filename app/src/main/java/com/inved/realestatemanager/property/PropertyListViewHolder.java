package com.inved.realestatemanager.property;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;


public class PropertyListViewHolder extends RecyclerView.ViewHolder {

    public static final String PROPERTY_ID = "PROPERTY_ID";
    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView surfaceAreaProperty;
    private TextView townProperty;
    private ImageView photo;
    private CardView mCardview;
    private Context context;

    PropertyListViewHolder(View propertyItemView) {
        super(propertyItemView);

        typeProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = propertyItemView.findViewById(R.id.fragment_list_property_item_price);
        townProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_city);
        mCardview = propertyItemView.findViewById(R.id.fragment_list_property_item);
        surfaceAreaProperty = propertyItemView.findViewById(R.id.fragment_detail_property_surface_area_text);
        photo = propertyItemView.findViewById(R.id.fragment_list_property_item_image);

    }


    void updateWithProperty(Property property, PropertyListInterface callback, RequestManager glide) {

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

        String urlNoImage = "https://semantic-ui.com/images/wireframe/image.png";
        //PHOTO URI 1

        if (property.getPhotoUri1() != null) {

            glide.load(new File(property.getPhotoUri1()))
                    .into(photo);


        } else {
            glide.load(urlNoImage)
                    .into(photo);

        }



        //CLICK ON CARDVIEW
        mCardview.setOnClickListener(view -> {

            long propertyId = property.getPropertyId();
            callback.clickOnCardView(propertyId);


            // Launch Detail fragment
          //  Intent intent = new Intent(view.getContext(), DetailPropertyFragment.class);
          //  view.getContext().startActivity(intent);



        });


    }

    public interface PropertyListInterface{
        void clickOnCardView(long propertyId); //Je lui donne ce dont j'avais besoin pour faire l'action
        void onClickDeleteButton(long propertyId);

    }

}