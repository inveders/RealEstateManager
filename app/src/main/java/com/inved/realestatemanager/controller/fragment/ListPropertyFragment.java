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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyAdapter;
import com.inved.realestatemanager.property.PropertyViewModel;

import java.util.List;

import butterknife.BindView;

public class ListPropertyFragment extends Fragment implements PropertyAdapter.Listener {

    // FOR DESIGN
    @BindView(R.id.fragment_list_property_recycler_view)
    RecyclerView recyclerView;
    private PropertyAdapter adapter;
    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    private Context context =getContext();
    private static int REAL_ESTATE_AGENT_ID = 1;

    public ListPropertyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_property, container, false);

        // Configure RecyclerView & ViewModel
        this.configureRecyclerView();
        this.configureViewModel();
        this.getProperties(REAL_ESTATE_AGENT_ID);

        return mView;
    }

    // 4 - Configure RecyclerView
    private void configureRecyclerView(){
        this.adapter = new PropertyAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));
          /* ItemClickSupport.addTo(recyclerView, R.layout.activity_todo_list_item)
                .setOnItemClickListener((recyclerView1, position, v) -> this.updateProperty(this.adapter.getProperty(position)));*/
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(context);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }

    // 3 - Get all properties for a real estate agent
    private void getProperties(int realEstateAgentId) {
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
    private void getCurrentAgent(int realEstateAgentId) {
        this.propertyViewModel.getRealEstateAgent(realEstateAgentId);
    }

    @Override
    public void onClickDeleteButton(int position) {
        // 7 - Delete property after user clicked on button
        // this.deleteProperty(this.adapter.getProperty(position)); NO NEED IN THIS APP
    }
}
