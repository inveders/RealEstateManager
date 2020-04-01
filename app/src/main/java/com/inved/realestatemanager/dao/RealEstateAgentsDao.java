package com.inved.realestatemanager.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

@Dao
public interface RealEstateAgentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createRealEstateAgent(RealEstateAgents realEstateAgents);

    @Query("SELECT * FROM RealEstateAgents WHERE firstname LIKE :firstname AND lastname LIKE :lastname")
    LiveData<RealEstateAgents> getRealEstateAgentByName(String firstname, String lastname);

    @Query("SELECT * FROM RealEstateAgents WHERE realEstateAgentId = :realEstateAgentId")
    Cursor getAgentsWithCursor(String realEstateAgentId);

    @Query("SELECT * FROM RealEstateAgents WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<RealEstateAgents> getRealEstateAgentById(String realEstateAgentId);

    @Query("SELECT * FROM RealEstateAgents")
    LiveData<List<RealEstateAgents>> getAllRealEstateAgents();

    @Query("DELETE FROM RealEstateAgents WHERE realEstateAgentId = :realEstateAgentId")
    void deleteRealEstateAgent(String realEstateAgentId);

    @Query("UPDATE RealEstateAgents SET firstname = :firstname, lastname = :lastname,urlPicture = :urlPicture,agencyName = :agencyName,agencyPlaceId = :agencyPlaceId WHERE realEstateAgentId = :realEstateAgentId")
    int updateAgent(String realEstateAgentId, String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId);
}
