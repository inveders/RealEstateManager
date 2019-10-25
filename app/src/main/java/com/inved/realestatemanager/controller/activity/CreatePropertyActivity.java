package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;

public class CreatePropertyActivity extends BaseActivity {

  //  @BindView(R.id.create_property_activity_edit_text)
    EditText editText;
  //  Toolbar toolbar;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_update_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  this.toolbar = findViewById(R.id.toolbar);
        this.configureToolBar();

       // setSupportActionBar(toolbar);

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

    // Create a new property A TERMINER AVEC L'ENSEMBLE DES DONNES CONSTITUANTS LA CREATION D'UN BIEN
    private void createProperty(){
        //   Property property = new Property(this.editText.getText().toString(), this.spinner.getSelectedItemPosition(), REAL_ESTATE_AGENT_ID);
        this.editText.setText("");
        //   this.propertyViewModel.createProperty(property);
    }
}
