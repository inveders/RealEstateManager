package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.example.testimmo.database.dao.PropertyDao;
import com.example.testimmo.models.Property;

import java.util.List;

public class PropertyDataRepository {

    //Le but du repository est vraiment d'isoler la source de données (DAO) du ViewModel, afin que ce dernier ne manipule pas directement la source de données.

    private final PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao) { this.propertyDao = propertyDao; }

    // --- GET ---

    public LiveData<List<Property>> getItems(long propertyId){ return this.propertyDao.getProperty(propertyId); }

    // --- CREATE ---

    public void createItem(Property property){ propertyDao.insertProperty(property); }

    // --- DELETE ---
    public void deleteItem(long propertyId){ propertyDao.deleteProperty(propertyId); }

    // --- UPDATE ---
    public void updateItem(Property property){ propertyDao.updateProperty(property); }
}
