package com.inved.realestatemanager.dao;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

@Database(entities = {Property.class, RealEstateAgents.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract RealEstateAgentsDao realEstateAgentsDao();
    public abstract PropertyDao propertyDao();


    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {

     //   context.deleteDatabase("MyDatabase.db");

        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }

            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("realEstateAgentId", 1);
                contentValues.put("firstname", "Raphaël");
                contentValues.put("lastname", "Gnimadi");
                contentValues.put("urlPicture", "file:///storage/emulated/0/Android/data/com.inved.realestatemanager/files/Pictures/JPEG_20191107184447_4348563422494251245.jpg");
                contentValues.put("agencyName", "S&C BORBICONI IMMOBILIER");
                contentValues.put("agencyPlaceId", "ChIJ2V5Kw-0zlUcRzwWgILW09pU");
                contentValues.put("email", "mikoumusiqueservices@gmail.com");

                db.insert("RealEstateAgents", OnConflictStrategy.IGNORE, contentValues);

                ContentValues contentValuesProperty = new ContentValues();
                contentValuesProperty.put("propertyId", 1);
                contentValuesProperty.put("realEstateAgentId", 1);
                contentValuesProperty.put("typeProperty", "Flat");
                contentValuesProperty.put("pricePropertyInDollar", 120000.0);
                contentValuesProperty.put("surfaceAreaProperty", 75);
                contentValuesProperty.put("numberRoomsInProperty", "1");
                contentValuesProperty.put("numberBathroomsInProperty", "1");
                contentValuesProperty.put("numberBedroomsInProperty", "2");
                contentValuesProperty.put("fullDescriptionProperty", "It's a really good property near to the beach");
                contentValuesProperty.put("streetNumber", "42");
                contentValuesProperty.put("streetName", "rue principale");
                contentValuesProperty.put("zipCode", "57840");
                contentValuesProperty.put("townProperty", "Ottange");
                contentValuesProperty.put("country", "France");
                contentValuesProperty.put("addressCompl", "Bis");
                contentValuesProperty.put("pointOfInterest", "school,car parks");
                contentValuesProperty.put("statusProperty", "in progress");
                contentValuesProperty.put("dateOfEntryOnMarketForProperty", "29/10/2019");
                contentValuesProperty.put("dateOfSaleForPorperty", "14/12/2019" );
                contentValuesProperty.put("selected", false);
                contentValuesProperty.put("realEstateAgentEmail", "mikoumusiqueservices@gmail.com");

                db.insert("Property", OnConflictStrategy.IGNORE, contentValuesProperty);
            }
        };
    }

}
