package com.inved.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.inved.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<List<Property>> getProperty(long realEstateAgentId);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getOneProperty(long propertyId);

   /* @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId OR typeProperty LIKE :type OR townProperty LIKE :town OR surfaceAreaProperty BETWEEN :minSurface AND :maxSurface " +
            " OR pricePropertyInDollar BETWEEN :minPrice AND :maxPrice OR numberBedroomsInProperty BETWEEN :minBedRoom AND :maxBedRoom " +
            " OR country LIKE :country OR statusProperty LIKE :status ")
    LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                int minBedRoom, int maxBedRoom, String country, String status, long realEstateAgentId);*/


    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> searchProperty(SupportSQLiteQuery query);

    @Insert
    long insertProperty(Property property);

    @Update
    int updateProperty(Property property);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deleteProperty(long propertyId);
}

