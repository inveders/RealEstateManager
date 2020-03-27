package com.inved.realestatemanager.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.DetailActivity;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.controller.dialogs.SearchFullScreenDialog;
import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;
import com.inved.realestatemanager.sharedpreferences.ManageDatabaseFilling;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.view.PropertyListAdapter;
import com.inved.realestatemanager.view.PropertyListViewHolder;

import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;

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
    private Disposable disposable;

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
        if (getContext() != null)
            tabletSize = getContext().getResources().getBoolean(R.bool.isTablet);

        this.fillDatabaseWithFirebaseValues();
        noPropertyFoundTextview = mView.findViewById(R.id.fragment_list_property_no_property_found_text);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mView.findViewById(R.id.fragment_list_property_recycler_view);
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Configure ViewModel
        this.configureViewModel();

        if (getActivity() != null) {

            ((ListPropertyActivity) getActivity()).setFragmentRefreshListener(this::getAllProperties);
            ((ListPropertyActivity) getActivity()).setFragmentRefreshSearchListener(this::updatePropertyList);

        }
        openSearchButton = mView.findViewById(R.id.list_property_search_open_floating_button);
        if (!tabletSize) {
            openSearchButton.show();
            startSearchProperty();
        } else {
            openSearchButton.hide();
        }

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
                mSwipeRefreshLayout.setRefreshing(false);
            }

        }).addOnFailureListener(e -> mSwipeRefreshLayout.setRefreshing(false));
    }


// --------------
// SEARCH
// --------------


    private void startSearchProperty() {

        openSearchButton.setOnClickListener(v -> {

            callback.onMenuChanged(1, null);
            // Create an instance of the dialog fragment and show it

            if (getActivity() != null) {
                SearchFullScreenDialog dialog = new SearchFullScreenDialog();
                dialog.setTargetFragment(this, 1);
                dialog.setCancelable(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialog.show(ft, "FullscreenDialogFragment");
            }


        });

    }

    @Override
    public void searchButton(List<Property> properties) {

        updatePropertyList(properties);

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
                    if (tabletSize) {
                        ManageCreateUpdateChoice.saveFirstPropertyIdOnTablet(getContext(), properties.get(0).getPropertyId());
                    }

                }
            }
            callback.onMenuChanged(0, null);

            updatePropertyList(properties);

        });
    }

    // 6 - Update the list of properties
    private void updatePropertyList(List<Property> properties) {

        this.adapter.updateData(properties);
        //TODO : voir si c'est possible d'implémenter ça dans un ViewModel
        noPropertyFoundTextview.setVisibility(this.adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        if (this.adapter.getItemCount() == 0) {
            openSearchButton.hide();
        } else {
            if (!tabletSize) {
                openSearchButton.show();
            }
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


    // ---------------------------
    // SYNC WITH FIREFASE
    // ---------------------------

    private void fillDatabaseWithFirebaseValues() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            RealEstateAgentHelper.getAgentWhateverAgency(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    if (task.getResult() != null) {
                        if (task.getResult().getDocuments().size() != 0) {
                            //We have this agency in firebase and we have to put these items in a new Room database
                            String agencyPlaceIdToSave = task.getResult().getDocuments().get(0).getString("agencyPlaceId");
                            String agencyNameToSave = task.getResult().getDocuments().get(0).getString("agencyName");
                            ManageAgency.saveAgencyPlaceId(MainApplication.getInstance().getApplicationContext(), agencyPlaceIdToSave);
                            ManageAgency.saveAgencyName(MainApplication.getInstance().getApplicationContext(), agencyNameToSave);
                            if (getContext() != null) {
                                if (!ManageDatabaseFilling.isDatabaseFilled(getContext())) {
                                    launchAsynchroneTask();
                                } else {
                                    this.checkIfSyncWithFirebaseIsNecessary();
                                }
                            }
                        }
                    }
                }

            }).addOnFailureListener(e -> {
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (disposable != null) {
            disposable.dispose();
        }

    }


    private void launchAsynchroneTask() {

        disposable = Completable.create(emitter -> {
            try {
                prepopulateRealEstateAgents();
                emitter.onComplete();
            } catch (Error e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        preopopulateProperties();
                        ManageDatabaseFilling.saveDatabaseFilledState(MainApplication.getInstance().getApplicationContext(), true);
                        getAllProperties();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });


    }

    private void prepopulateRealEstateAgents() {
        RealEstateAgentHelper.getAllAgents().get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots.size() > 0) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    RealEstateAgents realEstateAgents1 = documentSnapshot.toObject(RealEstateAgents.class);

                    RealEstateAgents newAgent = RealEstateAgentHelper.resetAgent(realEstateAgents1);

                    //Create real estate agent in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> RealEstateManagerDatabase.getInstance(MainApplication.getInstance().getApplicationContext()).realEstateAgentsDao().createRealEstateAgent(newAgent));
                }
            }
        }).addOnFailureListener(e -> {
        });

    }

    private void preopopulateProperties() {
        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.size() > 0) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Property property = documentSnapshot.toObject(Property.class);

                    Property newProperty = PropertyHelper.resetProperties(property);

                    //Create property in room with data from firebase
                    Executors.newSingleThreadScheduledExecutor().execute(() -> {
                        RealEstateManagerDatabase.getInstance(MainApplication.getInstance().getApplicationContext()).propertyDao().insertProperty(newProperty);
                        getAllProperties();
                    });

                }

            }

        }).addOnFailureListener(e -> {

        });
    }

    // ---------------------------
    // SYNC WITH FIREFASE
    // ---------------------------

    private void checkIfSyncWithFirebaseIsNecessary() {

        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {

            //We check if database is filling, we don't need to launch this method
            if (queryDocumentSnapshots.size() > 0) {

                propertyViewModel.getAllProperties().observe(getViewLifecycleOwner(), properties -> {
                    if (queryDocumentSnapshots.size() != properties.size()) {

                        //We delete all properties in room if there is a difference between
                        // properties number in firebase and in room
                        if (properties.size() != 0) {
                            for (int i = 0; i < properties.size(); i++) {
                                propertyViewModel.deleteProperty(properties.get(i).getPropertyId());
                            }
                        }
                        //We create properties from firebase in room
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Property property = documentSnapshot.toObject(Property.class);
                            //Create property in room with data from firebase
                            propertyViewModel.createProperty(PropertyHelper.resetProperties(property));
                            getAllProperties();
                        }
                    }
                });
            } else {
                propertyViewModel.getAllProperties().observe(getViewLifecycleOwner(), properties -> {
                    if (properties.size() != 0) {
                        //We delete all properties in Room if there is no property in firebase
                        for (int i = 0; i < properties.size(); i++) {
                            propertyViewModel.deleteProperty(properties.get(i).getPropertyId());
                        }
                    }
                });
            }

        }).addOnFailureListener(e -> {
        });
    }

}
