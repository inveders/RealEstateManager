package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;

public class UpdatePropertyActivity extends BaseActivity {

    private static int REAL_ESTATE_AGENT_ID = 1;
    private PropertyViewModel propertyViewModel;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_update_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolBar();


    }

    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.page_name_activity_udpdate_property);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    // 3 - Update an property (selected or not)
    private void updateProperty(Property property) {
        property.setSelected(!property.isSelected());
        this.propertyViewModel.updateProperty(property);
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }
}
