package com.inved.realestatemanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

@Dao
public interface RealEstateAgentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRealEstateAgent(RealEstateAgents realEstateAgents);

    @Query("SELECT * FROM RealEstateAgents WHERE firstname LIKE :firstname AND lastname LIKE :lastname")
    LiveData<RealEstateAgents> getRealEstateAgentByName(String firstname, String lastname);


    @Query("SELECT * FROM RealEstateAgents WHERE id = :realEstateAgentId")
    LiveData<RealEstateAgents> getRealEstateAgentById(long realEstateAgentId);

    @Query("SELECT * FROM RealEstateAgents")
    LiveData<List<RealEstateAgents>> getAllRealEstateAgents();

    @Query("DELETE FROM RealEstateAgents WHERE id = :realEstateAgentId")
    int deleteRealEstateAgent(long realEstateAgentId);

    @Update
    int updateAgent(RealEstateAgents realEstateAgents);
}
