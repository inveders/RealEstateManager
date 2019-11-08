package com.inved.realestatemanager.controller.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.dialogs.AddAgentDialog;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.RecyclerViewAgentManagement;

public class AgentManagementActivity extends BaseActivity implements RecyclerViewAgentManagement.AgentManagementInterface {

    private PropertyViewModel propertyViewModel;
    private RecyclerViewAgentManagement adapter;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_agent_management;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = findViewById(R.id.agent_management_recycler_view);
        adapter = new RecyclerViewAgentManagement(this, this);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        FloatingActionButton addAgentFloatingButton = findViewById(R.id.agent_management_add_floating_button);
        this.configureViewModel();

        addAgentFloatingButton.setOnClickListener(v -> openDialog());

    }

    private void openDialog() {

        AddAgentDialog dialog = new AddAgentDialog();
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");
    }

    @Override
    protected void configToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agent Management");
        }

    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        propertyViewModel.getAllRealEstateAgents().observe(this, realEstateAgents -> adapter.setData(realEstateAgents));
    }

    @Override
    public void onClickDeleteButton(long realEstateAgentId) {

        propertyViewModel.getProperties(realEstateAgentId).observe(this, properties -> {
            if (properties.size() > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.agent_management_no_delete_possible)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.agent_management_ok_choice), (dialog, id) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();


            } else {
                propertyViewModel.deleteRealEstateAgent(realEstateAgentId);
            }
        });


    }

    @Override
    public void onEditAgent(long id) {

        AddAgentDialog dialog = new AddAgentDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("myLong", id);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");

    }
}
