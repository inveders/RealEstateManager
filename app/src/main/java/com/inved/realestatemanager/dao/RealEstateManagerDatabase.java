package com.inved.realestatemanager.dao;

import android.content.Context;
import android.os.Handler;
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
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.sharedpreferences.ManageDatabaseFilling;
import com.inved.realestatemanager.utils.MainApplication;

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

     //   if(Utils.isInternetAvailable(MainApplication.getInstance().getApplicationContext())){
            if (INSTANCE == null) {
                synchronized (RealEstateManagerDatabase.class) {
                    if (INSTANCE == null) {
                        ManageDatabaseFilling.saveDatabaseFillingState(context.getApplicationContext(),true);
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                RealEstateManagerDatabase.class, "MyDatabase.db")
                                .addCallback(fillDatabaseWithFirebaseValues())
                                .build();

                        Log.d("debago","Build database");

                    }

                }
            }else{
                ManageDatabaseFilling.saveDatabaseFillingState(MainApplication.getInstance().getApplicationContext(),false);
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

                    RealEstateAgentHelper.getAgentWhateverAgency(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            if (task.getResult() != null) {
                                if (task.getResult().getDocuments().size() != 0) {
                                    //We have this agency in firebase and we have to put these items in a new Room database

                                    String agencyPlaceIdToSave = task.getResult().getDocuments().get(0).getString("agencyPlaceId");
                                    String agencyNameToSave = task.getResult().getDocuments().get(0).getString("agencyName");
                                    Log.d("debago", "DATABASE We have an agency, we create database and fill it: " + agencyPlaceIdToSave);

                                    ManageAgency.saveAgencyPlaceId(MainApplication.getInstance().getApplicationContext(), agencyPlaceIdToSave);
                                    ManageAgency.saveAgencyName(MainApplication.getInstance().getApplicationContext(), agencyNameToSave);
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

            if (queryDocumentSnapshots.size() > 0) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    RealEstateAgents realEstateAgents = documentSnapshot.toObject(RealEstateAgents.class);

                    RealEstateAgents newAgent = RealEstateAgentHelper.resetAgent(realEstateAgents);

                    //Create real estate agent in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).realEstateAgentsDao().createRealEstateAgent(newAgent));
                }

                final Handler handler = new Handler();
                handler.postDelayed(RealEstateManagerDatabase::preopopulateProperties, 1000);

            }


        }).addOnFailureListener(e -> {});
    }

    private static void preopopulateProperties(){
        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {
            //Log.d("debago", "check if getAllProperty not null: " + queryDocumentSnapshots.size());
            if (queryDocumentSnapshots.size() > 0) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Property property = documentSnapshot.toObject(Property.class);

                    Property newProperty = PropertyHelper.resetProperties(property);

                    //Create property in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(MainApplication.getInstance().getApplicationContext()).propertyDao().insertProperty(newProperty));


                }
            }
            ManageDatabaseFilling.saveDatabaseFillingState(MainApplication.getInstance().getApplicationContext(),false);

        }).addOnFailureListener(e -> {

        });
    }
}
