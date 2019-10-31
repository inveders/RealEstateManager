package com.inved.realestatemanager.controller.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.controller.fullscreendialog.DatePickerFragment;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.property.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.inved.realestatemanager.controller.fragment.ListPropertyFragment.REAL_ESTATE_AGENT_ID;

@RuntimePermissions
public class CreateUpdatePropertyFragmentTwo extends Fragment {

    private static final int REQUEST_CAMERA_PHOTO = 456;
    private static final int REQUEST_GALLERY_PHOTO = 455;
    private File mPhotoFile;

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
    private int numberBedroomsInProperty;
    private double pricePropertyInDollar;
    private double surfaceAreaProperty;
    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String pointOfInterest;
    private String addressProeprty;
    private long realEstateAgentId;

    private String cameraFilePath;

    String selectedDate;
    public static final int REQUEST_CODE_DATE_PICKER = 11; // Used to identify the result

    private OnFragmentInteractionListener mListener;

    Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

    public static CreateUpdatePropertyFragmentTwo newInstance() {
        CreateUpdatePropertyFragmentTwo fragment = new CreateUpdatePropertyFragmentTwo();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        long propertyId;

        Button confirmButton = v.findViewById(R.id.create_update_confirm_button);
        confirmButton.setOnClickListener(view -> finishToCreateProperty());

        Button addPhotoButton = v.findViewById(R.id.activity_create_update_add_photo_button);

        addPhotoButton.setOnClickListener(view -> selectImage());

        this.updateUI();
        this.configureViewModel();
        this.retriveRealEstateAgents();
        this.datePickerInit();

        if(ManageCreateUpdateChoice.getCreateUpdateChoice(MainApplication.getInstance().getApplicationContext())!=0){
            propertyId=ManageCreateUpdateChoice.getCreateUpdateChoice(MainApplication.getInstance().getApplicationContext());
            Log.d("debago","prop "+propertyId);
            this.updateUIwithDataFromDatabase(propertyId);
        }


        return v;
    }


    private void updateUIwithDataFromDatabase(long propertyId) {
        propertyViewModel.getOneProperty(propertyId).observe(this,property -> {

            if(property.getDateOfEntryOnMarketForProperty().isEmpty()||property.getDateOfEntryOnMarketForProperty()==null){
                datePickerInit();
            }else{
                dateOfEntry.setText(property.getDateOfEntryOnMarketForProperty());
            }

            if(!property.getFullDescriptionProperty().isEmpty()||property.getFullDescriptionProperty()!=null){
                fullDescriptionEditText.setText(property.getFullDescriptionProperty());
            }

            photoUri1=property.getPhotoUri1();
            photoUri2=property.getPhotoUri2();
            photoUri3=property.getPhotoUri3();
            photoUri4=property.getPhotoUri4();
            photoUri5=property.getPhotoUri5();
            updateUI();

        });
    }

    //TAKE A PICTURE

    /**
     * Alert dialog for capture or select from galley
     */

    private void selectImage() {
        final CharSequence[] items = {
                MainApplication.getResourses().getString(R.string.dialog_select_image_take_photo), MainApplication.getResourses().getString(R.string.dialog_select_image_choose_from_library),
                MainApplication.getResourses().getString(R.string.dialog_select_image_cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(MainApplication.getInstance().getResources().getString(R.string.dialog_select_image_take_photo))) {
                CreateUpdatePropertyFragmentTwoPermissionsDispatcher.dispatchTakePictureIntentWithPermissionCheck(this);
                dispatchTakePictureIntent();
            } else if (items[item].equals(MainApplication.getResourses().getString(R.string.dialog_select_image_choose_from_library))) {
                dispatchGalleryIntent();
            } else if (items[item].equals(MainApplication.getResourses().getString(R.string.dialog_select_image_cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    /**
     * Capture image from camera
     */

    @NeedsPermission(Manifest.permission.CAMERA)
    void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(getActivity()!=null){
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(MainApplication.getInstance().getApplicationContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

                    mPhotoFile = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA_PHOTO);
                }
            }
        }

    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);

    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_GALLERY_PHOTO:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    photo1.setImageURI(selectedImage);
                    if (selectedImage != null) {
                        photoUri1=selectedImage.toString();
                    }
                    break;
                case REQUEST_CAMERA_PHOTO:
                    photo1.setImageURI(Uri.parse(cameraFilePath));
                    break;
                case REQUEST_CODE_DATE_PICKER:
                    // get date from string
                    selectedDate = data.getStringExtra("selectedDate");
                    // set the value of the editText
                    dateOfEntry.setText(selectedDate);
                    break;
            }
            updateUI();
        }


    }




    /**
     * Create file with current timestamp name
     *
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        if(getActivity()!=null){
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
                // Save a file: path for using again
                cameraFilePath = "file://" + mFile.getAbsolutePath();
                return mFile;
            }

            return null;
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        CreateUpdatePropertyFragmentTwoPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void retriveRealEstateAgents() {
        if (propertyViewModel.getRealEstateAgent() != null) {
            propertyViewModel.getRealEstateAgent().observe(this, realEstateAgents -> {
                String firstname = realEstateAgents.getFirstname();
                String lastname = realEstateAgents.getLastname();
                agentName.setText(getString(R.string.detail_property_real_estate_agent_text, firstname, lastname));
            });
        }
    }

    private void datePickerInit() {
        dateOfEntry.setText(getString(R.string.create_update_choose_date));
        dateOfEntry.setOnClickListener(v -> showDatePickerDialog());

    }

    public void showDatePickerDialog() {

        if(getActivity()!=null){
            // get fragment manager so we can launch from fragment
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            AppCompatDialogFragment newFragment = new DatePickerFragment();
            // set the targetFragment to receive the results, specifying the request code

            newFragment.setTargetFragment(CreateUpdatePropertyFragmentTwo.this, REQUEST_CODE_DATE_PICKER);
            // show the datePicker
            newFragment.show(fm, "datePicker");
        }

    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ManageCreateUpdateChoice.saveCreateUpdateChoice(MainApplication.getInstance().getApplicationContext(),0);
        //Log.d("debago","6. on detach fragment one "+propertyId);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void finishToCreateProperty() {

        Bundle args = getArguments();
        if (args != null) {

            typeProperty = args.getString("typeProperty");
            numberRoomsInProperty = args.getString("numberRoomsInProperty");
            numberBathroomsInProperty = args.getString("numberBathroomsInProperty");
            numberBedroomsInProperty = args.getInt("numberBedroomsInProperty");
            pricePropertyInDollar = args.getDouble("pricePropertyInDollar", 0.0);
            surfaceAreaProperty = args.getDouble("surfaceAreaProperty", 0.0);
            streetNumber = args.getString("streetNumber");
            streetName = args.getString("streetName");
            zipCode = args.getString("zipCode");
            townProperty = args.getString("townProperty");
            country = args.getString("country");
            pointOfInterest = args.getString("pointOfInterest");
            addressProeprty = args.getString("addressProperty");
            realEstateAgentId = args.getLong("realEstateAgentId", 2);

        }


        String statusProperty = "in progress";
        String dateOfEntryOnMarketForProperty = dateOfEntry.getText().toString();
        String fullDescriptionText = fullDescriptionEditText.getText().toString();
        String dateOfSaleForPorperty = null;
        boolean selected = false;

        Property newProperty = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressProeprty, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);



        if(ManageCreateUpdateChoice.getCreateUpdateChoice(MainApplication.getInstance().getApplicationContext())!=0){
            this.propertyViewModel.updateProperty(newProperty);
            Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_update), Toast.LENGTH_SHORT).show();
        }else{
            this.propertyViewModel.createProperty(newProperty);
            Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_creation), Toast.LENGTH_SHORT).show();
        }

        startMainActivity();

    }

    private void updateUI() {
        String urlNoImage = "https://semantic-ui.com/images/wireframe/image.png";
        if (photoUri1 != null) {
            Glide.with(this)
                    .load(new File(photoUri1))
                    .into(photo1);
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo1);
        }

        if (photoUri2 != null) {
            Glide.with(this)
                    .load(new File(photoUri2))
                    .into(photo2);
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo2);
        }

        if (photoUri3 != null) {
            Glide.with(this)
                    .load(new File(photoUri3))
                    .into(photo3);
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo3);
        }

        if (photoUri4 != null) {
            Glide.with(this)
                    .load(new File(photoUri4))
                    .into(photo4);
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .into(photo4);
        }

        if (photoUri5 != null) {
            Glide.with(this)
                    .load(new File(photoUri5))
                    .into(photo5);
        } else {
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
