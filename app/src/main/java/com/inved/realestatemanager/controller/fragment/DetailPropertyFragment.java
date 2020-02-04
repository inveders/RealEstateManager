package com.inved.realestatemanager.controller.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.dialogs.DatePickerFragment;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.domain.UnitConversion;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.GeocodingViewModel;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.Utils;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;

@RuntimePermissions
public class DetailPropertyFragment extends Fragment {

    private TextView typeProperty;
    private TextView pricePropertyInDollar;
    private TextView pricePropertyUnit;
    private TextView surfaceAreaProperty;
    private TextView streetNumber;
    private TextView streetName;
    private TextView complAddress;
    private TextView townProperty;
    private TextView zipCode;
    private TextView country;

    private TextView numberRoomsInProperty;
    private TextView numberBathroomsInProperty;
    private TextView numberBedroomsInProperty;
    private TextView fullDescriptionProperty;
    private ImageSwitcher imageSwitcher;
    private ImageButton nextImage;
    private ImageButton prevImage;

    private TextView pointsOfInterestNearProperty;
    private TextView statusProperty;
    private TextView dateOfEntryOnMarketForProperty;
    private TextView dateOfSaleForPorperty;
    private TextView realEstateAgent;

    private TextView imageNameSwitcher;
    private ImageView propertyLocalisationImage;

    private PropertyViewModel propertyViewModel;
    private GeocodingViewModel geocodingViewModel;

    private int imageCount;
    private int imagePosition=0;
    private String myPropertyId;
    private int imageSwitcherNumber=0;
    private ArrayList<String> myImages;
    private ArrayList<String> myDescriptionImage;
    private static final String MAP_API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY;

    private static final int REQUEST_CODE_DATE_PICKER_DETAIL = 12; // Used to identify the result of date picker

    public DetailPropertyFragment() {

    }

    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_detail_property, container, false);

        typeProperty = mView.findViewById(R.id.fragment_detail_property_type_text);
        pricePropertyInDollar = mView.findViewById(R.id.fragment_detail_property_price_text);
        pricePropertyUnit = mView.findViewById(R.id.fragment_detail_property_item_unit_price);
        surfaceAreaProperty = mView.findViewById(R.id.fragment_detail_property_surface_area_text);
        numberRoomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_room_text);
        numberBedroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bedroom_text);
        numberBathroomsInProperty = mView.findViewById(R.id.fragment_detail_property_number_of_bathroom_text);
        fullDescriptionProperty = mView.findViewById(R.id.fragment_detail_property_description_text);
        imageSwitcher = mView.findViewById(R.id.fragment_detail_property_image_switcher);
        imageNameSwitcher=mView.findViewById(R.id.fragment_detail_property_image_name_text);
        nextImage = mView.findViewById(R.id.next_button_arrow);
        prevImage = mView.findViewById(R.id.prev_button_arrow);
        streetNumber = mView.findViewById(R.id.fragment_detail_property_street_number_text);
        streetName = mView.findViewById(R.id.fragment_detail_property_street_name_text);
        complAddress = mView.findViewById(R.id.fragment_detail_property_complement_address_text);
        townProperty = mView.findViewById(R.id.fragment_detail_property_town_text);
        zipCode = mView.findViewById(R.id.fragment_detail_property_zip_code_text);
        country = mView.findViewById(R.id.fragment_detail_property_country_text);
        pointsOfInterestNearProperty = mView.findViewById(R.id.fragment_detail_property_points_of_interest_text);
        statusProperty = mView.findViewById(R.id.fragment_detail_property_status_text);
        dateOfEntryOnMarketForProperty = mView.findViewById(R.id.fragment_detail_property_date_of_entry_on_market_text);
        dateOfSaleForPorperty = mView.findViewById(R.id.fragment_detail_property_date_of_sale_text);
        realEstateAgent = mView.findViewById(R.id.fragment_detail_property_real_estate_agent_text);
        propertyLocalisationImage = mView.findViewById(R.id.fragment_detail_property_location_map);

        myImages = new ArrayList<>();
        myDescriptionImage = new ArrayList<>();

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            myPropertyId = intent.getStringExtra(PROPERTY_ID);
            configureViewModel();
            propertyViewModel.getOneProperty(myPropertyId).observe(this, property -> {
                Log.d("debago","updateUI getActivity: "+imageSwitcherNumber);
                if(imageSwitcherNumber==0){
                    DetailPropertyFragmentPermissionsDispatcher.updateWithPropertyWithPermissionCheck(this,property);
                    getRealEstateAgent(property.getRealEstateAgentId());

                }

            });

            setMapStatic(myPropertyId);
        }


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myPropertyId = bundle.getString(PROPERTY_ID);
            configureViewModel();
            propertyViewModel.getOneProperty(myPropertyId).observe(this, property -> {
                Log.d("debago","updateUI bundle");
                        if(imageSwitcherNumber==0){
                            updateWithProperty(property);
                            getRealEstateAgent(property.getRealEstateAgentId());
                            imageSwitcherNumber++;
                        }

            });
            setMapStatic(myPropertyId);

        }

        dateOfSaleForPorperty.setOnClickListener(view -> showDatePickerDialog());

        return mView;
    }

    // 2 - Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);
        this.geocodingViewModel = ViewModelProviders.of(this).get(GeocodingViewModel.class);
    }


    // --------------
    // DATE PICKER
    // --------------

    private void showDatePickerDialog() {

        if (getActivity() != null) {
            // get fragment manager so we can launch from fragment
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            AppCompatDialogFragment newFragment = new DatePickerFragment();
            // set the targetFragment to receive the results, specifying the request code

            newFragment.setTargetFragment(DetailPropertyFragment.this, REQUEST_CODE_DATE_PICKER_DETAIL);
            // show the datePicker
            newFragment.show(fm, "datePickerDetail");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_DATE_PICKER_DETAIL) {// get date from string
                String selectedDate = data.getStringExtra("selectedDate");
                // set the value of the editText
                dateOfSaleForPorperty.setText(selectedDate);

                String status = "sold";
                // set the value in the database
                propertyViewModel.updateDateOfSaleForProperty(selectedDate,status,myPropertyId);

                //set the value in firebase
                PropertyHelper.updateDateOfSale(selectedDate,myPropertyId);
                PropertyHelper.updateStatusProperty(status,myPropertyId);
                Log.d("debago","in DetailPropertyFragment status is "+status+" and propertyId is: "+myPropertyId);
            }

        }


    }

    // --------------
    // PROPERTY
    // --------------

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void updateWithProperty(Property property) {

        UnitConversion unitConversion = new UnitConversion();

        //TYPE PROPERTY
        textManagement(property.getTypeProperty(),typeProperty);

        //PRICE
        if (property.getPricePropertyInEuro() != 0.0) {
            Utils utils = new Utils();
            String priceValue = utils.getPriceInGoodCurrency(property.getPricePropertyInEuro());
            this.pricePropertyInDollar.setText(priceValue);
            this.pricePropertyUnit.setText(utils.goodCurrencyUnit());

        } else {
            this.pricePropertyInDollar.setText(MainApplication.getResourses().getString(R.string.list_property_no_price_indicated));
            this.pricePropertyUnit.setText("");
        }

        //ADDRESS PROPERTY
        textManagement(property.getStreetNumber(),streetNumber);
        textManagement(property.getStreetName(),streetName);
        textManagement(property.getAddressCompl(),complAddress);
        textManagement(property.getTownProperty(),townProperty);
        textManagement(property.getZipCode(),zipCode);
        textManagement(property.getCountry(),country);

        //SURFACE AREA
        if (property.getSurfaceAreaProperty() != 0.0) {
            this.surfaceAreaProperty.setText(unitConversion.changeDoubleToStringWithThousandSeparator(property.getSurfaceAreaProperty()));
        } else {
            this.surfaceAreaProperty.setText(MainApplication.getResourses().getString(R.string.none));
        }

        //POINT OF INTEREST
        textManagement(property.getPointOfInterest(),pointsOfInterestNearProperty);

        //FULL DESCRIPTION PROPERTY
        textManagement(property.getFullDescriptionProperty(),fullDescriptionProperty);

        //NUMBER OF ROOM, BEDROOM, BATHROOM
        this.numberRoomsInProperty.setText(String.valueOf(property.getNumberRoomsInProperty()));
        this.numberBathroomsInProperty.setText(String.valueOf(property.getNumberBathroomsInProperty()));
        this.numberBedroomsInProperty.setText(String.valueOf(property.getNumberBedroomsInProperty()));

        //STATUS PROPERTY
        textManagement(property.getStatusProperty(),statusProperty);

        //DATE OF ENTRY
        textManagement(property.getDateOfEntryOnMarketForProperty(),dateOfEntryOnMarketForProperty);

        //DATE OF SALE
        if (property.getDateOfSaleForProperty() != null) {
            this.dateOfSaleForPorperty.setText(property.getDateOfSaleForProperty());
        } else {
            this.dateOfSaleForPorperty.setText(MainApplication.getResourses().getString(R.string.date_of_sale_to_define));
        }

        //IMAGE SWITCHER
        imageSwitcher.removeAllViews();
        imageSwitcher.setFactory(() -> {
            // TODO Auto-generated method stub

            // Create a new ImageView and set it's properties
            ImageView imageView = new ImageView(MainApplication.getInstance().getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            return imageView;
        });

        imagesManagement(property.getPhotoUri1(),property.getPhotoDescription1());
        imagesManagement(property.getPhotoUri2(),property.getPhotoDescription2());
        imagesManagement(property.getPhotoUri3(),property.getPhotoDescription3());
        imagesManagement(property.getPhotoUri4(),property.getPhotoDescription4());
        imagesManagement(property.getPhotoUri5(),property.getPhotoDescription5());

        myImagesManagement();

        clickOnImagesArrow();

    }

    private void imagesManagement(String photoUri,String photoDescription){
        if (photoUri != null) {
            myImages.add(photoUri);
            myDescriptionImage.add(photoDescription);
        }

    }

    private void myImagesManagement(){
        if(myImages.size()==1){
            Log.d("debago","in myImagesize equal to 1 "+myImages.size());
            imageSwitcher.setImageURI(Uri.parse(myImages.get(0)));
            imageNameSwitcher.setText(myDescriptionImage.get(0));
            imageCount=myImages.size();
            nextImage.setVisibility(View.INVISIBLE);
            prevImage.setVisibility(View.INVISIBLE);
        } else if (myImages.size() != 0) {
            Log.d("debago","in myImagesize equal different to zero "+myImages.size());
            imageSwitcher.setImageURI(Uri.parse(myImages.get(0)));
            imageNameSwitcher.setText(myDescriptionImage.get(0));
            imageCount=myImages.size();
        }else {
            imageSwitcher.setImageResource(R.mipmap.ic_logo_appli_realestate_round);
            nextImage.setVisibility(View.INVISIBLE);
            prevImage.setVisibility(View.INVISIBLE);
            imageNameSwitcher.setVisibility(View.INVISIBLE);
            imageCount=0;
        }
    }

    private void clickOnImagesArrow(){

        Animation in = AnimationUtils.loadAnimation(MainApplication.getInstance().getApplicationContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(MainApplication.getInstance().getApplicationContext(), android.R.anim.slide_out_right);

        nextImage.setOnClickListener(view -> {

            if(imagePosition+1<imageCount){
                imageSwitcher.setInAnimation(in);
                imageSwitcher.setImageURI(Uri.parse(myImages.get(imagePosition+1)));
                imageNameSwitcher.setText(myDescriptionImage.get(imagePosition+1));
                imagePosition++;
            }else{
                Toast.makeText(getContext(), MainApplication.getResourses().getString(R.string.no_more_photo), Toast.LENGTH_SHORT).show();
            }
        });

        prevImage.setOnClickListener(view -> {

            if(imagePosition-1>=0){
                imageSwitcher.setOutAnimation(out);
                imageSwitcher.setImageURI(Uri.parse(myImages.get(imagePosition-1)));
                imageNameSwitcher.setText(myDescriptionImage.get(imagePosition-1));
                imagePosition--;
            }else{
                Toast.makeText(getContext(), MainApplication.getResourses().getString(R.string.first_photo), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void textManagement(String text, TextView textView){

        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText(MainApplication.getResourses().getString(R.string.none));
        }

    }

    private void setMapStatic(String propertyId) {

        //Construct formatted address from data in room
        propertyViewModel.getOneProperty(propertyId).observe(this, properties -> {

            SplitString splitString = new SplitString();

            String streetNumber = properties.getStreetNumber();
            String streetName = properties.getStreetName();
            String zipCode = properties.getZipCode();
            String town = properties.getTownProperty();
            String country = properties.getCountry();
            String addressToConvert = streetNumber + " " + streetName + " " + zipCode + " " + town + " " + country;
            String addressFormatted = splitString.replaceAllSpacesByAddition(addressToConvert);

            geocodingSearch(addressFormatted);



        });

    }

    //Search latitude and longitude with formatted address, and show static map
    private void geocodingSearch(String addressFormatted){
        geocodingViewModel.getLatLngWithAddress(addressFormatted).observe(this, results -> {

            if (results.size() != 0) {

                double latitude = results.get(0).getGeometry().getLocation().getLat();
                double longitude = results.get(0).getGeometry().getLocation().getLng();

                String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + addressFormatted + "&zoom=16&size=170x100&maptype=roadmap&markers=color:blue%7C" + latitude + "," + longitude + "&key=" + MAP_API_KEY;

                //IMAGE LOCATION
                Glide.with(MainApplication.getInstance().getApplicationContext())
                        .load(url)
                        .into((propertyLocalisationImage));
            } else {
                Log.d("debago", "Geocoding no result ");

            }


        });
    }
    // --------------
    // AGENT
    // --------------

    private void getRealEstateAgent(String realEstateAgentId) {

        propertyViewModel.getRealEstateAgentById(realEstateAgentId).observe(this, realEstateAgents -> {

            if (realEstateAgents.getFirstname() != null && realEstateAgents.getLastname() != null) {
                String completeName = realEstateAgents.getFirstname() + " " + realEstateAgents.getLastname();
                this.realEstateAgent.setText(completeName);
            } else if (realEstateAgents.getFirstname() != null) {
                this.realEstateAgent.setText(realEstateAgents.getFirstname());
            } else if (realEstateAgents.getLastname() != null) {
                this.realEstateAgent.setText(realEstateAgents.getLastname());
            }
        });

    }

    // --------------
    // PERMISSION
    // --------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        DetailPropertyFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
