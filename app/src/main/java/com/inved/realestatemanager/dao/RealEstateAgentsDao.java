package com.inved.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

@Dao
public interface RealEstateAgentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRealEstateAgent(RealEstateAgents realEstateAgents);

    @Query("SELECT * FROM RealEstateAgents WHERE firstname LIKE :firstname AND lastname LIKE :lastname")
    LiveData<RealEstateAgents> getRealEstateAgentByName(String firstname, String lastname);


    @Query("SELECT * FROM RealEstateAgents WHERE realEstateAgentId = :realEstateAgentId")
    LiveData<RealEstateAgents> getRealEstateAgentById(long realEstateAgentId);

    @Query("SELECT * FROM RealEstateAgents WHERE email = :email")
    LiveData<RealEstateAgents> getRealEstateAgentByEmail(String email);

    @Query("SELECT * FROM RealEstateAgents")
    LiveData<List<RealEstateAgents>> getAllRealEstateAgents();

    @Query("DELETE FROM RealEstateAgents WHERE realEstateAgentId = :realEstateAgentId")
    int deleteRealEstateAgent(long realEstateAgentId);

    @Query("UPDATE RealEstateAgents SET firstname = :firstname, lastname = :lastname,urlPicture = :urlPicture,agencyName = :agencyName,agencyPlaceId = :agencyPlaceId,email = :email WHERE realEstateAgentId = :realEstateAgentId")
    int updateAgent(String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId,String email,long realEstateAgentId);
}
