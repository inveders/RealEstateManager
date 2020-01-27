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
import com.inved.realestatemanager.controller.dialogs.AddAgentDialog;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.RecyclerViewAgentManagement;

public class AgentManagementActivity extends BaseActivity implements RecyclerViewAgentManagement.AgentManagementInterface {

    private RecyclerViewAgentManagement adapter;

    // --------------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configToolbar();
        RecyclerView recyclerView = findViewById(R.id.agent_management_recycler_view);
        adapter = new RecyclerViewAgentManagement(this, this);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.configureViewModel();

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_agent_management;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        PropertyViewModel propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        propertyViewModel.getAllRealEstateAgents().observe(this, realEstateAgents -> adapter.setData(realEstateAgents));
    }

    // --------------------
    // TOOLBAR
    // --------------------

    private void configToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar_agent_management);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.page_name_activity_agent_management));
        }

    }

    // --------------------
    // AGENT
    // --------------------

    @Override
    public void onEditAgent(String id) {

        AddAgentDialog dialog = new AddAgentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("myRealEstateAgentId", id);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");

    }
}
