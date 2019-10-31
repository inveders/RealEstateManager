package com.inved.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.inved.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<List<Property>> getProperty(long realEstateAgentId);

    @Query("SELECT * FROM Property WHERE id = :propertyId")
    LiveData<Property> getOneProperty(long propertyId);

    @Query("SELECT * FROM Property WHERE realEstateAgentId = :realEstateAgentId AND typeProperty LIKE :type AND townProperty LIKE :town AND surfaceAreaProperty BETWEEN :minSurface AND :maxSurface " +
            " AND pricePropertyInDollar BETWEEN :minPrice AND :maxPrice AND numberBedroomsInProperty BETWEEN :minBedRoom AND :maxBedRoom " +
            " AND country LIKE :country AND statusProperty LIKE :status ")
    LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                int minBedRoom, int maxBedRoom, String country, String status, long realEstateAgentId);


    @Insert
    long insertProperty(Property property);

    @Update
    int updateProperty(Property property);

    @Query("DELETE FROM Property WHERE id = :propertyId")
    int deleteProperty(long propertyId);
}
