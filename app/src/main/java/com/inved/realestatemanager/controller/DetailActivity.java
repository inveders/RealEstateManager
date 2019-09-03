package com.inved.realestatemanager.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.testimmo.R;
import com.example.testimmo.base.BaseActivity;
import com.example.testimmo.property.DetailPropertyFragment;
import com.inved.realestatemanager.base.BaseActivity;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureAndShowDetailFragment();
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

    private void configureAndShowDetailFragment(){
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
}
