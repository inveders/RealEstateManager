package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.property.PropertyListAdapter;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.RecyclerViewAgentManagement;

public class AgentManagementActivity extends BaseActivity implements RecyclerViewAgentManagement.AgentManagementInterface {

    private RecyclerView recyclerView;
    private PropertyViewModel propertyViewModel;
    private RecyclerViewAgentManagement adapter;
    public static long REAL_ESTATE_AGENT_ID = 1;
    private Toolbar toolbar;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_agent_management;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.agent_management_recycler_view);
        adapter = new RecyclerViewAgentManagement(this,this);
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        this.configureViewModel();

    }

    @Override
    protected void configToolbar() {

        this.toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agent Management");
        }

    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        propertyViewModel.getAllRealEstateAgents().observe(this,realEstateAgents -> adapter.setData(realEstateAgents));
    }

    @Override
    public void onClickDeleteButton(long id) {

    }
}
