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
import com.inved.realestatemanager.firebase.StorageDownload;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;
import com.inved.realestatemanager.utils.Utils;

import java.util.concurrent.Executors;

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


        if(Utils.isInternetAvailable(MainApplication.getInstance().getApplicationContext())){
            if (INSTANCE == null) {
                synchronized (RealEstateManagerDatabase.class) {
                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                RealEstateManagerDatabase.class, "MyDatabase.db")
                                .addCallback(fillDatabaseWithFirebaseValues())
                                .build();

                        Log.d("debago","Build database");

                    }

                }
            }
        }else{
            Log.d("debago","Pas de connexion internet, merci de réitérer");
        }



        return INSTANCE;
    }

    // ---

    private static Callback fillDatabaseWithFirebaseValues() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Log.d("debago", "in fill dqtqbqse yith firebqse vqlue");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    Log.d("debago", "auth not null");
                    RealEstateAgentHelper.getAgentWhateverAgency(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Log.d("debago", "task successfill");
                            if (task.getResult() != null) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    //We have this agency in firebase and we have to put these items in a new Room database

                                    String agencyPlaceIdToSave = task.getResult().getDocuments().get(0).getString("agencyPlaceId");


                                    Log.d("debago", "DATABASE We have an agency, we create database and fill it: " + agencyPlaceIdToSave);

                                    ManageAgency.saveAgencyPlaceId(MainApplication.getInstance().getApplicationContext(), agencyPlaceIdToSave);

                                    prepopulateRealEstateAgents();




                                }


                            } else {
                                Log.d("debago", "DATABASE No agency here, no successful");

                            }
                        }

                    }).addOnFailureListener(e -> Log.d("debago", "DATABASE ON FAILURE"));
                }else{
                    Log.d("debago","Nothing");
                }


            }
        };
    }

    private static void prepopulateRealEstateAgents() {
        RealEstateAgentHelper.getAllAgents().get().addOnSuccessListener(queryDocumentSnapshots -> {
           // Log.d("debago", "check if getAllAgent not null: " + queryDocumentSnapshots.size());

            if (queryDocumentSnapshots.size() > 0) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    RealEstateAgents realEstateAgents = documentSnapshot.toObject(RealEstateAgents.class);

                    String firstname = realEstateAgents.getFirstname();
                    String lastname = realEstateAgents.getLastname();
                    String urlPicture = realEstateAgents.getUrlPicture();
                    String agencyName = realEstateAgents.getAgencyName();
                    String agencyPlaceId = realEstateAgents.getAgencyPlaceId();
                    String realEstateAgentId = realEstateAgents.getRealEstateAgentId();

                   // Log.d("debago", "create agent list: " + realEstateAgents.toString());

                    RealEstateAgents newAgent = new RealEstateAgents(realEstateAgentId,firstname, lastname, urlPicture, agencyName, agencyPlaceId);

                    //Create real estate agent in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).realEstateAgentsDao().createRealEstateAgent(newAgent));

                }
                Log.d("debago","In database 2");
                preopopulateProperties();

            }


        }).addOnFailureListener(e -> Log.d("debago","NO CONNECTION"));
    }

    private static void preopopulateProperties(){
        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {
            //Log.d("debago", "check if getAllProperty not null: " + queryDocumentSnapshots.size());
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
                    String dateOfSaleForProperty = property.getDateOfSaleForProperty();
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

                    StorageDownload storageDownload = new StorageDownload();

                    if(photoUri1!=null && photoUri1.length()<30){
                        String uri1 = storageDownload.beginDownload(photoUri1,propertyId);
                        if(uri1!=null){
                            photoUri1=uri1;
                        }
                    }

                    if(photoUri2!=null&& photoUri2.length()<30){
                        String uri2= storageDownload.beginDownload(photoUri2,propertyId);
                        if(uri2!=null){
                            photoUri2=uri2;
                        }
                    }

                    if(photoUri3!=null && photoUri3.length()<30){
                        String uri3 = storageDownload.beginDownload(photoUri3,propertyId);
                        if(uri3!=null){
                            photoUri3=uri3;
                        }
                    }

                    if(photoUri4!=null && photoUri4.length()<30){
                        String uri4 = storageDownload.beginDownload(photoUri4,propertyId);
                        if(uri4!=null){
                            photoUri4=uri4;
                        }
                    }

                    if(photoUri5!=null && photoUri5.length()<30){
                        String uri5 = storageDownload.beginDownload(photoUri5,propertyId);
                        if(uri5!=null){
                            photoUri1=uri5;
                        }
                    }

                    Property newProperty = new Property(propertyId,typeProperty, pricePropertyInDollar,
                            surfaceAreaProperty, numberRoomsInProperty,
                            numberBathroomsInProperty, numberBedroomsInProperty,
                            fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                            statusProperty, dateOfEntryOnMarketForProperty,
                            dateOfSaleForProperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                            photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

                   // Log.d("debago", "create property list: " + property.toString());

                    //Create property in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).propertyDao().insertProperty(newProperty));


                }
               // Log.d("debago","In database 3");
            }

        }).addOnFailureListener(e -> {

        });
    }
}
