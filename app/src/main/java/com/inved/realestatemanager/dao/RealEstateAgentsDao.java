package com.inved.realestatemanager.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.models.RealEstateAgents;

@Dao
public interface RealEstateAgentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRealEstateAgent(RealEstateAgents realEstateAgents);

    @Query("SELECT * FROM RealEstateAgents WHERE id = :realEstateAgentId")
    LiveData<RealEstateAgents> getRealEstateAgent(long realEstateAgentId);
}
