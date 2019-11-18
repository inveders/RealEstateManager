package com.inved.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<List<Property>> getProperty(long realEstateAgentId);

    @Query("SELECT * FROM Property")
    LiveData<List<Property>> getAllProperties();


    @Query("SELECT * FROM Property WHERE propertyId = :propertyId")
    LiveData<Property> getOneProperty(long propertyId);

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> searchProperty(SupportSQLiteQuery query);

    @Insert
    long insertProperty(Property property);

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> updateProperty(SupportSQLiteQuery query);

    @Query("DELETE FROM Property WHERE propertyId = :propertyId")
    int deleteProperty(long propertyId);

    @Update
    int updatePropertyGood(Property property);
}

