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

@Database(entities = {Property.class, RealEstateAgents.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile RealEstateManagerDatabase INSTANCE;

    // --- DAO ---
    public abstract RealEstateAgentsDao realEstateAgentsDao();
    public abstract PropertyDao propertyDao();


    // --- INSTANCE ---
    public static RealEstateManagerDatabase getInstance(Context context) {

        context.deleteDatabase("MyDatabase.db");

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
                contentValues.put("firstname", "Alexandra Lembe");
                contentValues.put("lastname", "Gnimadi");
                contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");

                db.insert("RealEstateAgents", OnConflictStrategy.IGNORE, contentValues);

                ContentValues contentValuesProperty = new ContentValues();
                contentValuesProperty.put("propertyId", 1);
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
                contentValuesProperty.put("addressProperty", "42 bis rue principale 57840 Ottange");
                contentValuesProperty.put("pointOfInterest", "school,car parks");
                contentValuesProperty.put("statusProperty", "in progress");
                contentValuesProperty.put("dateOfEntryOnMarketForProperty", "29/10/2019");

                /*

                this.dateOfEntryOnMarketForProperty = dateOfEntryOnMarketForProperty;
                this.dateOfSaleForPorperty = dateOfSaleForPorperty;
                this.selected = selected;
                this.photoUri1 = photoUri1;
                this.photoUri2 = photoUri2;
                this.photoUri3 = photoUri3;
                this.photoUri4 = photoUri4;
                this.photoUri5 = photoUri5;
                this.photoDescription1 = photoDescription1;
                this.photoDescription2 = photoDescription2;
                this.photoDescription3 = photoDescription3;
                this.photoDescription4 = photoDescription4;
                this.photoDescription5 = photoDescription5;
                this.realEstateAgentId = realEstateAgentId;*/

                db.insert("Property", OnConflictStrategy.IGNORE, contentValuesProperty);
            }
        };
    }

    private static final Migration MIGRATION_2_4 = new Migration(2,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

         /*   database.execSQL("ALTER TABLE Property "
                    + " RENAME COLUMN streeNumber TO streetNumber");*/

        }
    };

}
