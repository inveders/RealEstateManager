package com.inved.realestatemanager.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.controller.activity.DetailActivity;
import com.inved.realestatemanager.controller.dialogs.SearchFullScreenDialog;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.PropertyListAdapter;
import com.inved.realestatemanager.view.PropertyListViewHolder;

import java.util.List;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;


public class ListPropertyFragment extends Fragment implements PropertyListViewHolder.PropertyListInterface, SearchFullScreenDialog.OnClickSearchInterface {

    // FOR DESIGN

    private PropertyListAdapter adapter;
    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    private FloatingActionButton openSearchButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adapter = new PropertyListAdapter(context, this);
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

        openSearchButton = mView.findViewById(R.id.list_property_search_open_floating_button);
        RecyclerView recyclerView = mView.findViewById(R.id.fragment_list_property_recycler_view);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if (getActivity() != null) {

            ((MainActivity) getActivity()).setFragmentRefreshListener(this::getAllProperties);
        }

        startSearchProperty();

        return mView;
    }

    private void startSearchProperty() {

        openSearchButton.setOnClickListener(v -> {

            // Create an instance of the dialog fragment and show it
            SearchFullScreenDialog dialog = new SearchFullScreenDialog();

            dialog.setCallback(this::updatePropertyList);

            if (getFragmentManager() != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "FullscreenDialogFragment");
            }
        });

    }


    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        getAllProperties();
    }

    // 3 - Get all properties for a real estate agent
    private void getAllProperties() {
        this.propertyViewModel.getAllProperties().observe(this, this::updatePropertyList);
    }

    // 6 - Update the list of properties
    private void updatePropertyList(List<Property> properties) {
        if (!properties.isEmpty()) {

            this.adapter.updateData(properties);
        }
    }

    @Override
    public void clickOnCardView(long propertyId) {

        if (getActivity() != null) {

            //We open activity if we are in portrait mode
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(PROPERTY_ID, propertyId);
            startActivity(intent);

            //Here we open fragment in landscape mode
            Fragment detailFragment = new DetailPropertyFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(PROPERTY_ID, propertyId);
            detailFragment.setArguments(bundle);

        }

    }

    @Override
    public void searchButton(List<Property> properties) {
        updatePropertyList(properties);
    }
}
