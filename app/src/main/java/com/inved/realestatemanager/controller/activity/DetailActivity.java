package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.DetailPropertyFragment;

import butterknife.OnClick;

public class DetailActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolbar();
        this.configureAndShowDetailFragment();
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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem addProperty = menu.findItem(R.id.menu_action_add);
        addProperty.setVisible(false);


        final MenuItem updateProperty = menu.findItem(R.id.menu_action_update);
        updateProperty.setOnMenuItemClickListener(menuItem -> {
            startUpdatePropertyActivity();

            return true;
        });

        final MenuItem searchProperty = menu.findItem(R.id.menu_action_search);
        searchProperty.setVisible(false);

        return true;
    }

    private void startUpdatePropertyActivity() {

    }
}
