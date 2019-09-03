package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.testimmo.database.dao.RealEstateAgentsDao;
import com.example.testimmo.models.RealEstateAgents;

public class RealEstateAgentDataRepository {

    private final RealEstateAgentsDao realEstateAgentsDao;

    public RealEstateAgentDataRepository(RealEstateAgentsDao userDao) { this.realEstateAgentsDao = userDao; }

    // --- GET REAL ESTATE AGENT ---
    public LiveData<RealEstateAgents> getRealEstateAgent(long realEstateAgentId) { return this.realEstateAgentsDao.getRealEstateAgent(realEstateAgentId); }
}
