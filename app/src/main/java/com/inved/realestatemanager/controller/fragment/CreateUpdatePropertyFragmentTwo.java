package com.inved.realestatemanager.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.controller.dialogs.DatePickerFragment;
import com.inved.realestatemanager.domain.GetSpinner;
import com.inved.realestatemanager.domain.RandomString;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.firebase.PropertyHelper;
import com.inved.realestatemanager.firebase.StorageHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.CreateUpdatePropertyViewModel;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageCreateUpdateChoice;
import com.inved.realestatemanager.sharedpreferences.ManagePhotoNumberCreateUpdate;
import com.inved.realestatemanager.utils.FileCompressor;
import com.inved.realestatemanager.utils.ImageCameraOrGallery;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreateUpdatePropertyFragmentTwo extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CAMERA_PHOTO = 456;
    private static final int REQUEST_GALLERY_PHOTO = 455;
    private StorageHelper storageHelper = new StorageHelper();

    private String cameraFilePath;

    private GetSpinner getSpinner = new GetSpinner();

    private TextWatcher textWatcher;
    private PropertyViewModel propertyViewModel;
    private TextView dateOfEntry;
    private Spinner agentNameSpinner;
    private EditText fullDescriptionEditText;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private ImageView photo5;

    private String photoUri1 = null;
    private String photoUri2 = null;
    private String photoUri3 = null;
    private String photoUri4 = null;
    private String photoUri5 = null;
    private String photoDescription = null;
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
    private double pricePropertyInEuro;
    private double surfaceAreaProperty;
    private String streetNumber;
    private String streetName;
    private String zipCode;
    private String townProperty;
    private String country;
    private String pointOfInterest;
    private String addressCompl;
    private String realEstateAgentId;
    private String propertyId;
    private String propertyIdCreate;
    private String fullDescriptionText;
    private String dateOfEntryOnMarketForProperty;
    private String statusProperty;

    private Context context;
    private String selectedAgent;
    private FileCompressor mCompressor;
    private File mPhotoFile;

    //Dialog stuff
    private Dialog DialogImage;
    private Button saveButton, clearButton;
    private ImageView closeButton;
    private ImageView dialogPhotoImageView;
    private EditText dialogEditText;
    private ImageCameraOrGallery imageCameraOrGallery;
    private RandomString randomString = new RandomString();

    private List<String> spinnerAgentList = new ArrayList<>();

    private static final int REQUEST_CODE_DATE_PICKER = 11; // Used to identify the result

    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_two, container, false);

        imageCameraOrGallery = new ImageCameraOrGallery();
        dateOfEntry = v.findViewById(R.id.activity_create_update_property_date_entry_text);
        agentNameSpinner = v.findViewById(R.id.activity_create_update_spinner_real_estate_agent_text);
        //Spinner step 1/4 Initialize spinner to be selected
        agentNameSpinner.setOnItemSelectedListener(this);
        agentNameSpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        spinnerAgentList.add(getString(R.string.select_agent));
        photo1 = v.findViewById(R.id.activity_create_update_added_photo_one);
        photo2 = v.findViewById(R.id.activity_create_update_added_photo_two);
        photo3 = v.findViewById(R.id.activity_create_update_added_photo_three);
        photo4 = v.findViewById(R.id.activity_create_update_added_photo_four);
        photo5 = v.findViewById(R.id.activity_create_update_added_photo_five);
        fullDescriptionEditText = v.findViewById(R.id.activity_create_update_property_full_description_text);

        this.textwatcherMethod();


        fullDescriptionEditText.addTextChangedListener(textWatcher);


        Button confirmButton = v.findViewById(R.id.create_update_confirm_button);
        confirmButton.setOnClickListener(view ->

                finishToCreateProperty());

        FloatingActionButton addPhotoButton = v.findViewById(R.id.activity_create_update_add_photo_button);

        addPhotoButton.setOnClickListener(view -> {
                    if (photoUri5 != null) {
                        Toast.makeText(context, getString(R.string.already_five_photo), Toast.LENGTH_SHORT).show();

                    } else {
                        selectImage();
                    }

                }

        );


        this.updateUI();
        this.configureViewModel();
        this.retriveRealEstateAgents();
        this.datePickerInit();


        if (getActivity() != null) {
            context = getActivity();
        } else {
            context = MainApplication.getInstance().getApplicationContext();
        }

        mCompressor = new FileCompressor();

        if (ManageCreateUpdateChoice.getCreateUpdateChoice(context) != null) {
            propertyId = ManageCreateUpdateChoice.getCreateUpdateChoice(context);

            this.updateUIwithDataFromDatabase(propertyId);
            confirmButton.setText(getString(R.string.create_update_confirm_button_update));
        }


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            CreateUpdatePropertyViewModel createUpdatePropertyViewModel = new ViewModelProvider(getActivity()).get(CreateUpdatePropertyViewModel.class);
            createUpdatePropertyViewModel.getMyData().observe(getViewLifecycleOwner(), objects -> {

                this.typeProperty = objects.get(0).toString();
                this.numberRoomsInProperty = objects.get(1).toString();
                this.numberBathroomsInProperty = objects.get(2).toString();
                this.numberBedroomsInProperty = (int) objects.get(3);
                this.pricePropertyInEuro = Double.valueOf(objects.get(4).toString());
                this.surfaceAreaProperty = Double.valueOf(objects.get(5).toString());
                this.streetNumber = objects.get(6).toString();
                this.streetName = objects.get(7).toString();
                this.zipCode = objects.get(8).toString();
                this.townProperty = objects.get(9).toString();
                this.country = objects.get(10).toString();
                this.pointOfInterest = objects.get(11).toString();

                if (objects.get(12) != null) {
                    this.addressCompl = objects.get(12).toString();
                } else {
                    this.addressCompl = null;
                }

                if (objects.get(13) != null) {
                    this.propertyId = objects.get(13).toString();
                }


            });
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ManageCreateUpdateChoice.saveCreateUpdateChoice(MainApplication.getInstance().getApplicationContext(), null);
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = new ViewModelProvider(this, mViewModelFactory).get(PropertyViewModel.class);

    }

    private void reinitializeManageChoice() {
        if (getContext() != null) {
            ManageCreateUpdateChoice.saveCreateUpdateChoice(getContext(), null);
        }
    }


    // --------------
    // TEXTWATCHER
    // --------------
    private void textwatcherMethod() {

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 0) {
                    //put backgroung grey when we remove all text

                    if (fullDescriptionEditText.getText().hashCode() == charSequence.hashCode()) {
                        fullDescriptionEditText.setBackgroundResource(R.drawable.edit_text_design);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable != null && !editable.toString().equalsIgnoreCase("")) {
                    if (fullDescriptionEditText.getText().hashCode() == editable.hashCode()) {
                        String value = editable.toString();
                        fullDescriptionEditText.removeTextChangedListener(this);

                        if (value.length() == 0) {

                            fullDescriptionEditText.setBackgroundResource(R.drawable.edit_text_design);
                        } else {

                            fullDescriptionEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                        }
                        fullDescriptionEditText.addTextChangedListener(this);
                    }
                }


            }
        };
    }

    // --------------
    // CUSTOM DIALOG TO EDIT PHOTO
    // --------------

    private void myCustomDialog(String imageViewLink, int photoNumber) {

        if (getActivity() != null) {
            initDialogImage();

            Glide.with(this)
                    .load(new File(imageViewLink))
                    .apply(RequestOptions.centerCropTransform())
                    .override(240, 240)
                    .fitCenter()
                    .into(dialogPhotoImageView);

            managePhotoDescription(photoNumber);
            ManagePhotoNumberCreateUpdate.saveUpdateStatus(getActivity(), "update");
            ManagePhotoNumberCreateUpdate.savePhotoNumber(getActivity(), photoNumber);

            setClearButton(photoNumber);
            setSaveButton(photoNumber);
            closeButton.setOnClickListener(view -> DialogImage.cancel());

            DialogImage.show();
        }

    }

    private void initDialogImage(){
        if(getActivity()!=null){
            DialogImage = new Dialog(getActivity());
            DialogImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogImage.setContentView(R.layout.custom_dialog_image);
            DialogImage.setTitle("My Custom Dialog");

            closeButton = DialogImage.findViewById(R.id.custom_dialog_close);
            clearButton = DialogImage.findViewById(R.id.button_clear);
            saveButton = DialogImage.findViewById(R.id.button_save);
            dialogPhotoImageView = DialogImage.findViewById(R.id.custom_dialog_photo);
            dialogEditText = DialogImage.findViewById(R.id.custom_dialog_edittext);

            clearButton.setEnabled(true);
            saveButton.setEnabled(true);

            dialogPhotoImageView.setOnClickListener(view -> {
                DialogImage.dismiss();
                selectImage();
            });
        }


    }

    private void managePhotoDescription(int photoNumber){
        if (photoDescription1 == null) {
            photoDescription1 = "";
        }
        if (photoDescription2 == null) {
            photoDescription2 = "";
        }
        if (photoDescription3 == null) {
            photoDescription3 = "";
        }
        if (photoDescription4 == null) {
            photoDescription4 = "";
        }
        if (photoDescription5 == null) {
            photoDescription5 = "";
        }

        switch (photoNumber) {

            case 1:
                dialogEditText.setText(photoDescription1, TextView.BufferType.EDITABLE);
                break;
            case 2:
                dialogEditText.setText(photoDescription2, TextView.BufferType.EDITABLE);
                break;
            case 3:
                dialogEditText.setText(photoDescription3, TextView.BufferType.EDITABLE);
                break;
            case 4:
                dialogEditText.setText(photoDescription4, TextView.BufferType.EDITABLE);
                break;
            case 5:
                dialogEditText.setText(photoDescription5, TextView.BufferType.EDITABLE);
                break;

        }
    }

    private void setSaveButton(int photoNumber){
        if(getActivity()!=null){
            saveButton.setOnClickListener(view -> {

                switch (photoNumber) {

                    case 1:
                        photoDescription1 = dialogEditText.getText().toString();
                        break;
                    case 2:
                        photoDescription2 = dialogEditText.getText().toString();
                        break;
                    case 3:
                        photoDescription3 = dialogEditText.getText().toString();
                        break;
                    case 4:
                        photoDescription4 = dialogEditText.getText().toString();
                        break;
                    case 5:
                        photoDescription5 = dialogEditText.getText().toString();
                        break;

                }

                DialogImage.dismiss();
                ManagePhotoNumberCreateUpdate.savePhotoNumber(getActivity(), 0);
                ManagePhotoNumberCreateUpdate.saveUpdateStatus(getActivity(), "create");
            });
        }

    }

    private void setClearButton(int photoNumber){
        clearButton.setOnClickListener(view -> {
            //we clear image and photodescription in custom dialog
            Glide.with(this)
                    .load(R.drawable.no_image)
                    .apply(RequestOptions.centerCropTransform())
                    .override(240, 240)
                    .fitCenter()
                    .into(dialogPhotoImageView);
            dialogEditText.setText("");
            //we clear photouri and photodescription to update UI in fragment
            switch (photoNumber) {

                case 1:
                    photoDescription1 = null;
                    photoUri1 = null;
                    break;
                case 2:
                    photoDescription2 = null;
                    photoUri2 = null;
                    break;
                case 3:
                    photoDescription3 = null;
                    photoUri3 = null;
                    break;
                case 4:
                    photoDescription4 = null;
                    photoUri4 = null;
                    break;
                case 5:
                    photoDescription5 = null;
                    photoUri5 = null;
                    break;

            }

        });
    }


    // --------------
    // TAKE PICTURE
    // --------------

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
                dispatchTakePictureIntent();

            } else if (items[item].equals(MainApplication.getResourses().getString(R.string.dialog_select_image_choose_from_library))) {
                dispatchGalleryIntent();
            } else if (items[item].equals(MainApplication.getResourses().getString(R.string.dialog_select_image_cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void editImageName(String photoUri) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                    .setView(R.layout.edittext_dialog)
                    .setPositiveButton(getString(R.string.agent_management_ok_choice), (dialog, which) -> {

                        EditText etSujet = ((AlertDialog) dialog).findViewById(R.id.sujet);

                        photoDescription = etSujet.getText().toString();
                        if (photoDescription1 == null) {
                            photoDescription1 = photoDescription;
                        } else if (photoDescription2 == null) {
                            photoDescription2 = photoDescription;
                        } else if (photoDescription3 == null) {
                            photoDescription3 = photoDescription;
                        } else if (photoDescription4 == null) {
                            photoDescription4 = photoDescription;
                        } else if (photoDescription5 == null) {
                            photoDescription5 = photoDescription;
                        }
                        if (photoUri != null) {
                            managePhotoUri(photoUri);
                        }
                        updateUI();


                    })
                    .setNegativeButton(getString(R.string.dialog_select_image_cancel), (dialog, which) -> {
                    })
                    .show();
        }
    }


    /**
     * Capture image from camera
     */


    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity() != null) {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = imageCameraOrGallery.createImageFile();
                    cameraFilePath = imageCameraOrGallery.getCameraFilePath(photoFile);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {


                case REQUEST_GALLERY_PHOTO:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    try {
                        mPhotoFile = mCompressor.compressToFile(new File(imageCameraOrGallery.getRealPathFromUri(selectedImage)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // photo1.setImageURI(selectedImage);
                    String photoUri = imageCameraOrGallery.getRealPathFromUri(selectedImage);

                    editImageName(photoUri);


                    break;
                case REQUEST_CAMERA_PHOTO:
                    try {

                        mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (cameraFilePath != null) {
                        photoUri = cameraFilePath;
                        editImageName(photoUri);

                    }


                    break;
                case REQUEST_CODE_DATE_PICKER:
                    // get date from string
                    String selectedDate = data.getStringExtra("selectedDate");
                    // set the value of the editText
                    dateOfEntry.setText(selectedDate);
                    dateOfEntry.setBackgroundResource(R.drawable.edit_text_design_focused);
                    break;
            }


        }


    }


    private void managePhotoUri(String photoUri) {
        if (getActivity() != null) {
            if (ManagePhotoNumberCreateUpdate.getUpdateStatus(getActivity()).equals("create")) {
                //When we add a new photo
                if (photoUri1 == null) {
                    photoUri1 = photoUri;
                } else if (photoUri2 == null) {
                    photoUri2 = photoUri;
                } else if (photoUri3 == null) {
                    photoUri3 = photoUri;
                } else if (photoUri4 == null) {
                    photoUri4 = photoUri;
                } else if (photoUri5 == null) {
                    photoUri5 = photoUri;
                }
            } else if (ManagePhotoNumberCreateUpdate.getUpdateStatus(getActivity()).equals("update")) {
                int photoNumber = ManagePhotoNumberCreateUpdate.getPhotoNumber(getActivity());
                //When we change existing photo
                if (photoNumber == 1) {
                    photoUri1 = photoUri;
                } else if (photoNumber == 2) {
                    photoUri2 = photoUri;
                } else if (photoNumber == 3) {
                    photoUri3 = photoUri;
                } else if (photoNumber == 4) {
                    photoUri4 = photoUri;
                } else if (photoNumber == 5) {
                    photoUri5 = photoUri;
                }
            }
        }

    }

    private void glideNoImage(ImageView imageView) {
        Glide.with(this)
                .load(R.drawable.no_image)
                .override(240, 240)
                .fitCenter()
                .into(imageView);
    }


    // --------------
    // SPINNER
    // --------------

    //Spinner step 2/4 : implement methods onItemSelected and onNothingSelected
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // An item was selected. You can retrieve the selected item using
        if (adapterView.getId() == R.id.activity_create_update_spinner_real_estate_agent_text) {

            selectedAgent = agentNameSpinner.getSelectedItem().toString();
            if (agentNameSpinner.getSelectedItem().toString().equals(getString(R.string.select_agent))) {
                agentNameSpinner.setBackgroundResource(R.drawable.edit_text_design);
            } else {
                agentNameSpinner.setBackgroundResource(R.drawable.edit_text_design_focused);
            }

        }
    }

    //Spinner step 4/4 : retrieve all agents in database and fill spinner with them
    private void retriveRealEstateAgents() {
        if (propertyViewModel.getAllRealEstateAgents() != null) {
            propertyViewModel.getAllRealEstateAgents().observe(getViewLifecycleOwner(), realEstateAgents -> {
                for (RealEstateAgents list : realEstateAgents) {
                    String firstname = list.getFirstname();
                    String lastname = list.getLastname();
                    String agentFirstnameLastname = firstname + " " + lastname;
                    spinnerAgentList.add(agentFirstnameLastname);
                }

            });

            //I fill agent spinner with firstname and lastname of the database programatically

            if (getActivity() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_spinner_item, spinnerAgentList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                agentNameSpinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    // --------------
    // DATE PICKER
    // --------------

    private void datePickerInit() {
        dateOfEntry.setText(getString(R.string.create_update_choose_date));
        dateOfEntry.setBackgroundResource(R.drawable.edit_text_design);
        dateOfEntry.setOnClickListener(v -> showDatePickerDialog());

    }

    private void showDatePickerDialog() {

        if (getActivity() != null) {
            // get fragment manager so we can launch from fragment
            final FragmentManager fm = getActivity().getSupportFragmentManager();
            AppCompatDialogFragment newFragment = new DatePickerFragment();
            // set the targetFragment to receive the results, specifying the request code

            newFragment.setTargetFragment(CreateUpdatePropertyFragmentTwo.this, REQUEST_CODE_DATE_PICKER);
            // show the datePicker
            newFragment.show(fm, "datePicker");
        }

    }

    // --------------
    // CREATE OR UPDATE
    // --------------

    private void actionsAccordingToCreateOrUpdate() {
        if (ManageCreateUpdateChoice.getCreateUpdateChoice(MainApplication.getInstance().getApplicationContext()) != null) {

            //The choice is to update property

            updatePropertyInRoom();
            updatePropertyInFirebase();

            Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_update), Toast.LENGTH_SHORT).show();
            uploadFileChoice(propertyId);
        } else {

            //The choice is to create property

            createPropertyInRoom();
            createPropertyInFirebase();

            Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_creation), Toast.LENGTH_SHORT).show();
            uploadFileChoice(propertyIdCreate);

        }


        reinitializeManageChoice();
        startMainActivity();
    }

    // --------------
    // CREATE PROPERTY
    // --------------

    private void finishToCreateProperty() {

        if (selectedAgent == null || selectedAgent.equals(getString(R.string.select_agent))) {
            Toast.makeText(context, getString(R.string.set_error_agent), Toast.LENGTH_SHORT).show();
        } else if (fullDescriptionEditText.getText().toString().trim().isEmpty()) {
            fullDescriptionEditText.setError(getString(R.string.set_error_street_number));
        } else if (photoUri1 == null) {
            Toast.makeText(context, getString(R.string.set_error_photo), Toast.LENGTH_SHORT).show();

        } else {

            statusProperty = getString(R.string.status_property_in_progress);
            fullDescriptionText = fullDescriptionEditText.getText().toString();

            if (dateOfEntry.getText().toString().trim().equals(getString(R.string.create_update_choose_date))) {
                Utils utils = new Utils();

                dateOfEntry.setText(utils.getTodayDate());
                dateOfEntryOnMarketForProperty = utils.getTodayDate();
            } else {
                dateOfEntryOnMarketForProperty = dateOfEntry.getText().toString();
            }

            propertyIdCreate = randomString.generateRandomString();


            if (selectedAgent != null) {
                SplitString splitString = new SplitString();
                String firstname = splitString.splitStringWithSpace(selectedAgent, 0);
                String lastname = splitString.splitStringWithSpace(selectedAgent, 1);
                propertyViewModel.getRealEstateAgentByName(firstname, lastname).observe(getViewLifecycleOwner(), realEstateAgents -> {
                    realEstateAgentId = realEstateAgents.getRealEstateAgentId();
                    actionsAccordingToCreateOrUpdate();
                });

            } else {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    realEstateAgentId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                } else {
                    realEstateAgentId = null;
                }
                actionsAccordingToCreateOrUpdate();

            }

        }


    }

    private void createPropertyInFirebase() {
        PropertyHelper.createProperty(propertyIdCreate, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);
    }

    private void createPropertyInRoom() {
        Property newProperty = new Property(propertyIdCreate, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

        this.propertyViewModel.createProperty(newProperty);
    }


    // --------------
    // UPDATE PROPERTY
    // --------------

    private void updateUIwithDataFromDatabase(String propertyId) {
        propertyViewModel.getOneProperty(propertyId).observe(getViewLifecycleOwner(), property -> {

            if (property.getDateOfEntryOnMarketForProperty().isEmpty() || property.getDateOfEntryOnMarketForProperty() == null) {
                datePickerInit();
            } else {
                dateOfEntry.setText(property.getDateOfEntryOnMarketForProperty());
                dateOfEntry.setBackgroundResource(R.drawable.edit_text_design_focused);
            }

            //Spinner step 3/4 : retrieve the agents who are already in database and show him in the spinner (pre-fill spinner)
            propertyViewModel.getRealEstateAgentById(property.getRealEstateAgentId()).observe(getViewLifecycleOwner(), realEstateAgents -> {
                if (realEstateAgents.getFirstname() != null) {
                    String firstname = realEstateAgents.getFirstname();
                    String lastname = realEstateAgents.getLastname();
                    int indexSpinner = getSpinner.getIndexSpinner(agentNameSpinner, firstname + " " + lastname);
                    agentNameSpinner.setSelection(indexSpinner);
                }

            });


            if (!property.getFullDescriptionProperty().isEmpty() || property.getFullDescriptionProperty() != null) {
                fullDescriptionEditText.setText(property.getFullDescriptionProperty());
            }

            photoUri1 = property.getPhotoUri1();
            photoUri2 = property.getPhotoUri2();
            photoUri3 = property.getPhotoUri3();
            photoUri4 = property.getPhotoUri4();
            photoUri5 = property.getPhotoUri5();
            photoDescription1 = property.getPhotoDescription1();
            photoDescription2 = property.getPhotoDescription2();
            photoDescription3 = property.getPhotoDescription3();
            photoDescription4 = property.getPhotoDescription4();
            photoDescription5 = property.getPhotoDescription5();
            updateUI();

        });
    }


    private void updatePropertyInFirebase() {
        PropertyHelper.createProperty(propertyId, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);
    }

    private void updatePropertyInRoom() {
        this.propertyViewModel.updateProperty(typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId, propertyId);

    }

    // --------------
    // FILE
    // --------------

    private void uploadFileChoice(String id) {
        //Update firestore database with good image url and upload image in Firebase Storage
        if (photoUri1 != null)
            storageHelper.uploadFromUri(Uri.parse(photoUri1), id, 1);
        if (photoUri2 != null)
            storageHelper.uploadFromUri(Uri.parse(photoUri2), id, 2);
        if (photoUri3 != null)
            storageHelper.uploadFromUri(Uri.parse(photoUri3), id, 3);
        if (photoUri4 != null)
            storageHelper.uploadFromUri(Uri.parse(photoUri4), id, 4);
        if (photoUri5 != null)
            storageHelper.uploadFromUri(Uri.parse(photoUri5), id, 5);

    }

    // --------------
    // UI
    // --------------

    private void updateUI() {

        showImageWithGlide(photoUri1,photo1,1,240);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            showImageWithGlide(photoUri2,photo2,2,110);
            showImageWithGlide(photoUri3,photo3,3,110);
            showImageWithGlide(photoUri4,photo4,4,110);
            showImageWithGlide(photoUri5,photo5,5,110);
        }else{
            showImageWithGlide(photoUri2,photo2,2,50);
            showImageWithGlide(photoUri3,photo3,3,50);
            showImageWithGlide(photoUri4,photo4,4,50);
            showImageWithGlide(photoUri5,photo5,5,50);
        }

    }

    private void showImageWithGlide(String photoUriGlide,ImageView photoGlide,int photoNumber,int size){

        Uri fileUri;
        if (photoUriGlide != null) {
            fileUri = Uri.parse(photoUriGlide);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .override(size, size)
                        .fitCenter()
                        .into(photoGlide);

                photoGlide.setOnClickListener(view -> myCustomDialog(Uri.parse(photoUriGlide).getPath(), photoNumber));
            }
        } else {
            glideNoImage(photoGlide);

        }
    }

    // --------------
    // INTENTS
    // --------------

    private void startMainActivity() {
        Intent intent = new Intent(getContext(), ListPropertyActivity.class);
        startActivity(intent);
    }


}
