package com.inved.realestatemanager.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.inved.realestatemanager.R;

import java.security.MessageDigest;

public class WatermarkTransformation extends BitmapTransformation {


    public WatermarkTransformation() {
    super();
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        int w = toTransform.getWidth();
        int h = toTransform.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, toTransform.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(toTransform, 0,0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(MainApplication.getResourses(), R.drawable.real_estate_sold_sign_png);
        //  canvas.drawBitmap(waterMark, 0, 0, null);
        int startX= (canvas.getWidth()-waterMark.getWidth())/2;//for horizontal position
        int startY=(canvas.getHeight()-waterMark.getHeight())/2;//for vertical position
        canvas.drawBitmap(waterMark,startX,startY,null);

        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}



