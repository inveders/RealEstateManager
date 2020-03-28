package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.ViewPagerFragmentAdapter;
import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentOne;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;

import static com.inved.realestatemanager.controller.activity.DetailActivity.PROPERTY_ID_INTENT;


public class CreatePropertyActivity extends BaseActivity implements CreateUpdatePropertyFragmentOne.CreateUpdateChangePageInterface {

    //ViewPager
    ViewPager2 viewPager2;
    ViewPagerFragmentAdapter myAdapter;

    // --------------------
    // LIFE CYCLE
    // --------------------


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureToolBar();
        this.configureViewPager();

        if (getIntent().getStringExtra(PROPERTY_ID_INTENT) != null) {

            //If different of null, we retrieve the property Id to update the item
            String propertyId = getIntent().getStringExtra(PROPERTY_ID_INTENT);
            //We send values in fragment one for create update activity
            ManageCreateUpdateChoice.saveCreateUpdateChoice(this, propertyId);

        }

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_update_property;
    }

    // --------------------
    // TOOLBAR
    // --------------------

    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_create_update);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (getIntent().getStringExtra(PROPERTY_ID_INTENT) != null) {
                getSupportActionBar().setTitle(getString(R.string.page_name_activity_update_property));
            }else{
                getSupportActionBar().setTitle(getString(R.string.page_name_activity_create_property));
            }

        }

    }

    // --------------------
    // VIEW PAGER
    // --------------------

    private void configureViewPager(){
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager2 = findViewById(R.id.viewPager2);
        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        // set Orientation in your ViewPager2
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setUserInputEnabled(false);// SAMPLE CODE to disable swiping in viewpager2
        viewPager2.setAdapter(myAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    @Override
    public void changeFragmentPage() {

        viewPager2.setCurrentItem(1);
    }

    // --------------------
    // CHECKBOX CLICK
    // --------------------

    public void onCheckboxClicked(View view) {

        ((CheckBox) view).setOnCheckedChangeListener((compoundButton, b) -> {

        });

    }


}
