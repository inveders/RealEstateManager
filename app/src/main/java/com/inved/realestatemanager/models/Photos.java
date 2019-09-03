package com.inved.realestatemanager.models;

import android.graphics.drawable.BitmapDrawable;

import java.util.List;

public class Photos {

    private List<BitmapDrawable> photos;

    public Photos(List<BitmapDrawable> photos) {
        this.photos = photos;
    }

    public List<BitmapDrawable> getPhotos() {
        return photos;
    }

    public void setPhotos(List<BitmapDrawable> photos) {
        this.photos = photos;
    }
}
