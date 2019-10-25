package com.inved.realestatemanager.controller;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.activity.CreatePropertyActivity;
import com.inved.realestatemanager.controller.activity.MapsActivity;
import com.inved.realestatemanager.controller.activity.ProfileActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;

import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    /**
     * Nous avons également créé différentes méthodes privées (3) appelant les méthodes publiques de notre ViewModel
     * afin d'observer leur résultat. Pour les méthodes de type Get, nous avons utilisé la méthode  observe()  pour
     * être alerté automatiquement si le résultat en base de données change... :)
     * <p>
     * Nous avons également utilisé les lambdas pour réduire notre expression et appeler la méthode  updateHeader()
     * quand un changement se produit.
     * <p>
     * On peut aussi enlever le bouton de suppression de notre recycler view et tout le code relatif à cela
     */



   /* @BindView(R.id.main_activity_spinner)
    Spinner spinner;*/

    //Declaration for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    //MenuItem logout; IN CASE WE NEED TO LOGOUT

    // Declare fragments
    private ListPropertyFragment listPropertyFragment;
    private DetailPropertyFragment detailPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.configureToolbarAndNavigationDrawer();
        //NavigationDrawer
        //  logout=findViewById(R.id.activity_main_drawer_logout); ONLY IN CASE WE NEED TO LOGOUT
        this.configureDrawerLayout();
        this.configureNavigationView();



        this.configureAndShowListFragment();
        // this.configureAndShowDetailFragment();

    }


    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_main;
    }


    @OnClick(R.id.menu_action_add)
    public void onClickAddButton() {
        // 7 - Create item after user clicked on button
        //  this.createProperty(); GO ON THE CREATE PROPERTY ACTIVITY
        Intent intent = new Intent(MainActivity.this, CreatePropertyActivity.class);

        startActivity(intent);
    }

    @OnClick(R.id.menu_action_update)
    public void onClickUpdateButton() {
        // 7 - Create item after user clicked on button
        // GO ON THE UPDATE PROPERTY ACTIVITY
    }












    // ---------------------------
    // NAVIGATION DRAWER & TOOLBAR
    // ---------------------------

    // Configure Toolbar and Navigation Drawer
    private void configureToolbarAndNavigationDrawer() {

        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

    }


    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // userInformationFromFirebase();


    }

    // Configure NavigationView
    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //Navigation drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_profile:
                startProfileActivity();
                break;
            case R.id.activity_main_drawer_map:
                startCreatePropertyActivity();
                break;
          /*  case R.id.activity_main_drawer_logout:signOutUserFromFirebase();
                break;*/ //IN CASE WE NEED TO LOGOUT BUT NOT NEED HERE
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

        final MenuItem addProperty = menu.findItem(R.id.menu_action_add);
        addProperty.setOnMenuItemClickListener(menuItem -> {
            startCreatePropertyActivity();
            return true;
        });

        final MenuItem updateProperty = menu.findItem(R.id.menu_action_update);
        updateProperty.setOnMenuItemClickListener(menuItem -> {
            startUpdatePropertyActivity();

            return true;
        });

        final MenuItem searchProperty = menu.findItem(R.id.menu_action_search);
        searchProperty.setOnMenuItemClickListener(menuItem -> {
            startSearchProperty();

            return true;
        });


        return true;
    }

    private void startCreatePropertyActivity() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }

    private void startUpdatePropertyActivity() {

    }


    // ---------------------------
    // INTENT TO OPEN NEW ACTIVITY
    // ---------------------------

    // Launch Profile Activity
    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
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



    // -------------------
    // SEARCH IN DATABASE
    // -------------------

    private void startSearchProperty() {

    /*    SearchFullScreenDialog dialog = SearchFullScreenDialog.newInstance();

        if (getFragmentManager() != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            dialog.show(ft, SearchFullScreenDialog.TAG);
        }

        dialog.setCallback((ratingChoosen, openForLunchChoosen, customersNumberChoosen, distanceChoosen) -> {

        });*/


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

    // --------------
    // CallBack
    // --------------

    /* Nécessite implements ListPropertyFragment.OnButtonClickedListener
    NO NEED HERE
    Enfin, nous modifions ( 3 ) notre méthode onButtonClicked( ) afin d'afficher DetailActivity (et son fragment DetailFragment) uniquement lorsque nous sommes sur Smartphone.
    @Override
    public void onButtonClicked(View view) {
        // 3 - Check if detail fragment is not created or if not visible
        if (detailPropertyFragment == null || !detailPropertyFragment.isVisible()){
            startActivity(new Intent(this, DetailActivity.class));
        }
    }*/


}

