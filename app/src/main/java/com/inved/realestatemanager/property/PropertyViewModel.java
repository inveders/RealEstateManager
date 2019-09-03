package com.inved.realestatemanager.property;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testimmo.models.Property;
import com.example.testimmo.models.RealEstateAgents;
import com.example.testimmo.repositories.PropertyDataRepository;
import com.example.testimmo.repositories.RealEstateAgentDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    // REPOSITORIES
    private final PropertyDataRepository propertyDataSource;
    private final RealEstateAgentDataRepository realEstateAgentDataSource;
    private final Executor executor; //Nous utilisons la classe Executor afin de réaliser de manière asynchrone les requêtes de mise à jour de nos tables SQLite.

    // DATA
    @Nullable
    private LiveData<RealEstateAgents> currentAgent;

    public PropertyViewModel(PropertyDataRepository propertyDataSource, RealEstateAgentDataRepository realEstateAgentDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.realEstateAgentDataSource = realEstateAgentDataSource;
        this.executor = executor;
    }

    public void init(long realEstateAgentId) {
        if (this.currentAgent != null) {
            return;
        }
        currentAgent = realEstateAgentDataSource.getRealEstateAgent(realEstateAgentId);
    }

    // -------------
    // FOR USER
    // -------------

    public LiveData<RealEstateAgents> getRealEstateAgent(long realEstateAgentId) { return this.currentAgent;  }

    // -------------
    // FOR ITEM
    // -------------

    public LiveData<List<Property>> getProperties(long realEstateAgentId) {
        return propertyDataSource.getItems(realEstateAgentId);
    }

    public void createProperty(Property property) {
        executor.execute(() -> {
            propertyDataSource.createItem(property);
        });
    }

    public void deleteProperty(long propertyId) {
        executor.execute(() -> {
            propertyDataSource.deleteItem(propertyId);
        });
    }

    public void updateProperty(Property property) {
        executor.execute(() -> {
            propertyDataSource.updateItem(property);
        });
    }
}
