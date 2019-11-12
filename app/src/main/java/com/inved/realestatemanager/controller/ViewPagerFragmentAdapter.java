package com.inved.realestatemanager.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentOne;
import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentTwo;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {


    /**
     * A simple pager adapter that represents 2 ScreenSlidePageFragment objects, in
     * sequence.
     */

    private static final int NUM_PAGES = 2;

    private CreateUpdatePropertyFragmentOne mCreateUpdatePropertyFragmentOne;
    private CreateUpdatePropertyFragmentTwo mCreateUpdatePropertyFragmentTwo;

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.mCreateUpdatePropertyFragmentOne = new CreateUpdatePropertyFragmentOne();
        this.mCreateUpdatePropertyFragmentTwo = new CreateUpdatePropertyFragmentTwo();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return mCreateUpdatePropertyFragmentOne;
            case 1:
                return mCreateUpdatePropertyFragmentTwo;

        }
        return mCreateUpdatePropertyFragmentOne;

    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }

}
