package com.inved.realestatemanager.injections;

import android.content.Context;

import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.repositories.PropertyDataRepository;
import com.inved.realestatemanager.repositories.RealEstateAgentDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static PropertyDataRepository providePropertyDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao());
    }

    private static RealEstateAgentDataRepository provideUserDataSource(Context context) {
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new RealEstateAgentDataRepository(database.realEstateAgentsDao());
    }


    private static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        PropertyDataRepository dataSourceProperty = providePropertyDataSource(context);
        RealEstateAgentDataRepository dataSourceRealEstateAgent = provideUserDataSource(context);

        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceProperty, dataSourceRealEstateAgent, executor);
    }
}

