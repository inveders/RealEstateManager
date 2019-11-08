package com.inved.realestatemanager.dao;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

import static java.sql.Types.NULL;

@Database(entities = {Property.class, RealEstateAgents.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract RealEstateAgentsDao realEstateAgentsDao();
    public abstract PropertyDao propertyDao();


    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {

//        context.deleteDatabase("MyDatabase.db");

        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateManagerDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                           // .addMigrations(MIGRATION_2_4)
                           // .fallbackToDestructiveMigration()
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
                contentValues.put("id", 1);
                contentValues.put("firstname", "Raphaël");
                contentValues.put("lastname", "Gnimadi");
                contentValues.put("urlPicture", "file:///storage/emulated/0/Android/data/com.inved.realestatemanager/files/Pictures/JPEG_20191107184447_4348563422494251245.jpg");

                db.insert("RealEstateAgents", OnConflictStrategy.IGNORE, contentValues);

                ContentValues contentValuesProperty = new ContentValues();
                contentValuesProperty.put("id", 1);
                contentValuesProperty.put("realEstateAgentId", 1);
                contentValuesProperty.put("typeProperty", "house");
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


                db.insert("Property", OnConflictStrategy.IGNORE, contentValuesProperty);
            }
        };
    }


    private static final Migration MIGRATION_2_4 = new Migration(2,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

         /*   database.execSQL("ALTER TABLE Property "
                    + " RENAME COLUMN streeNumber TO streetNumber");*/

        }
    };

}
