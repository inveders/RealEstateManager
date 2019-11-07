package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.RealEstateAgentsDao;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

public class RealEstateAgentDataRepository {

    private final RealEstateAgentsDao realEstateAgentsDao;

    public RealEstateAgentDataRepository(RealEstateAgentsDao userDao) { this.realEstateAgentsDao = userDao; }

    // --- GET REAL ESTATE AGENT ---
    public LiveData<RealEstateAgents> getRealEstateAgent(long realEstateAgentId) { return this.realEstateAgentsDao.getRealEstateAgent(realEstateAgentId); }

    // --- GET ALL REAL ESTATE AGENTS ---
    public LiveData<List<RealEstateAgents>> getAllRealEstateAgents() { return this.realEstateAgentsDao.getAllRealEstateAgents(); }



// --- CREATE ---

    public void createAgent(RealEstateAgents realEstateAgents){ realEstateAgentsDao.createRealEstateAgent(realEstateAgents); }

}
