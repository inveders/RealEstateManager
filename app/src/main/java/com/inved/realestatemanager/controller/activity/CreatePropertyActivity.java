package com.inved.realestatemanager.controller.activity;

import android.content.res.ColorStateList;
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
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import static com.inved.realestatemanager.controller.activity.DetailActivity.PROPERTY_ID_INTENT;


public class CreatePropertyActivity extends BaseActivity implements CreateUpdatePropertyFragmentOne.CreateUpdateChangePageInterface {

    //ViewPager
    ViewPager2 viewPager2;
    private Toolbar toolbar;
    ViewPagerFragmentAdapter myAdapter;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_update_property;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureToolBar();

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager2 = findViewById(R.id.viewPager2);

        myAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        // set Orientation in your ViewPager2
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setUserInputEnabled(false);// SAMPLE CODE to disable swiping in viewpager2
        viewPager2.setAdapter(myAdapter);


        if (getIntent().getLongExtra(PROPERTY_ID_INTENT, 0) != 0) {

            long propertyId = getIntent().getLongExtra(PROPERTY_ID_INTENT, 0);
            //We send values in fragment one of create update activity
            ManageCreateUpdateChoice.saveCreateUpdateChoice(this, propertyId);

        }


    }


    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar_create_update);
        setSupportActionBar(toolbar);
        //setTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textOnPrimary)));
        if (getSupportActionBar() != null) {
            Log.d("debago","getsupportactionbar is not null");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.page_name_activity_create_property));
        }

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


    // CHECKBOX CLICK
    public void onCheckboxClicked(View view) {

        ((CheckBox) view).setOnCheckedChangeListener((compoundButton, b) -> {

        });

    }


}
