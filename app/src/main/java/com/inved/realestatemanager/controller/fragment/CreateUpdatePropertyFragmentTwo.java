package com.inved.realestatemanager.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

public class CreateUpdatePropertyFragmentTwo extends Fragment {

    private PropertyViewModel propertyViewModel;
    private EditText dateOfEntry;
    private EditText status;
    private Spinner agentName;

    private ImageView foregroundPhoto;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private ImageView photo5;
    private EditText photoDescription;

    private String photoUri1 = null;
    private String photoUri2 = null;
    private String photoUri3 = null;
    private String photoUri4 = null;
    private String photoUri5 = null;
    private String photoDescription1 = null;
    private String photoDescription2 = null;
    private String photoDescription3 = null;
    private String photoDescription4 = null;
    private String photoDescription5 = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_two, container, false);

        dateOfEntry = v.findViewById(R.id.activity_create_update_property_date_entry_text);
        status = v.findViewById(R.id.activity_create_update_property_status_text);
        agentName = v.findViewById(R.id.create_update_spinner_real_estate_agent_text);

        foregroundPhoto = v.findViewById(R.id.activity_create_update_add_photo);
        photo1 = v.findViewById(R.id.activity_create_update_added_photo_one);
        photo2 = v.findViewById(R.id.activity_create_update_added_photo_two);
        photo3 = v.findViewById(R.id.activity_create_update_added_photo_three);
        photo4 = v.findViewById(R.id.activity_create_update_added_photo_four);
        photo5 = v.findViewById(R.id.activity_create_update_added_photo_five);
        photoDescription = v.findViewById(R.id.activity_create_update_property_added_photo_description);

        Button confirmButton = v.findViewById(R.id.create_update_confirm_button);
        confirmButton.setOnClickListener(view -> finishToCreateProperty());

        this.configureViewModel();
        this.updateUI();

        return v;
    }


    private void finishToCreateProperty() {


        String photoDescription = null;

        String statusProperty = status.getText().toString();
        String dateOfEntryOnMarketForProperty = dateOfEntry.getText().toString();
        int realEstateAgentId = 1; //CHANGE AFTER


        RealEstateAgents newAgent = new RealEstateAgents(REAL_ESTATE_AGENT_ID, "Alexandra", "Gnimadi", "http://mikoumusique.com");

    //    this.propertyViewModel.updateProperty(newProperty);
        this.propertyViewModel.createRealEstateAgent(newAgent);

        Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation), Toast.LENGTH_SHORT).show();
        startMainActivity();

    }

    private void updateUI(){
        String urlNoImage = "https://semantic-ui.com/images/wireframe/image.png";
        if(photoUri1!=null){
            Glide.with(this)
                    .load(new File(photoUri1))
                    .into(photo1);
        }else{
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo1);
        }

        if(photoUri2!=null){
            Glide.with(this)
                    .load(new File(photoUri2))
                    .into(photo2);
        }else{
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo2);
        }

        if(photoUri3!=null){
            Glide.with(this)
                    .load(new File(photoUri3))
                    .into(photo3);
        }else{
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo3);
        }

        if(photoUri4!=null){
            Glide.with(this)
                    .load(new File(photoUri4))
                    .into(photo4);
        }else{
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo4);
        }

        if(photoUri5!=null){
            Glide.with(this)
                    .load(new File(photoUri5))
                    .into(photo5);
        }else{
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo5);
        }
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.propertyViewModel.init(REAL_ESTATE_AGENT_ID);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

}
