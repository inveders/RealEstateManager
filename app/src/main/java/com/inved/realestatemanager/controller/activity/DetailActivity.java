package com.inved.realestatemanager.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;

import butterknife.OnClick;

import static com.inved.realestatemanager.property.PropertyListViewHolder.PROPERTY_ID;


public class DetailActivity extends BaseActivity  {

    public static final String PROPERTY_ID_INTENT = "PROPERTY_ID_INTENT";
    private Toolbar toolbar;
    private long propertyId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.configureAndShowDetailFragment();

        propertyId=getIntent().getLongExtra(PROPERTY_ID,0);


    }

    private void configureToolbar() {

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }

    // Declare detail fragment
    private DetailPropertyFragment detailPropertyFragment;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_detail;
    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailPropertyFragment = (DetailPropertyFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);

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

    @OnClick(R.id.menu_action_update)
    public void onClickUpdateButton() {
        // 7 - Create item after user clicked on button
        // GO ON THE UPDATE PROPERTY ACTIVITY
        startCreatePropertyActivity();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem addProperty = menu.findItem(R.id.menu_action_add);
        addProperty.setVisible(false);


        final MenuItem updateProperty = menu.findItem(R.id.menu_action_update);
        updateProperty.setOnMenuItemClickListener(menuItem -> {
            startCreatePropertyActivity();

            return true;
        });

        final MenuItem searchProperty = menu.findItem(R.id.menu_action_search);
        searchProperty.setVisible(false);

        return true;
    }

    private void startCreatePropertyActivity() {

        //We send data to the CreateProeprtyActivity
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        intent.putExtra(PROPERTY_ID_INTENT,propertyId);
        startActivity(intent);
    }


}
