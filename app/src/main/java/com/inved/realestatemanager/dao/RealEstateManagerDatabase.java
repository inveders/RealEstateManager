package com.inved.realestatemanager.dao;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.firebase.StorageHelper;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

import java.io.IOException;
import java.util.concurrent.Executors;

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
                                if (task.getResult().getDocuments().size() != 0) {
                                    //We have this agency in firebase and we have to put these items in a new Room database

                                    String agencyPlaceIdToSave = task.getResult().getDocuments().get(0).getString("agencyPlaceId");


                                    Log.d("debago", "DATABASE We have an agency, we create database and fill it: " + agencyPlaceIdToSave);

                                    ManageAgency.saveAgencyPlaceId(MainApplication.getInstance().getApplicationContext(), agencyPlaceIdToSave);

                                    prepopulateRealEstateAgents();

                                    preopopulateProperties();


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

    private static void prepopulateRealEstateAgents() {
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
                    String realEstateAgentId = realEstateAgents.getRealEstateAgentId();

                    Log.d("debago", "create agent list: " + realEstateAgents.toString());

                    RealEstateAgents newAgent = new RealEstateAgents(realEstateAgentId,firstname, lastname, urlPicture, agencyName, agencyPlaceId);

                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).realEstateAgentsDao().createRealEstateAgent(newAgent));

                }

            }


        }).addOnFailureListener(e -> {

        });
    }

    private static void preopopulateProperties(){
        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.d("debago", "check if getAllProperty not null: " + queryDocumentSnapshots.size());
            if (queryDocumentSnapshots.size() > 0) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Property property = documentSnapshot.toObject(Property.class);

                    String propertyId = property.getPropertyId();
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
                    String realEstateAgentId = property.getRealEstateAgentId();

                    Property newProperty = new Property(propertyId,typeProperty, pricePropertyInDollar,
                            surfaceAreaProperty, numberRoomsInProperty,
                            numberBathroomsInProperty, numberBedroomsInProperty,
                            fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                            statusProperty, dateOfEntryOnMarketForProperty,
                            dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                            photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

                    Log.d("debago", "create property list: " + property.toString());

                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).propertyDao().insertProperty(newProperty));

                    StorageHelper storageHelper = new StorageHelper();
                    try {
                        storageHelper.beginDownload(photoUri1,propertyId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }).addOnFailureListener(e -> {

        });
    }
}
