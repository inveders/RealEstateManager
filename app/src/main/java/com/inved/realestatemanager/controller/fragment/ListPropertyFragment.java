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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.controller.activity.DetailActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.view.PropertyListAdapter;
import com.inved.realestatemanager.view.PropertyListViewHolder;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.util.List;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;


public class ListPropertyFragment extends Fragment implements PropertyListViewHolder.PropertyListInterface {

    // FOR DESIGN

    public static PropertyListAdapter adapter;
    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    private static long REAL_ESTATE_AGENT_ID = 1;
    private Context context;

    public static ListPropertyFragment newInstance() {
        return new ListPropertyFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        adapter = new PropertyListAdapter(context, this, Glide.with(this));
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

        RecyclerView recyclerView = mView.findViewById(R.id.fragment_list_property_recycler_view);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        if(getActivity()!=null){

            ((MainActivity)getActivity()).setFragmentRefreshListener(this::getAllProperties);
        }


        return mView;
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

        if(getActivity()!=null){

            //We open activity if we are in portrait mode
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(PROPERTY_ID, propertyId);
            startActivity(intent);

            //Here we open fragment in landscape mode
            Fragment detailFragment = new DetailPropertyFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(PROPERTY_ID, propertyId);
            detailFragment.setArguments(bundle);

           /* FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main_frame_layout, detailFragment); // first the id of the frame layout which contains fragment
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();*/
        }



    }

    @Override
    public void onClickDeleteButton(long propertyId) {

    }


}
