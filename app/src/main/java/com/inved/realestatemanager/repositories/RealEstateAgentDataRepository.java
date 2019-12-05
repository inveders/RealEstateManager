package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.RealEstateAgentsDao;
import com.inved.realestatemanager.models.RealEstateAgents;

import java.util.List;

public class RealEstateAgentDataRepository {

    private final RealEstateAgentsDao realEstateAgentsDao;

    public RealEstateAgentDataRepository(RealEstateAgentsDao userDao) { this.realEstateAgentsDao = userDao; }

    // --- GET REAL ESTATE AGENT ---
    public LiveData<RealEstateAgents> getRealEstateAgentByName(String firstname, String lastname) { return this.realEstateAgentsDao.getRealEstateAgentByName(firstname,lastname); }

    public LiveData<RealEstateAgents> getRealEstateAgentById(long realEstateAgentId) { return this.realEstateAgentsDao.getRealEstateAgentById(realEstateAgentId); }

    public LiveData<RealEstateAgents> getRealEstateAgentByEmail(String email) { return this.realEstateAgentsDao.getRealEstateAgentByEmail(email); }


    // --- GET ALL REAL ESTATE AGENTS ---
    public LiveData<List<RealEstateAgents>> getAllRealEstateAgents() { return this.realEstateAgentsDao.getAllRealEstateAgents(); }

    public void deleteRealEstateAgent(long realEstateAgentId) { this.realEstateAgentsDao.deleteRealEstateAgent(realEstateAgentId); }

// --- CREATE ---

    public void createAgent(RealEstateAgents realEstateAgents){ realEstateAgentsDao.createRealEstateAgent(realEstateAgents); }

    // --- UPDATE ---

    public int updateAgent(String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId,String email,long realEstateAgentId){ return this.realEstateAgentsDao.updateAgent(firstname, lastname, urlPicture, agencyName, agencyPlaceId,email,realEstateAgentId); }

}
