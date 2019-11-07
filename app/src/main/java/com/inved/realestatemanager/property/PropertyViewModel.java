package com.inved.realestatemanager.property;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.repositories.PropertyDataRepository;
import com.inved.realestatemanager.repositories.RealEstateAgentDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    // REPOSITORIES
    private final PropertyDataRepository propertyDataSource;
    private final RealEstateAgentDataRepository realEstateAgentDataSource;
    private final Executor executor; //Nous utilisons la classe Executor afin de réaliser de manière asynchrone les requêtes de mise à jour de nos tables SQLite.


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

    public LiveData<RealEstateAgents> getRealEstateAgentById(long realEstateAgentId) {
        return realEstateAgentDataSource.getRealEstateAgentById(realEstateAgentId);
    }

    public LiveData<List<RealEstateAgents>> getAllRealEstateAgents() {
        return realEstateAgentDataSource.getAllRealEstateAgents();
    }

    public void createRealEstateAgent(RealEstateAgents realEstateAgent) {

        executor.execute(() -> realEstateAgentDataSource.createAgent(realEstateAgent));

    }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Property>> getProperties(long realEstateAgentId) {
        return propertyDataSource.getItems(realEstateAgentId);
    }

    public LiveData<Property> getOneProperty(long propertyId) {
        return propertyDataSource.getOneItem(propertyId);
    }



    public void createProperty(Property property) {
        executor.execute(() -> propertyDataSource.createItem(property));
    }

    public void deleteProperty(long propertyId) {
        executor.execute(() -> propertyDataSource.deleteItem(propertyId));
    }

    public void updateProperty(Property property) {
        executor.execute(() -> propertyDataSource.updateItem(property));
    }

    // -------------
    // FOR SEARCH
    // -------------

    public LiveData<List<Property>> searchProperty(String type, String town, double minSurface, double maxSurface, double minPrice, double maxPrice,
                                                   int minBedRoom, int maxBedRoom, String country, String status, long realEstateAgentId) {
        return propertyDataSource.searchProperty(type, town, minSurface, maxSurface, minPrice, maxPrice, minBedRoom,maxBedRoom,country,status,realEstateAgentId);
    }
}
