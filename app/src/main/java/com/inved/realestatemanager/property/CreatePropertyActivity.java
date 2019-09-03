package com.inved.realestatemanager.property;

import android.widget.EditText;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;

public class CreatePropertyActivity extends BaseActivity {

  //  @BindView(R.id.create_property_activity_edit_text)
    EditText editText;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_create_property;
    }

    // Create a new property A TERMINER AVEC L'ENSEMBLE DES DONNES CONSTITUANTS LA CREATION D'UN BIEN
    private void createProperty(){
        //   Property property = new Property(this.editText.getText().toString(), this.spinner.getSelectedItemPosition(), REAL_ESTATE_AGENT_ID);
        this.editText.setText("");
        //   this.propertyViewModel.createProperty(property);
    }
}
