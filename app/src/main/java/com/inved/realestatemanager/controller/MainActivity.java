package com.inved.realestatemanager.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.CreatePropertyActivity;
import com.inved.realestatemanager.property.DetailPropertyFragment;
import com.inved.realestatemanager.property.ListPropertyFragment;
import com.inved.realestatemanager.property.PropertyAdapter;
import com.inved.realestatemanager.property.PropertyViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements PropertyAdapter.Listener {

    /**
     * Nous avons également créé différentes méthodes privées (3) appelant les méthodes publiques de notre ViewModel
     * afin d'observer leur résultat. Pour les méthodes de type Get, nous avons utilisé la méthode  observe()  pour
     * être alerté automatiquement si le résultat en base de données change... :)
     *
     * Nous avons également utilisé les lambdas pour réduire notre expression et appeler la méthode  updateHeader()
     * quand un changement se produit.
     *
     * On peut aussi enlever le bouton de suppression de notre recycler view et tout le code relatif à cela
     * */


    // FOR DESIGN
    @BindView(R.id.fragment_list_property_recycler_view)
    RecyclerView recyclerView;
   /* @BindView(R.id.main_activity_spinner)
    Spinner spinner;*/

    //Declaration for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //MenuItem logout; IN CASE WE NEED TO LOGOUT

    // 1 - FOR DATA
    private PropertyViewModel propertyViewModel;
    private PropertyAdapter adapter;
    private static int REAL_ESTATE_AGENT_ID = 1;

    // Declare fragments
    private ListPropertyFragment listPropertyFragment;
    private DetailPropertyFragment detailPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //NavigationDrawer
        //  logout=findViewById(R.id.activity_main_drawer_logout); IN CASE WE NEED TO LOGOUT
        this.configureDrawerLayout();

        // Configure RecyclerView & ViewModel
        this.configureRecyclerView();
        this.configureViewModel();

        // Get current real estate agent & properties from Database
        this.getCurrentAgent(REAL_ESTATE_AGENT_ID);
        this.getProperties(REAL_ESTATE_AGENT_ID); //CEPENDANT ON VEUT AVOIR TOUS LES BIENS ET PAS SEULEMENT CEUX D'UN AGENT EN PARTICULIER

        this.configureAndShowListFragment();
        this.configureAndShowDetailFragment();

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

    @Override
    public void onClickDeleteButton(int position) {
        // 7 - Delete property after user clicked on button
        // this.deleteProperty(this.adapter.getProperty(position)); NO NEED IN THIS APP
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
      /***///  this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }

    // ---

    // 3 - Get Current Agent
    private void getCurrentAgent(int realEstateAgentId){
        this.propertyViewModel.getRealEstateAgent(realEstateAgentId);
    }

    // ---

    // 3 - Get all properties for a real estate agent
    private void getProperties(int realEstateAgentId){
        this.propertyViewModel.getProperties(realEstateAgentId).observe(this, this::updatePropertyList);
    }


    // 3 - Update an property (selected or not)
    private void updateProperty(Property property){
        property.setSelected(!property.isSelected());
        this.propertyViewModel.updateProperty(property);
    }

    // -------------------
    // UI
    // -------------------

    // 4 - Configure RecyclerView
    private void configureRecyclerView(){
        this.adapter = new PropertyAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //   ItemClickSupport.addTo(recyclerView, R.layout.activity_todo_list_item)
        //         .setOnItemClickListener((recyclerView1, position, v) -> this.updateProperty(this.adapter.getProperty(position)));
    }


    // 6 - Update the list of properties
    private void updatePropertyList(List<Property> properties){
        this.adapter.updateData(properties);
    }


    // ---------------------------
    // NAVIGATION DRAWER & TOOLBAR
    // ---------------------------

    // Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // userInformationFromFirebase();



    }



    //Navigation drawer
  //  @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_profile :startProfileActivity();
                break;
            case R.id.activity_main_drawer_map: this.startMapsActivity();
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

    // Configure Toolbar
    private void configureToolBar(){
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
    }

    // ---------------------------
    // INTENT TO OPEN NEW ACTIVITY
    // ---------------------------

    // Launch Profile Activity
    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startMapsActivity(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowListFragment(){
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

    private void configureAndShowDetailFragment(){
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

