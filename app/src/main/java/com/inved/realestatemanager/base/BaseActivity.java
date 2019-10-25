package com.inved.realestatemanager.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.CreatePropertyActivity;


public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // --------------------
    // LIFE CYCLE
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutContentViewID());
//        ButterKnife.bind(this); //Configure Butterknife
    }

    protected abstract int getLayoutContentViewID();

    // --------------------
    // UI
    // --------------------

    protected void configToolbar(){
        this.toolbar = findViewById(R.id.toolbar);
        Log.d("debago","in config toolbar Base Activity");
        setSupportActionBar(toolbar);
        setTitle("HOME");
    }



}

