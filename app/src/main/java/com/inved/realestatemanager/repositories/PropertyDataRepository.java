package com.inved.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.inved.realestatemanager.dao.PropertyDao;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.RoomSearchQuery;
import com.inved.realestatemanager.utils.RoomUpdateQuery;

import java.util.List;

public class PropertyDataRepository {

    private final PropertyDao propertyDao;
    private RoomSearchQuery roomSearchQuery = new RoomSearchQuery();
    private RoomUpdateQuery roomUpdateQuery = new RoomUpdateQuery();

    public PropertyDataRepository(PropertyDao propertyDao) { this.propertyDao = propertyDao; }

    // --- GET ---

    public LiveData<List<Property>> getItems(String realEstateAgentId){ return this.propertyDao.getProperty(realEstateAgentId); }

    public LiveData<List<Property>> getAllItems(){ return this.propertyDao.getAllProperties(); }

    public LiveData<Property> getOneItem(String propertyId){ return this.propertyDao.getOneProperty(propertyId); }

    public int getMaxPrice(){return this.propertyDao.getMaxPrice();}

    public int getMaxSurface(){return this.propertyDao.getMaxSurface();}

    // --- SEARCH ---

    public LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                     int minBedRoom, int maxBedRoom, String country, String status, String realEstateAgentId) {
        return this.propertyDao.searchProperty(roomSearchQuery.queryRoomDatabase(type, town, minSurface, maxSurface, minPrice, maxPrice, minBedRoom,maxBedRoom,country,status,realEstateAgentId));

    }

    // --- CREATE ---

    public void createItem(Property property){propertyDao.insertProperty(property); }

    // --- DELETE ---
    public void deleteItem(String propertyId){ propertyDao.deleteProperty(propertyId); }

    // --- UPDATE ---

    public void updateDateOfSaleForProperty(String dateOfSale, String status, String propertyId){
        propertyDao.updateDateOfSaleForProperty(dateOfSale, status, propertyId);
    }


    public void updateProperty(String typeProperty, double pricePropertyInEuro,
                               double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                               int numberBedroomsInProperty, String fullDescriptionText, String streetNumber,
                               String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                               String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                               boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                               String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                               String photoDescription4, String photoDescription5, String realEstateAgentId, String propertyId) {
        this.propertyDao.updateProperty(roomUpdateQuery.queryRoomUpdateDatabase(typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty, numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty, dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId, propertyId));

    }


}