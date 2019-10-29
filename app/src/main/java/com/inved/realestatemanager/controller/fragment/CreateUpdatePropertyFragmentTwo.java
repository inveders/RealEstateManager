package com.inved.realestatemanager.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

public class CreateUpdatePropertyFragmentTwo extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 546 ;
    private PropertyViewModel propertyViewModel;
    private TextView dateOfEntry;
    private TextView agentName;
    private EditText fullDescriptionEditText;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private ImageView photo5;
    private TextView photoDescription;

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

    //VARIABLES FROM CALLBACK
    private String typeProperty;
    private String numberRoomsInProperty;
    private String numberBathroomsInProperty;
    private  String numberBedroomsInProperty;
    private double pricePropertyInDollar;
    private double surfaceAreaProperty;
    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String pointOfInterest;
    private String addressProeprty;
    private int realEstateAgentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_two, container, false);

        dateOfEntry = v.findViewById(R.id.activity_create_update_property_date_entry_text);
        agentName = v.findViewById(R.id.create_update_textview_real_estate_agent_text);

        photo1 = v.findViewById(R.id.activity_create_update_added_photo_one);
        photo2 = v.findViewById(R.id.activity_create_update_added_photo_two);
        photo3 = v.findViewById(R.id.activity_create_update_added_photo_three);
        photo4 = v.findViewById(R.id.activity_create_update_added_photo_four);
        photo5 = v.findViewById(R.id.activity_create_update_added_photo_five);
        photoDescription = v.findViewById(R.id.activity_create_update_property_added_photo_description);
        fullDescriptionEditText = v.findViewById(R.id.activity_create_update_property_full_description_text);


        Button confirmButton = v.findViewById(R.id.create_update_confirm_button);
        confirmButton.setOnClickListener(view -> finishToCreateProperty());

        Button addPhotoButton = v.findViewById(R.id.activity_create_update_add_photo_button);
        addPhotoButton.setOnClickListener(view -> pickFromGallery());

        this.configureViewModel();
        this.retriveRealEstateAgents();
        this.datePickerInit();
        this.updateUI();

        return v;
    }

    private void takePicture() {


    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    photo1.setImageURI(selectedImage);
                    photoUri1=selectedImage.toString();
                    break;
            }
    }

        private void retriveRealEstateAgents() {
        propertyViewModel.getRealEstateAgent().observe(this,realEstateAgents -> {
            String firstname=realEstateAgents.getFirstname();
            String lastname=realEstateAgents.getLastname();
            agentName.setText(getString(R.string.detail_property_real_estate_agent_text,firstname,lastname));
        });
    }

    private void datePickerInit() {
        dateOfEntry.setText(getString(R.string.create_update_choose_date));
        dateOfEntry.setOnClickListener(v -> showDatePickerDialog());

    }

    public void showDatePickerDialog() {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "DatePicker");

    }



    private void finishToCreateProperty() {

        Bundle args = getArguments();
        if (args != null) {

            typeProperty=args.getString("typeProperty");
            numberRoomsInProperty=args.getString("numberRoomsInProperty");
            numberBathroomsInProperty=args.getString("numberBathroomsInProperty");
            numberBedroomsInProperty=args.getString("numberBedroomsInProperty");
            pricePropertyInDollar=args.getDouble("pricePropertyInDollar",0.0);
            surfaceAreaProperty=args.getDouble("surfaceAreaProperty",0.0);
            streetNumber=args.getString("streetNumber");
            streetName=args.getString("streetName");
            zipCode=args.getString("zipCode");
            townProperty=args.getString("townProperty");
            country=args.getString("country");
            pointOfInterest=args.getString("pointOfInterest");
            addressProeprty=args.getString("addressProperty");
            realEstateAgentId=args.getInt("realEstateAgentId",1);

        }



        String statusProperty = "in progress";
        String dateOfEntryOnMarketForProperty = dateOfEntry.getText().toString();
        String fullDescriptionText = fullDescriptionEditText.getText().toString();
        String dateOfSaleForPorperty=null;
        boolean selected=false;

        Property newProperty = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText,streetNumber, streetName, zipCode, townProperty, country,addressProeprty,pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected,photoUri1,photoUri2,photoUri3,photoUri4,photoUri5,photoDescription1,photoDescription2,
                photoDescription3,photoDescription4,photoDescription5,realEstateAgentId);

        //RealEstateAgents newAgent = new RealEstateAgents(REAL_ESTATE_AGENT_ID, "Alexandra", "Gnimadi", "http://mikoumusique.com");

        this.propertyViewModel.createProperty(newProperty);
      //  this.propertyViewModel.createRealEstateAgent(newAgent);

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
