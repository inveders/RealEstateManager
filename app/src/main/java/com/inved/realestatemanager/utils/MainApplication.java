package com.inved.realestatemanager.utils;

import android.app.Application;
import android.content.res.Resources;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.inved.realestatemanager.retrofit.RetrofitServiceApiExchange;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;


public class MainApplication extends Application {

    private static MainApplication mInstance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        JodaTimeAndroid.init(this);
        RetrofitServiceApiExchange retrofitServiceApiExchange = new RetrofitServiceApiExchange();
        retrofitServiceApiExchange.retrofitCall();
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
