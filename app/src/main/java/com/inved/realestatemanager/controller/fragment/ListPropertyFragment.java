package com.inved.realestatemanager.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyListAdapter;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.List;


public class ListPropertyFragment extends Fragment implements PropertyListAdapter.Listener {

    // FOR DESIGN

    private RecyclerView recyclerView;
    private PropertyListAdapter adapter;
    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    public static long REAL_ESTATE_AGENT_ID = 1;
    private Context context;
    public static ListPropertyFragment newInstance(){
        return new ListPropertyFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        adapter=new PropertyListAdapter(context,this, Glide.with(this));
    }

    public ListPropertyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configure ViewModel
        this.configureViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_property, container, false);

        recyclerView = mView.findViewById(R.id.fragment_list_property_recycler_view);
        this.recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return mView;
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
        getProperties(REAL_ESTATE_AGENT_ID);
    }

    // 3 - Get all properties for a real estate agent
    private void getProperties(long realEstateAgentId) {
        this.propertyViewModel.getProperties(realEstateAgentId).observe(this, this::updatePropertyList);
    }

    // 6 - Update the list of properties
    private void updatePropertyList(List<Property> properties) {
        if (!properties.isEmpty()) {
            Log.d("debago", "MainActivity : updatepropertyList properties value is " + properties);
            this.adapter.updateData(properties);
        }
    }

    // 3 - Get Current Agent
    private void getCurrentAgent(long realEstateAgentId) {
        this.propertyViewModel.getRealEstateAgent(realEstateAgentId);
    }

    @Override
    public void onClickDeleteButton(int position) {
        // 7 - Delete property after user clicked on button
        // this.deleteProperty(this.adapter.getProperty(position)); NO NEED IN THIS APP
    }
}
