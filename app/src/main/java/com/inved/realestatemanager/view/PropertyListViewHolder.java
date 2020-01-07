package com.inved.realestatemanager.view;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.GlideApp;
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
    private ShimmerFrameLayout shimmerFrameLayout ;

    PropertyListViewHolder(View propertyItemView) {
        super(propertyItemView);

        typeProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_type);
        pricePropertyInDollar = propertyItemView.findViewById(R.id.fragment_list_property_item_price);
        townProperty = propertyItemView.findViewById(R.id.fragment_list_property_item_city);
        mCardview = propertyItemView.findViewById(R.id.fragment_list_property_item);
        surfaceAreaProperty = propertyItemView.findViewById(R.id.fragment_detail_property_surface_area_text);
        photo = propertyItemView.findViewById(R.id.fragment_list_property_item_image);
        shimmerFrameLayout=propertyItemView.findViewById(R.id.shimmer_view_container);

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

        //PHOTO URI 1

        if (property.getPhotoUri1() != null) {
            showShimmer();
            if(property.getDateOfSaleForProperty()==null || property.getDateOfSaleForProperty().isEmpty()){
                File localFile = new File(property.getPhotoUri1());
                File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String mFileName = "/" + localFile.getName();
                File goodFile = new File(storageDir,mFileName);
                if (goodFile.exists()) {
                    Log.d("debago","good file exist");
                    photoUriInGlide(property.getPhotoUri1());

                }else{
                    Log.d("debago","good file NOT exist "+goodFile);
                    photoFirebaseStorageInGlide(property.getPropertyId(),property.getPhotoUri1());
                }
            }else{
                Log.d("debago","good file sold");
                photoSoldUriInGlide(property.getPhotoUri1());
            }

        }



        //CLICK ON CARDVIEW
        mCardview.setOnClickListener(view -> {

            String propertyId = property.getPropertyId();
            callback.clickOnCardView(propertyId);

        });


    }


    private void photoUriInGlide(String photoUri){
        Uri fileUri = Uri.parse(photoUri);
        if (fileUri.getPath() != null) {
            GlideApp.with(MainApplication.getInstance().getApplicationContext())
                    .load(new File(fileUri.getPath()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d("debago","Exception is : "+e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            stopShimmer();
                            return false;
                        }
                    })
                    .into((photo));
        }
    }

    private void photoSoldUriInGlide(String photoUri){
        Uri fileUri = Uri.parse(photoUri);
        if (fileUri.getPath() != null) {
            GlideApp.with(MainApplication.getInstance().getApplicationContext())
                    .load(new File(fileUri.getPath()))
                    .transform(new WatermarkTransformation())
                    .into((photo));
            stopShimmer();
        }










    }

    private void photoFirebaseStorageInGlide(String propertyId,String photoUri){

        StorageReference fileReference = FirebaseStorage.getInstance().getReference(propertyId).child("Pictures")
                .child(last27characters(photoUri));

        GlideApp.with(MainApplication.getInstance().getApplicationContext())
                .load(fileReference)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("debago","Exception is : "+e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("debago","onResourceReady");
                        stopShimmer();
                        return false;
                    }
                })
                .into(photo);
    }

    private void photoSoldFirebaseStorageInGlide(String propertyId,String photoUri){

        StorageReference fileReference = FirebaseStorage.getInstance().getReference(propertyId).child("Pictures")
                .child(last27characters(photoUri));

        GlideApp.with(MainApplication.getInstance().getApplicationContext())
                .load(fileReference)
                .transform(new WatermarkTransformation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("debago","Exception is : "+e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        stopShimmer();
                        return false;
                    }
                })
                .into(photo);
    }

    public interface PropertyListInterface{
        void clickOnCardView(String propertyId);

    }

    public String last27characters(String chaine)
    {
        if (chaine.length() <= 27)
            return(chaine);
        else
            return(chaine.substring(chaine.length() - 27));
    }

    private void stopShimmer(){
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
    }

    private void showShimmer(){
        shimmerFrameLayout.showShimmer(true);
    }

}