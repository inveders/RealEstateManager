package com.inved.realestatemanager.dao;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageDatabaseFilling;
import com.inved.realestatemanager.utils.MainApplication;

@Database(entities = {Property.class, RealEstateAgents.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase{

    // --- SINGLETON ---
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract RealEstateAgentsDao realEstateAgentsDao();

    public abstract PropertyDao propertyDao();

    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {

        //  context.deleteDatabase("MyDatabase.db");

            if (INSTANCE == null) {
                synchronized (RealEstateManagerDatabase.class) {
                    if (INSTANCE == null) {
                        ManageDatabaseFilling.saveDatabaseFillingState(context.getApplicationContext(),true);
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                RealEstateManagerDatabase.class, "MyDatabase.db")
                              //  .addCallback(fillDatabaseWithFirebaseValues())
                                .build();

                        Log.d("debago","Build database");

                    }

                }
            }else{
                ManageDatabaseFilling.saveDatabaseFillingState(MainApplication.getInstance().getApplicationContext(),false);
            }

        return INSTANCE;
    }

}
