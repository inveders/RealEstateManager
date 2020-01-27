package com.inved.realestatemanager.utils;

import android.app.Application;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;


public class MainApplication extends Application { //extends MultiDexApplication

    private static MainApplication mInstance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        JodaTimeAndroid.init(this);
       // deleteDatabase("MyDatabase.db");
        Stetho.initializeWithDefaults(this);
        Fabric.with(this, new Crashlytics());
        res=getResources();

    }

    public static MainApplication getInstance() {
        return mInstance;
    }

    public static Resources getResourses() {
        return res;
    }
}
