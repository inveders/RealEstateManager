package com.inved.realestatemanager.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.inved.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<List<Property>> getProperty(String realEstateAgentId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperties();

    @Query("SELECT * FROM Property WHERE propertyId = :propertyId")
    Cursor getPropertiesWithCursor(String propertyId);

    @Query("SELECT * FROM Property WHERE propertyId = :propertyId")
    LiveData<Property> getOneProperty(String propertyId);

    @Query("SELECT MAX(pricePropertyInEuro) FROM Property")
    int getMaxPrice();

    @Query("SELECT MAX(surfaceAreaProperty) FROM Property")
    int getMaxSurface();

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> searchProperty(SupportSQLiteQuery query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProperty(Property property);

    @RawQuery(observedEntities = Property.class)
    int updateProperty(SupportSQLiteQuery query);


    @Query("UPDATE Property SET dateOfSaleForProperty = :dateOfSaleForProperty,statusProperty = :status WHERE propertyId = :propertyId")
    int updateDateOfSaleForProperty(String dateOfSaleForProperty,String status,String propertyId);

    @Query("UPDATE Property SET selected = :selected WHERE propertyId = :propertyId")
    int updateSelected(Boolean selected,String propertyId);

    @Query("DELETE FROM Property WHERE propertyId = :propertyId")
    void deleteProperty(String propertyId);

}

