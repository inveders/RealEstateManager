package com.inved.realestatemanager.controller.activity;


import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;
import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.firebase.StorageDownload;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;
import com.inved.realestatemanager.utils.ManageCurrency;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ListPropertyActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ListPropertyFragment.MenuChangementsInterface {
    

    //Declaration for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    // Declare fragments
    private ListPropertyFragment listPropertyFragment;
    private DetailPropertyFragment detailPropertyFragment;
    private FragmentRefreshListener fragmentRefreshListener;
    private NavigationView navigationView;

    private Menu mOptionsMenu;
    private PropertyViewModel propertyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkIfUserExistInFirebase();
        this.configureViewModel();
        this.checkIfSyncWithFirebaseIsNecessary();
        this.configureToolbarAndNavigationDrawer();
        //NavigationDrawer
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.spinnerManagement();
        this.configureAndShowListFragment();
        // this.configureAndShowDetailFragment();

    }

    private void spinnerManagement() {

        Spinner spinnerCurrency = (Spinner) navigationView.getMenu().findItem(R.id.activity_main_drawer_currency).getActionView();
        GetSpinner getSpinner = new GetSpinner();
        spinnerCurrency.setSelection(getSpinner.getIndexSpinner(spinnerCurrency, ManageCurrency.getCurrency(ListPropertyActivity.this)));
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = spinnerCurrency.getSelectedItem().toString();
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
                ManageCurrency.saveCurrency(ListPropertyActivity.this, result);
                refreshFragment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void checkIfSyncWithFirebaseIsNecessary() {

        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {

            if (queryDocumentSnapshots.size() > 0) {

                propertyViewModel.getAllProperties().observe(this, properties -> {
                    Log.d("debago", "properties in firebase " + queryDocumentSnapshots.size() + " and properties in room is " + properties.size());

                    if (queryDocumentSnapshots.size() != properties.size()) {

                        //I delete all properties in room
                        if (properties.size() != 0) {
                            for (int i = 0; i < properties.size(); i++) {
                                propertyViewModel.deleteProperty(properties.get(i).getPropertyId());
                            }
                        }


                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Property property = documentSnapshot.toObject(Property.class);

                            String propertyId = property.getPropertyId();
                            String typeProperty = property.getTypeProperty();
                            double pricePropertyInEuro = property.getPricePropertyInEuro();
                            double surfaceAreaProperty = property.getSurfaceAreaProperty();
                            String numberRoomsInProperty = property.getNumberRoomsInProperty();
                            String numberBathroomsInProperty = property.getNumberBathroomsInProperty();
                            int numberBedroomsInProperty = property.getNumberBedroomsInProperty();
                            String fullDescriptionProperty = property.getFullDescriptionProperty();

                            String streetNumber = property.getStreetNumber();
                            String streetName = property.getStreetName();
                            String zipCode = property.getZipCode();
                            String townProperty = property.getTownProperty();
                            String country = property.getCountry();
                            String addressCompl = property.getAddressCompl();
                            String pointOfInterest = property.getPointOfInterest();
                            String statusProperty = property.getStatusProperty();
                            String dateOfEntryOnMarketForProperty = property.getDateOfEntryOnMarketForProperty();
                            String dateOfSaleForProperty = property.getDateOfSaleForProperty();
                            boolean selected = property.isSelected();
                            String photoUri1 = property.getPhotoUri1();
                            String photoUri2 = property.getPhotoUri2();
                            String photoUri3 = property.getPhotoUri3();
                            String photoUri4 = property.getPhotoUri4();
                            String photoUri5 = property.getPhotoUri5();
                            String photoDescription1 = property.getPhotoDescription1();
                            String photoDescription2 = property.getPhotoDescription2();
                            String photoDescription3 = property.getPhotoDescription3();
                            String photoDescription4 = property.getPhotoDescription4();
                            String photoDescription5 = property.getPhotoDescription5();
                            String realEstateAgentId = property.getRealEstateAgentId();

                            StorageDownload storageDownload = new StorageDownload();

                            if (photoUri1 != null && photoUri1.length() < 30) {
                                String uri1 = storageDownload.beginDownload(photoUri1, propertyId);
                                if (uri1 != null) {
                                    photoUri1 = uri1;
                                }
                            }

                            if (photoUri2 != null && photoUri2.length() < 30) {
                                String uri2 = storageDownload.beginDownload(photoUri2, propertyId);
                                if (uri2 != null) {
                                    photoUri2 = uri2;
                                }
                            }

                            if (photoUri3 != null && photoUri3.length() < 30) {
                                String uri3 = storageDownload.beginDownload(photoUri3, propertyId);
                                if (uri3 != null) {
                                    photoUri3 = uri3;
                                }
                            }

                            if (photoUri4 != null && photoUri4.length() < 30) {
                                String uri4 = storageDownload.beginDownload(photoUri4, propertyId);
                                if (uri4 != null) {
                                    photoUri4 = uri4;
                                }
                            }

                            if (photoUri5 != null && photoUri5.length() < 30) {
                                String uri5 = storageDownload.beginDownload(photoUri5, propertyId);
                                if (uri5 != null) {
                                    photoUri1 = uri5;
                                }
                            }

                            Property newProperty = new Property(propertyId, typeProperty, pricePropertyInEuro,
                                    surfaceAreaProperty, numberRoomsInProperty,
                                    numberBathroomsInProperty, numberBedroomsInProperty,
                                    fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                                    statusProperty, dateOfEntryOnMarketForProperty,
                                    dateOfSaleForProperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                                    photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

                            //Create property in room with data from firebase
                            propertyViewModel.createProperty(newProperty);
                        }


                    }


                });


            } else {

                propertyViewModel.getAllProperties().observe(this, properties -> {
                    Log.d("debago", "NULL properties in firebase " + queryDocumentSnapshots.size() + " and properties in room is " + properties.size());
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

    protected void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);

    }


    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_list_property;
    }


    // ---------------------------
    // NAVIGATION DRAWER & TOOLBAR
    // ---------------------------

    // Configure Toolbar and Navigation Drawer
    private void configureToolbarAndNavigationDrawer() {

        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textOnPrimary)));

    }


    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    // Configure NavigationView
    private void configureNavigationView() {
        navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textOnPrimary)));
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.textOnPrimary)));
        navigationView.setNavigationItemSelectedListener(this);
    }


    //Navigation drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_agent_management:
                startProfileActivity();
                break;
            case R.id.activity_main_drawer_map:
                ListPropertyActivityPermissionsDispatcher.startMapsActivityWithPermissionCheck(this);
                break;
            case R.id.activity_main_drawer_logout:

                this.logoutAlertDialog();
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //Navigation drawer
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        // Save the menu reference
        mOptionsMenu = menu;
        onMenuChanged(0);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onMenuChanged(int number) {

        if (mOptionsMenu != null) {

            MenuItem item = mOptionsMenu.findItem(R.id.menu);

            switch (number) {
                case 0:
                    // code block for add
                    item.setIcon(R.drawable.ic_menu_add_white_24dp);
                    item.setOnMenuItemClickListener(menuItem -> {
                        ManageCreateUpdateChoice.saveCreateUpdateChoice(this, null);
                        ListPropertyActivityPermissionsDispatcher.startCreateUpdatePropertyActivityWithPermissionCheck(this);
                        return true;
                    });
                    break;
                case 1:
                    // code block clear
                    item.setIcon(R.drawable.ic_menu_clear_white_24dp);
                    item.setOnMenuItemClickListener(menuItem -> {
                        refreshFragment();
                        return true;
                    });
                    break;
                case 2:
                    // code block edit
                    item.setIcon(R.drawable.ic_menu_update_white_24dp);
                    break;
                default:
                    // code block
            }

        }
    }


    // ---------------------------
    // INTENT TO OPEN NEW ACTIVITY
    // ---------------------------

    @NeedsPermission(Manifest.permission.CAMERA)
    public void startCreateUpdatePropertyActivity() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }

    // Launch Profile Activity
    private void startProfileActivity() {
        Intent intent = new Intent(this, AgentManagementActivity.class);
        startActivity(intent);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ListPropertyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowListFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        listPropertyFragment = (ListPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

        if (listPropertyFragment == null) {
            // B - Create new list property fragment
            listPropertyFragment = new ListPropertyFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, listPropertyFragment)
                    .commit();
        }
    }


   /* private void configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailPropertyFragment = (DetailPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (detailPropertyFragment == null && findViewById(R.id.activity_detail_frame_layout) != null) {
            detailPropertyFragment = new DetailPropertyFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_frame_layout, detailPropertyFragment)
                    .commit();
        }
    }

*/

    private void refreshFragment() {

        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }
    }

    public interface FragmentRefreshListener {
        void onRefresh();

    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

}

