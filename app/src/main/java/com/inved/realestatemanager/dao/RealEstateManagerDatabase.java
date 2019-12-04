package com.inved.realestatemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

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
                            .addCallback(fillDatabaseWithFirebaseValues())
                            .build();



                }

            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback fillDatabaseWithFirebaseValues() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    RealEstateAgentHelper.getAgentWhateverAgency(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            if (task.getResult() != null) {
                                Log.d("debago", "LAUNCH 1");
                                if (task.getResult().getDocuments().size() != 0) {
                                    //We have this agency in firebase and we have to put these items in a new Room database

                                    String agencyPlaceIdToSave = task.getResult().getDocuments().get(0).getString("agencyPlaceId");


                                    Log.d("debago", "DATABASE We have an agency, we create database and fill it: " + agencyPlaceIdToSave);

                                    ManageAgency.saveAgencyPlaceId(MainApplication.getInstance().getApplicationContext(), agencyPlaceIdToSave);

                                    RealEstateAgentHelper.getAllAgents().get().addOnSuccessListener(queryDocumentSnapshots -> {
                                        Log.d("debago", "check if getAllAgent not null: " + queryDocumentSnapshots.size());

                                        if (queryDocumentSnapshots.size() > 0) {

                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                RealEstateAgents realEstateAgents = documentSnapshot.toObject(RealEstateAgents.class);

                                                String firstname = realEstateAgents.getFirstname();
                                                String lastname = realEstateAgents.getLastname();
                                                String urlPicture = realEstateAgents.getUrlPicture();
                                                String agencyName = realEstateAgents.getAgencyName();
                                                String agencyPlaceId = realEstateAgents.getAgencyPlaceId();
                                                String email = realEstateAgents.getEmail();

                                                Log.d("debago", "create agent list: " + realEstateAgents.toString());

                                                ContentValues contentValues = new ContentValues();
                                                contentValues.put("realEstateAgentId", 1);
                                                contentValues.put("firstname", firstname);
                                                contentValues.put("lastname", lastname);
                                                contentValues.put("urlPicture", urlPicture);
                                                contentValues.put("agencyName", agencyName);
                                                contentValues.put("agencyPlaceId", agencyPlaceId);
                                                contentValues.put("email", email);

                                                db.insert("RealEstateAgents", OnConflictStrategy.IGNORE, contentValues);
                                            }

                                        }


                                    }).addOnFailureListener(e -> {

                                    });


                                    PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {
                                        Log.d("debago", "check if getAllProperty not null: " + queryDocumentSnapshots.size());
                                        if (queryDocumentSnapshots.size() > 0) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                Property property = documentSnapshot.toObject(Property.class);

                                                String typeProperty = property.getTypeProperty();
                                                double pricePropertyInDollar = property.getPricePropertyInDollar();
                                                double surfaceAreaProperty = property.getSurfaceAreaProperty();
                                                String numberRoomsInProperty = property.getNumberRoomsInProperty();
                                                String numberBathroomsInProperty = property.getNumberBathroomsInProperty();
                                                int numberBedroomsInProperty = property.getNumberBedroomsInProperty();
                                                String fullDescriptionProperty = property.getFullDescriptionProperty();

                                                String streetNumber = property.getStreetNumber();
                                                String streetName = property.getStreetName();
                                                String zipCode = property.getZipCode();
                                                String townProperty = property.getTownProperty();
                                                String country = property.getCountry();
                                                String addressCompl = property.getAddressCompl();
                                                String pointOfInterest = property.getPointOfInterest();
                                                String statusProperty = property.getStatusProperty();
                                                String dateOfEntryOnMarketForProperty = property.getDateOfEntryOnMarketForProperty();
                                                String dateOfSaleForPorperty = property.getDateOfSaleForPorperty();
                                                boolean selected = property.isSelected();
                                                String photoUri1 = property.getPhotoUri1();
                                                String photoUri2 = property.getPhotoUri2();
                                                String photoUri3 = property.getPhotoUri3();
                                                String photoUri4 = property.getPhotoUri4();
                                                String photoUri5 = property.getPhotoUri5();
                                                String photoDescription1 = property.getPhotoDescription1();
                                                String photoDescription2 = property.getPhotoDescription2();
                                                String photoDescription3 = property.getPhotoDescription3();
                                                String photoDescription4 = property.getPhotoDescription4();
                                                String photoDescription5 = property.getPhotoDescription5();
                                                String realEstateAgentEmail = property.getRealEstateAgentEmail();
                                                long realEstateAgentId = property.getRealEstateAgentId();

                                                ContentValues contentValuesProperty = new ContentValues();
                                                contentValuesProperty.put("propertyId", 1);
                                                contentValuesProperty.put("realEstateAgentId", realEstateAgentId);
                                                contentValuesProperty.put("typeProperty", typeProperty);
                                                contentValuesProperty.put("pricePropertyInDollar", pricePropertyInDollar);
                                                contentValuesProperty.put("surfaceAreaProperty", surfaceAreaProperty);
                                                contentValuesProperty.put("numberRoomsInProperty", numberRoomsInProperty);
                                                contentValuesProperty.put("numberBathroomsInProperty", numberBathroomsInProperty);
                                                contentValuesProperty.put("numberBedroomsInProperty", numberBedroomsInProperty);
                                                contentValuesProperty.put("fullDescriptionProperty", fullDescriptionProperty);
                                                contentValuesProperty.put("streetNumber", streetNumber);
                                                contentValuesProperty.put("streetName", streetName);
                                                contentValuesProperty.put("zipCode", zipCode);
                                                contentValuesProperty.put("townProperty", townProperty);
                                                contentValuesProperty.put("country", country);
                                                contentValuesProperty.put("addressCompl", addressCompl);
                                                contentValuesProperty.put("pointOfInterest", pointOfInterest);
                                                contentValuesProperty.put("statusProperty", statusProperty);
                                                contentValuesProperty.put("dateOfEntryOnMarketForProperty", dateOfEntryOnMarketForProperty);
                                                contentValuesProperty.put("dateOfSaleForPorperty", dateOfSaleForPorperty);
                                                contentValuesProperty.put("selected", selected);
                                                contentValuesProperty.put("realEstateAgentEmail", realEstateAgentEmail);
                                                contentValuesProperty.put("photoUri1", photoUri1);
                                                contentValuesProperty.put("photoUri2", photoUri2);
                                                contentValuesProperty.put("photoUri3", photoUri3);
                                                contentValuesProperty.put("photoUri4", photoUri4);
                                                contentValuesProperty.put("photoUri5", photoUri5);
                                                contentValuesProperty.put("photoDescription1", photoDescription1);
                                                contentValuesProperty.put("photoDescription2", photoDescription2);
                                                contentValuesProperty.put("photoDescription3", photoDescription3);
                                                contentValuesProperty.put("photoDescription4", photoDescription4);
                                                contentValuesProperty.put("photoDescription5", photoDescription5);

                                                db.insert("Property", OnConflictStrategy.IGNORE, contentValuesProperty);

                                            }

                                        }

                                    }).addOnFailureListener(e -> {

                                    });

                                }


                            } else {
                                Log.d("debago", "DATABASE No agency here, no successful");

                            }
                        }

                    }).addOnFailureListener(e -> Log.d("debago", "DATABASE ON FAILURE"));
                }


            }
        };
    }


}
