package com.inved.realestatemanager.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;


public class DetailActivity extends BaseActivity  {

    public static final String PROPERTY_ID_INTENT = "PROPERTY_ID_INTENT";
    private String propertyId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.configureAndShowDetailFragment();

        propertyId=getIntent().getStringExtra(PROPERTY_ID);

    }

    private void configureToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.page_name_activity_detail));
        }

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_detail;
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        // Declare detail fragment
        DetailPropertyFragment detailPropertyFragment = (DetailPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);

        if (detailPropertyFragment == null) {
            // B - Create new detail property fragment
            detailPropertyFragment = new DetailPropertyFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_frame_layout, detailPropertyFragment)
                    .commit();
        }
    }

    // --------------
    // MENU
    // --------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.menu);
        item.setIcon(R.drawable.ic_menu_update_white_24dp);
        item.setOnMenuItemClickListener(menuItem -> {
            startCreatePropertyActivity();

            return true;
        });


        return true;
    }

    private void startCreatePropertyActivity() {

        //We send data to the CreateProeprtyActivity
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        intent.putExtra(PROPERTY_ID_INTENT,propertyId);
        startActivity(intent);
    }


}
