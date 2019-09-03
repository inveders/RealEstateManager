package com.inved.realestatemanager.utils;

import android.app.Application;

import com.facebook.stetho.Stetho;

import net.danlew.android.joda.JodaTimeAndroid;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Stetho.initializeWithDefaults(this);

    }
}
