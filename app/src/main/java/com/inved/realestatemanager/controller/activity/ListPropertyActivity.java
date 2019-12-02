package com.inved.realestatemanager.controller.activity;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;
import com.inved.realestatemanager.controller.fragment.ListPropertyFragment;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;

import butterknife.OnClick;

public class ListPropertyActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


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

    //Declaration for Navigation Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private PropertyViewModel propertyViewModel;

    // Declare fragments
    private ListPropertyFragment listPropertyFragment;
    private DetailPropertyFragment detailPropertyFragment;
    private FragmentRefreshListener fragmentRefreshListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.configureViewModel();
        this.configureToolbarAndNavigationDrawer();
        //NavigationDrawer
        this.configureDrawerLayout();
        this.configureNavigationView();


        this.configureAndShowListFragment();
        // this.configureAndShowDetailFragment();

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_list_property;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);


    }

    @OnClick(R.id.menu_action_add)
    public void onClickAddButton() {
        // 7 - Create item after user clicked on button
        //  this.createProperty(); GO ON THE CREATE PROPERTY ACTIVITY
        Intent intent = new Intent(ListPropertyActivity.this, CreatePropertyActivity.class);

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
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
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
                startMapsActivity();
                break;
            case R.id.activity_main_drawer_logout:
                logout();
                break;
            case R.id.activity_main_drawer_delete_account:
                deleteAccount();
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

        final MenuItem addProperty = menu.findItem(R.id.menu_action_add);
        addProperty.setOnMenuItemClickListener(menuItem -> {
            startCreateUpdatePropertyActivity();
            return true;
        });

        final MenuItem updateProperty = menu.findItem(R.id.menu_action_update);
        updateProperty.setVisible(false);

        final MenuItem clearSearch = menu.findItem(R.id.menu_action_clear);
        clearSearch.setVisible(false);
        clearSearch.setOnMenuItemClickListener(menuItem -> {
            menu.findItem(R.id.menu_action_clear).setVisible(false);
            refreshFragment();
            return true;
        });



        return true;
    }

    private void startCreateUpdatePropertyActivity() {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }

    private void logout(){
        AuthUI.getInstance()

                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());

    }

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted() {
        return aVoid -> {
            startMainActivity();
            finish();
        };
    }

    private void deleteAccount(){

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            long realEstateAgentId = Long.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());

            propertyViewModel.getAllPropertiesForOneAgent(realEstateAgentId).observe(this, properties -> {
                if (properties.size() > 0) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setMessage(R.string.agent_management_no_delete_possible)
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.agent_management_ok_choice), (dialog, id) -> dialog.dismiss());

                    android.app.AlertDialog alert = builder.create();
                    alert.show();


                } else {

                    new AlertDialog.Builder(this)
                            .setMessage(R.string.popup_message_confirmation_delete_account)
                            .setPositiveButton(R.string.alert_dialog_yes, (dialogInterface, i) -> {
                                deleteUserFromRommDatabase(realEstateAgentId);
                                deleteUserFromFirebase(realEstateAgentId);
                            })
                            .setNegativeButton(R.string.alert_dialog_no, null)
                            .show();
                }
            });
        }



    }

    private void deleteUserFromFirebase(long realEstateAgentId) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        if(currentUser!=null){
            AuthCredential credential = GoogleAuthProvider
                    .getCredential(currentUser.getEmail(), null);

            // Prompt the user to re-provide their sign-in credentials
            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        AuthUI.getInstance()
                                .delete(getApplicationContext())
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        startMainActivity();
                                        finish();
                                    }
                                });

                        RealEstateAgentHelper.deleteAgent(realEstateAgentId).addOnFailureListener(onFailureListener());

                    });
        }



    }

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    private void deleteUserFromRommDatabase(long realEstateAgentId){
        propertyViewModel.deleteRealEstateAgent(realEstateAgentId);
    }

    // ---------------------------
    // INTENT TO OPEN NEW ACTIVITY
    // ---------------------------

    // Launch Profile Activity
    private void startProfileActivity() {
        Intent intent = new Intent(this, AgentManagementActivity.class);
        startActivity(intent);
    }

    private void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }




    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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

