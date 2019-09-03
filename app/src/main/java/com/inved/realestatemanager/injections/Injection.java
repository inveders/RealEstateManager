package com.inved.realestatemanager.injections;

import android.content.Context;

import com.example.testimmo.database.dao.RealEstateManagerDatabase;
import com.example.testimmo.repositories.PropertyDataRepository;
import com.example.testimmo.repositories.RealEstateAgentDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static PropertyDataRepository providePropertyDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao());
    }

    public static RealEstateAgentDataRepository provideUserDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new RealEstateAgentDataRepository(database.realEstateAgentsDao());
    }

    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyDataRepository dataSourceProperty = providePropertyDataSource(context);
        RealEstateAgentDataRepository dataSourceRealEstateAgent = provideUserDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceProperty, dataSourceRealEstateAgent, executor);
    }
}
