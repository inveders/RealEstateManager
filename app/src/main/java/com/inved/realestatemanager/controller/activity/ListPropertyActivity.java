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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;
import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;
import com.inved.realestatemanager.sharedpreferences.ManageCurrency;
import com.inved.realestatemanager.sharedpreferences.ManageDatabaseFilling;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ListPropertyActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ListPropertyFragment.MenuChangementsInterface {

    //Declaration for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Menu mOptionsMenu;
    private PropertyViewModel propertyViewModel;
    private FragmentRefreshListener fragmentRefreshListener;

    // --------------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureViewModel();
        this.checkIfSyncWithFirebaseIsNecessary();
        this.configureToolbarAndNavigationDrawer();

        //NavigationDrawer
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.spinnerManagement();

        this.configureAndShowListFragment();
        this.configureAndShowDetailFragment();

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_list_property;
    }

    protected void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = new ViewModelProvider(this, mViewModelFactory).get(PropertyViewModel.class);

    }



    // ---------------------------
    // SYNC WITH FIREFASE
    // ---------------------------

    private void checkIfSyncWithFirebaseIsNecessary() {

        PropertyHelper.getAllProperties().get().addOnSuccessListener(queryDocumentSnapshots -> {

            //We check if database is filling, we don't need to launch this method
            if (!ManageDatabaseFilling.getIsDatabaseFilling(this)) {
                if (queryDocumentSnapshots.size() > 0) {

                    propertyViewModel.getAllProperties().observe(this, properties -> {
                        Log.d("debago", "properties in firebase " + queryDocumentSnapshots.size() + " and properties in room is " + properties.size());

                        if (queryDocumentSnapshots.size() != properties.size()) {

                            //We delete all properties in room if there is a difference between properties number in firebase and in room
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
            }


        }).addOnFailureListener(e -> {
        });
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

    //Spinner to choose currency in navigation drawer
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


    //Navigation drawer selection of elements
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_agent_management:
                startAgentManagementActivity();
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

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    // ---------------------------
    // MENU
    // ---------------------------

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
                    // add icon
                    item.setIcon(R.drawable.ic_menu_add_white_24dp);
                    item.setOnMenuItemClickListener(menuItem -> {
                        ManageCreateUpdateChoice.saveCreateUpdateChoice(this, null);
                        ListPropertyActivityPermissionsDispatcher.startCreateUpdatePropertyActivityWithPermissionCheck(this);
                        return true;
                    });
                    break;
                case 1:
                    //clear icon
                    item.setIcon(R.drawable.ic_menu_clear_white_24dp);
                    item.setOnMenuItemClickListener(menuItem -> {
                        refreshFragment();
                        return true;
                    });
                    break;
                case 2:
                    // edit icon
                    item.setIcon(R.drawable.ic_menu_update_white_24dp);
                    break;
                default:

            }

        }
    }


    // ---------------------------
    // INTENTS TO OPEN NEW ACTIVITY
    // ---------------------------

    @NeedsPermission(Manifest.permission.CAMERA)
    public void startCreateUpdatePropertyActivity() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }


    private void startAgentManagementActivity() {
        Intent intent = new Intent(this, AgentManagementActivity.class);
        startActivity(intent);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    // --------------
    // PERMISSIONS
    // --------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ListPropertyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    // --------------
    // FRAGMENTS AND REFRESH FRAGMENT
    // --------------

    private void configureAndShowListFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        // Declaration for fragments
        ListPropertyFragment listPropertyFragment = (ListPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

        if (listPropertyFragment == null) {
            // B - Create new list property fragment
            listPropertyFragment = new ListPropertyFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_frame_layout, listPropertyFragment)
                    .commit();
        }
    }


    private void configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        DetailPropertyFragment detailPropertyFragment = (DetailPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (detailPropertyFragment == null && findViewById(R.id.activity_detail_frame_layout) != null) {
            detailPropertyFragment = new DetailPropertyFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_frame_layout, detailPropertyFragment)
                    .commit();
        }
    }



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

