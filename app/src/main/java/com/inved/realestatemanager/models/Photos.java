package com.inved.realestatemanager.models;

import android.graphics.drawable.BitmapDrawable;

import androidx.room.TypeConverter;

import java.util.List;

public class Photos {

    private List<BitmapDrawable> photos;


    public Photos(List<BitmapDrawable> photos) {
        this.photos = photos;
    }

    @TypeConverter
    public List<BitmapDrawable> getPhotos() {
        return photos;
    }

    @TypeConverter
    public void setPhotos(List<BitmapDrawable> photos) {
        this.photos = photos;
    }
}
