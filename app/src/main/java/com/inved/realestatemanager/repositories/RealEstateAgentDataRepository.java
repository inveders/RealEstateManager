package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.RealEstateAgentsDao;
import com.inved.realestatemanager.models.RealEstateAgents;

public class RealEstateAgentDataRepository {

    private final RealEstateAgentsDao realEstateAgentsDao;

    public RealEstateAgentDataRepository(RealEstateAgentsDao userDao) { this.realEstateAgentsDao = userDao; }

    // --- GET REAL ESTATE AGENT ---
    public LiveData<RealEstateAgents> getRealEstateAgent(long realEstateAgentId) { return this.realEstateAgentsDao.getRealEstateAgent(realEstateAgentId); }
}
