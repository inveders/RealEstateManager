package com.inved.realestatemanager.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.testimmo.property.PropertyViewModel;
import com.example.testimmo.repositories.PropertyDataRepository;
import com.example.testimmo.repositories.RealEstateAgentDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    //Une factory est simplement un pattern permettant de déléguer la création d'une classe à une autre

    private final PropertyDataRepository propertyDataSource;
    private final RealEstateAgentDataRepository realEstateAgentDataSource;
    private final Executor executor;

    public ViewModelFactory(PropertyDataRepository propertyDataSource, RealEstateAgentDataRepository realEstateAgentDataSource, Executor executor) {
        this.propertyDataSource = propertyDataSource;
        this.realEstateAgentDataSource = realEstateAgentDataSource;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyViewModel.class)) {
            return (T) new PropertyViewModel(propertyDataSource, realEstateAgentDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}