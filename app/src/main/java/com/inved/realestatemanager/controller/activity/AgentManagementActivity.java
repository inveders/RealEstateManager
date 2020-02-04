package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.dialogs.AddAgentDialog;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.firebase.StorageDownload;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.RecyclerViewAgentManagement;

public class AgentManagementActivity extends BaseActivity implements RecyclerViewAgentManagement.AgentManagementInterface {

    private RecyclerViewAgentManagement adapter;
    private PropertyViewModel propertyViewModel;

    // --------------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configToolbar();
        this.configureRecyclerView();
        this.configureViewModel();
        this.checkIfAllAgentsAreInRoom();

    }

    private void checkIfAllAgentsAreInRoom() {

        RealEstateAgentHelper.getAllAgents().get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots.size() > 0) {

                propertyViewModel.getAllRealEstateAgents().observe(this, realEstateAgents -> {
                    Log.d("debago", "agents in firebase " + queryDocumentSnapshots.size() + " and agents in room is " + realEstateAgents.size());

                    if (queryDocumentSnapshots.size() != realEstateAgents.size()) {

                        //We create agents from firebase in room
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            RealEstateAgents realEstateAgents1 = documentSnapshot.toObject(RealEstateAgents.class);

                            String realEstateAgentId = realEstateAgents1.getRealEstateAgentId();
                            String agencyPlaceId = realEstateAgents1.getAgencyPlaceId();
                            String agencyName = realEstateAgents1.getAgencyName();
                            String firstname = realEstateAgents1.getFirstname();
                            String lastname = realEstateAgents1.getLastname();
                            String urlPicture = realEstateAgents1.getUrlPicture();

                            StorageDownload storageDownload = new StorageDownload();

                            if (urlPicture != null && urlPicture.length() < 30) {
                                String uri1 = storageDownload.beginDownload(urlPicture, realEstateAgentId);
                                if (uri1 != null) {
                                    urlPicture = uri1;
                                }
                            }

                            RealEstateAgents newAgent = new RealEstateAgents(realEstateAgentId, firstname, lastname, urlPicture, agencyName, agencyPlaceId);

                            //Create agent in room with data from firebase
                            propertyViewModel.createRealEstateAgent(newAgent);
                        }


                    }


                });


            }


        }).addOnFailureListener(e -> {
        });

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_agent_management;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        propertyViewModel = new ViewModelProvider(this, mViewModelFactory).get(PropertyViewModel.class);
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
    // RECYCLER VIEW
    // --------------------

    private void configureRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.agent_management_recycler_view);
        adapter = new RecyclerViewAgentManagement(this, this);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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
