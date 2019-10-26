package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.PropertyDao;
import com.inved.realestatemanager.models.Property;

import java.util.List;

public class PropertyDataRepository {

    //Le but du repository est vraiment d'isoler la source de données (DAO) du ViewModel, afin que ce dernier ne manipule pas directement la source de données.

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao) { this.propertyDao = propertyDao; }

    // --- GET ---

    public LiveData<List<Property>> getItems(int realEstateAgentId){ return this.propertyDao.getProperty(realEstateAgentId); }

    // --- CREATE ---

    public void createItem(Property property){ propertyDao.insertProperty(property); }

    // --- DELETE ---
    public void deleteItem(long propertyId){ propertyDao.deleteProperty(propertyId); }

    // --- UPDATE ---
    public void updateItem(Property property){ propertyDao.updateProperty(property); }
}
