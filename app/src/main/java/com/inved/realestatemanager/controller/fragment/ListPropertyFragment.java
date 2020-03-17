package com.inved.realestatemanager.controller.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.DetailActivity;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.controller.dialogs.SearchFullScreenDialog;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.PropertyListAdapter;
import com.inved.realestatemanager.view.PropertyListViewHolder;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;

@RuntimePermissions
public class ListPropertyFragment extends Fragment implements PropertyListViewHolder.PropertyListInterface, SearchFullScreenDialog.OnClickSearchInterface {

    // FOR DESIGN
    private PropertyListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    private FloatingActionButton openSearchButton;
    private MenuChangementsInterface callback;
    private TextView noPropertyFoundTextview;
    private int queryCount = 0;
    private boolean tabletSize;

    public static final String BOOLEAN_TABLET = "BOOLEAN_TABLET";
    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------


    public ListPropertyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_property, container, false);
        if(getContext()!=null)
        tabletSize= getContext().getResources().getBoolean(R.bool.isTablet);

        noPropertyFoundTextview = mView.findViewById(R.id.fragment_list_property_no_property_found_text);
        openSearchButton = mView.findViewById(R.id.list_property_search_open_floating_button);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mView.findViewById(R.id.fragment_list_property_recycler_view);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Configure ViewModel
        this.configureViewModel();

        if (getActivity() != null) {

            ((ListPropertyActivity) getActivity()).setFragmentRefreshListener(this::getAllProperties);

        }

        startSearchProperty();
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            refreshWithFirebase();
        });

        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adapter = new PropertyListAdapter(context, this);

        this.createCallbackToParentActivity();
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = new ViewModelProvider(this, mViewModelFactory).get(PropertyViewModel.class);

        getAllProperties();

    }

    private void refreshWithFirebase() {

        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots.size() > 0) {

                propertyViewModel.getAllProperties().observe(getViewLifecycleOwner(), properties -> {

                    //We create properties from firebase in room
                    if (queryCount < queryDocumentSnapshots.size()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Property property = documentSnapshot.toObject(Property.class);

                            propertyViewModel.createProperty(PropertyHelper.resetProperties(property));
                            queryCount++;
                        }
                    }

                    new Handler().postDelayed(() -> {
                        mSwipeRefreshLayout.setRefreshing(false);
                        getAllProperties();
                        queryCount = 0;
                    }, 5000);

                });


            } else {
                Toast.makeText(getContext(), this.getString(R.string.no_properties_in_firebase), Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(e -> {
        });
    }

    // --------------
    // SEARCH
    // --------------


    private void startSearchProperty() {

        openSearchButton.setOnClickListener(v -> {

            callback.onMenuChanged(1, null);
            //setHasOptionsMenu(true);
            // Create an instance of the dialog fragment and show it
            SearchFullScreenDialog dialog = new SearchFullScreenDialog();
            dialog.setTargetFragment(this, 1);
            dialog.setCancelable(false);

            //  dialog.setCallback(this::updatePropertyList);

            if (getFragmentManager() != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "FullscreenDialogFragment");
            }
        });

    }

    @Override
    public void searchButton(List<Property> properties) {

        ListPropertyFragmentPermissionsDispatcher.updatePropertyListWithPermissionCheck(this, properties);

    }

    @Override
    public void cancelButton() {
        callback.onMenuChanged(0, null);
    }

    // -------------------
    // DATA
    // -------------------


    // 3 - Get all properties
    private void getAllProperties() {
        this.propertyViewModel.getAllProperties().observe(getViewLifecycleOwner(), properties -> {
            if (getContext() != null) {
                if (properties.size() != 0) {
                    ManageCreateUpdateChoice.saveFirstPropertyIdOnTablet(getContext(), properties.get(0).getPropertyId());

                }
            }
            callback.onMenuChanged(0, null);
            ListPropertyFragmentPermissionsDispatcher.updatePropertyListWithPermissionCheck(this, properties);

        });
    }

    // 6 - Update the list of properties
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void updatePropertyList(List<Property> properties) {
        this.adapter.updateData(properties);
        noPropertyFoundTextview.setVisibility(this.adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if (this.adapter.getItemCount() == 0) {
            openSearchButton.hide();
        } else {
            openSearchButton.show();
        }

    }

    @Override
    public void clickOnCardView(String propertyId) {

        if (getActivity() != null) {


            if (tabletSize) {
                callback.onMenuChanged(0, propertyId);
                //Here we open fragment in landscape mode
                Fragment detailFragment = new DetailPropertyFragment();
                Bundle bundle = new Bundle();
                bundle.putString(PROPERTY_ID, propertyId);
                bundle.putBoolean(BOOLEAN_TABLET, true);
                detailFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_detail_frame_layout, detailFragment, "fragmentDetail")
                        .addToBackStack(null)
                        .commit();


            } else {
                //We open activity if we are in portrait mode
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(PROPERTY_ID, propertyId);
                intent.putExtra(BOOLEAN_TABLET, false);
                startActivity(intent);
            }


        }

    }


    // --------------
    // INTERFACE
    // --------------

    //Interface beween ListPropertyActivity and ListPropertyFragment

    public interface MenuChangementsInterface {
        void onMenuChanged(int number, String propertyId);

    }

    // 3 - Create callback to parent activity
    private void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            callback = (MenuChangementsInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement MenuChamgementsInterface");
        }
    }

    // --------------
    // PERMISSIONS
    // --------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ListPropertyFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
