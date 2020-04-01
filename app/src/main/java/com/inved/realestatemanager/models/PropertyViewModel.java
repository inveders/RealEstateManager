package com.inved.realestatemanager.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.inved.realestatemanager.repositories.PropertyDataRepository;
import com.inved.realestatemanager.repositories.RealEstateAgentDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    // REPOSITORIES
    private final PropertyDataRepository propertyDataSource;
    private final RealEstateAgentDataRepository realEstateAgentDataSource;
    private final Executor executor; //We use Executor class to make asynschrone requests to update database SQLite tables.


    public PropertyViewModel(PropertyDataRepository propertyDataSource, RealEstateAgentDataRepository realEstateAgentDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.realEstateAgentDataSource = realEstateAgentDataSource;
        this.executor = executor;
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<RealEstateAgents> getRealEstateAgentByName(String firstname, String lastname) {
        return realEstateAgentDataSource.getRealEstateAgentByName(firstname,lastname);
    }

    public LiveData<RealEstateAgents> getRealEstateAgentById(String realEstateAgentId) {
        return realEstateAgentDataSource.getRealEstateAgentById(realEstateAgentId);
    }

    public LiveData<List<RealEstateAgents>> getAllRealEstateAgents() {
        return realEstateAgentDataSource.getAllRealEstateAgents();
    }

    public void createRealEstateAgent(RealEstateAgents realEstateAgent) {

        executor.execute(() -> realEstateAgentDataSource.createAgent(realEstateAgent));
    }

    public void updateRealEstateAgent(String realEstateAgentId, String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId) {

        executor.execute(() -> realEstateAgentDataSource.updateAgent(realEstateAgentId,firstname, lastname, urlPicture, agencyName, agencyPlaceId));
    }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Property>> getAllPropertiesForOneAgent(String realEstateAgentId) {
        return propertyDataSource.getItems(realEstateAgentId);
    }

    public LiveData<List<Property>> getAllProperties() {
        return propertyDataSource.getAllItems();
    }

    public LiveData<Property> getOneProperty(String propertyId) {
        return propertyDataSource.getOneItem(propertyId);
    }

    public int getMaxPrice() {
        return propertyDataSource.getMaxPrice();
    }

    public int getMaxSurface() {
        return propertyDataSource.getMaxSurface();
    }

    public void createProperty(Property property) {
         executor.execute(() -> propertyDataSource.createItem(property));

    }

    public void deleteProperty(String propertyId) {
        executor.execute(() -> propertyDataSource.deleteItem(propertyId));
    }

    public void updateProperty(String typeProperty, double pricePropertyInEuro,
                                                   double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                                   int numberBedroomsInProperty, String fullDescriptionText, String streetNumber,
                                                   String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                                                   String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                                                   boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                                                   String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                                                   String photoDescription4, String photoDescription5, String realEstateAgentId,String propertyId) {
        executor.execute(() -> propertyDataSource.updateProperty(typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty, numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty, dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId,propertyId));
    }



    public void updateDateOfSaleForProperty(String dateOfSaleForProperty,String status,String propertyId) {
        executor.execute(() -> propertyDataSource.updateDateOfSaleForProperty(dateOfSaleForProperty,status,propertyId));
    }

    // -------------
    // FOR SEARCH
    // -------------

    public LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                   int minBedRoom, int maxBedRoom, String country, String status, String realEstateAgentId) {
        return propertyDataSource.searchProperty(type, town, minSurface, maxSurface, minPrice, maxPrice, minBedRoom,maxBedRoom,country,status,realEstateAgentId);
    }

}
