package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.PropertyDao;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.RoomQuery;

import java.util.List;

public class PropertyDataRepository {

    //Le but du repository est vraiment d'isoler la source de données (DAO) du ViewModel, afin que ce dernier ne manipule pas directement la source de données.

    private final PropertyDao propertyDao;
    private RoomQuery roomQuery = new RoomQuery();

    public PropertyDataRepository(PropertyDao propertyDao) { this.propertyDao = propertyDao; }

    // --- GET ---

    public LiveData<List<Property>> getItems(long realEstateAgentId){ return this.propertyDao.getProperty(realEstateAgentId); }

    public LiveData<Property> getOneItem(long propertyId){ return this.propertyDao.getOneProperty(propertyId); }

    // --- SEARCH ---

    public LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                     int minBedRoom, int maxBedRoom, String country, String status, long realEstateAgentId) {
        return this.propertyDao.searchProperty(roomQuery.queryRoomDatabase(type, town, minSurface, maxSurface, minPrice, maxPrice, minBedRoom,maxBedRoom,country,status,realEstateAgentId));

    }


    // --- CREATE ---

    public void createItem(Property property){ propertyDao.insertProperty(property); }

    // --- DELETE ---
    public void deleteItem(long propertyId){ propertyDao.deleteProperty(propertyId); }

    // --- UPDATE ---
    public void updateItem(Property property){ propertyDao.updateProperty(property); }
}