package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentOne;
import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentOne.CreateUpdateInterface;
import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentTwo;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import java.util.ArrayList;

import static com.inved.realestatemanager.controller.activity.DetailActivity.PROPERTY_ID_INTENT;


public class CreatePropertyActivity extends BaseActivity implements CreateUpdateInterface {

    //ViewPager
    ViewPager2 viewPager2;
    private static final int NUM_PAGES = 2;
    ViewPagerFragmentAdapter myAdapter;
    private long propertyId;
    private ArrayList<Fragment> arrayList = new ArrayList<>();


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

        // add Fragments in your ViewPagerFragmentAdapter class
        arrayList.add(new CreateUpdatePropertyFragmentOne());
        arrayList.add(new CreateUpdatePropertyFragmentTwo());

        // set Orientation in your ViewPager2
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setUserInputEnabled(false);// SAMPLE CODE to disable swiping in viewpager2
        viewPager2.setAdapter(myAdapter);


        if (getIntent().getLongExtra(PROPERTY_ID_INTENT, 0) != 0) {

            propertyId = getIntent().getLongExtra(PROPERTY_ID_INTENT, 0);
            //We send values in fragment one of create update activity
            ManageCreateUpdateChoice.saveCreateUpdateChoice(this,propertyId);

        }


    }


    // Configure Toolbar
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.page_name_activity_create_property);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
    public void clickOnNextButton(String typeProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                  String numberBedroomsInProperty, double pricePropertyInDollar, double surfaceAreaProperty,
                                  String streetNumber, String streetName, String zipCode, String townProperty, String country,
                                  String pointOfInterest, String addressProperty, long realEstateAgentId,long propertyId) {

        Fragment fragmentTwo = new CreateUpdatePropertyFragmentTwo();
        Bundle args = new Bundle();

        args.putString("typeProperty", typeProperty);
        args.putString("numberRoomsInProperty", numberRoomsInProperty);
        args.putString("numberBathroomsInProperty", numberBathroomsInProperty);
        args.putString("numberBedroomsInProperty", numberBedroomsInProperty);
        args.putDouble("pricePropertyInDollar", pricePropertyInDollar);
        args.putDouble("surfaceAreaProperty", surfaceAreaProperty);
        args.putString("streetNumber", streetNumber);
        args.putString("streetName", streetName);
        args.putString("zipCode", zipCode);
        args.putString("townProperty", townProperty);
        args.putString("country", country);
        args.putString("pointOfInterest", pointOfInterest);
        args.putString("addressProperty", addressProperty);
        args.putLong("realEstateAgentId", realEstateAgentId);

        fragmentTwo.setArguments(args);
        viewPager2.setCurrentItem(1);

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public class ViewPagerFragmentAdapter extends FragmentStateAdapter {


        ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new CreateUpdatePropertyFragmentOne();
                case 1:
                    return new CreateUpdatePropertyFragmentTwo();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    // CHECKBOX CLICK
    public void onCheckboxClicked(View view) {

        ((CheckBox) view).setOnCheckedChangeListener((compoundButton, b) -> {

        });

    }



}
