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

       // context.deleteDatabase("MyDatabase.db");

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
                contentValues.put("firstname", "Alexandra");
                contentValues.put("lastname", "Gnimadi");
                contentValues.put("urlPicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2");

                db.insert("RealEstateAgents", OnConflictStrategy.IGNORE, contentValues);
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
