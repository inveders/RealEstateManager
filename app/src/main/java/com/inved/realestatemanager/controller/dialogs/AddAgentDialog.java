package com.inved.realestatemanager.controller.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.firebase.StorageHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.utils.FileCompressor;
import com.inved.realestatemanager.utils.GlideApp;
import com.inved.realestatemanager.utils.ImageCameraOrGallery;
import com.inved.realestatemanager.utils.MainApplication;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddAgentDialog extends DialogFragment implements TextWatcher {

    //final values
    private static final String MAP_API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY;
    private static final int REQUEST_CAMERA_PHOTO = 456;
    private static final int REQUEST_GALLERY_PHOTO = 455;
    private static final String TAG = "debago";

    //View Model
    private PropertyViewModel propertyViewModel;

    //Photo
    private String urlPicture;
    private String cameraFilePath;
    private ImageCameraOrGallery imageCameraOrGallery;

    //UI
    private ImageView agentPhoto;
    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private Button addPhotoButton;
    private TextView addActionButton;
    private ImageButton cancelSearchButton;
    private AutocompleteSupportFragment autocompleteFragment;
    private Bundle bundle;
    private String agencyName;
    private String agencyPlaceId;

    private FileCompressor mCompressor;
    private File mPhotoFile;

    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.layout_add_agent_dialog, container, false);

        imageCameraOrGallery = new ImageCameraOrGallery();
        agentPhoto = view.findViewById(R.id.add_agent_photo);
        addPhotoButton = view.findViewById(R.id.add_agent_add_photo_button);
        lastnameEditText = view.findViewById(R.id.add_agent_edittext_lastname);
        firstnameEditText = view.findViewById(R.id.add_agent_edittext_firstname);
        addActionButton = view.findViewById(R.id.add_agent_dialog_add_new_agent);
        cancelSearchButton = view.findViewById(R.id.agent_add_dialog_close);

        //Initialize TextWatcher
        firstnameEditText.addTextChangedListener(this);
        lastnameEditText.addTextChangedListener(this);
        mCompressor = new FileCompressor();
        this.configureViewModel();

        initializeMethods();
        autocompleteAgency();

        return view;
    }

    private void initializeMethods() {

        if (getDialog() != null) {
            getDialog().setTitle(getString(R.string.add_agent_dialog_title));
        }

        //Retrieve data from AgentManagement Activity
        bundle = getArguments();
        if (bundle != null) {
            addActionButton.setText(getString(R.string.add_agent_dialog_edit_text));

            String realEstateAgentIdBundle = bundle.getString("myRealEstateAgentId", null);
            if (realEstateAgentIdBundle != null) {
                retrieveRealEstateAgentData(realEstateAgentIdBundle);
            }

        }

        addPhotoButton.setOnClickListener(v -> selectImage());
        cancelSearchButton.setOnClickListener(v -> getDialog().dismiss());
        addActionButton.setOnClickListener(v -> this.createOrUpdateRealEstateAgent());
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.propertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    @Override
    public void onDestroyView() {

        //To close the autocompletefragment to avoid to duplicate his id
        if (autocompleteFragment != null) {
            getChildFragmentManager().beginTransaction().remove(autocompleteFragment).commitAllowingStateLoss();
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // --------------
    // FRAGMENT AUTOCOMPLETE
    // --------------

    private void autocompleteAgency() {

        if (getActivity() != null) {
            Places.initialize(getActivity(), MAP_API_KEY);

            // Initialize the AutocompleteSupportFragment.
            autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_agency);

            // Specify the types of place data to return.
            if (autocompleteFragment != null) {
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
                autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
                autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        agencyName = place.getName();
                        agencyPlaceId = place.getId();
                    }

                    @Override
                    public void onError(@NonNull Status status) {
                        Log.e(TAG, "An error occurred: " + status);
                    }
                });
            } else {
                Log.e(TAG, "Null activity");
            }
        }


    }


    // --------------
    // AGENT
    // --------------

    private void createOrUpdateRealEstateAgent() {

        if (getContext() != null) {
            if (agencyName != null) {
                ManageAgency.saveAgencyName(getContext(), agencyName);

            } else {
                agencyName = ManageAgency.getAgencyName(getContext());

            }
            if (agencyPlaceId != null) {
                ManageAgency.saveAgencyPlaceId(getContext(), agencyPlaceId);
            } else {
                agencyPlaceId = ManageAgency.getAgencyPlaceId(getContext());
            }

        }

        if (firstnameEditText.getText().toString().isEmpty()) {
            firstnameEditText.setError(getString(R.string.set_error_add_agent_firstname));
        } else if (lastnameEditText.getText().toString().isEmpty()) {
            lastnameEditText.setError(getString(R.string.set_error_add_agent_lastname));
        } else if (agencyName == null) {
            if (getActivity() != null) {
                agencyName = ManageAgency.getAgencyName(getActivity());
            }


        } else {
            String firstname = firstnameEditText.getText().toString();
            String lastname = lastnameEditText.getText().toString();
            String realEstateAgentId;
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                realEstateAgentId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            } else {
                realEstateAgentId = null;
            }

            if (getContext() != null) {
                if (agencyName != null) {
                    ManageAgency.saveAgencyName(getContext(), agencyName);

                } else {
                    agencyName = ManageAgency.getAgencyName(getContext());

                }
                if (agencyPlaceId != null) {
                    ManageAgency.saveAgencyPlaceId(getContext(), agencyPlaceId);
                } else {
                    agencyPlaceId = ManageAgency.getAgencyPlaceId(getContext());
                }

            }


            //create agent in firebase
            RealEstateAgentHelper.createAgent(realEstateAgentId, firstname, lastname, urlPicture, agencyName, agencyPlaceId);

            assert realEstateAgentId != null;
            //agent object to create agent in room
            RealEstateAgents realEstateAgents = new RealEstateAgents(realEstateAgentId, firstname, lastname, urlPicture, agencyName, agencyPlaceId);


            if (bundle != null) {
                //update agent in room
                String realEstateAgentIdBundle = bundle.getString("myRealEstateAgentId", null);
                if (realEstateAgentIdBundle != null) {
                    this.propertyViewModel.updateRealEstateAgent(realEstateAgentIdBundle, firstname, lastname, urlPicture, agencyName, agencyPlaceId);

                    Toast.makeText(getContext(), getString(R.string.add_agent_dialog_update_confirm_text), Toast.LENGTH_SHORT).show();
                }


            } else {
                //create new agent in room
                this.propertyViewModel.createRealEstateAgent(realEstateAgents);

                Toast.makeText(getContext(), getString(R.string.add_agent_dialog_add_confirm_text), Toast.LENGTH_SHORT).show();
                startListPropertyActivity();

            }

            //to upload a photo on Firebase storage
            if (urlPicture != null) {
                StorageHelper storageHelper = new StorageHelper();
                storageHelper.uploadFromUri(Uri.parse(urlPicture), FirebaseAuth.getInstance().getCurrentUser().getEmail(), 6);

            }

            //to close the dialog
            if (getDialog() != null) {
                getDialog().dismiss();
            }
        }


    }


    private void retrieveRealEstateAgentData(String realEstateAgendId) {

        propertyViewModel.getRealEstateAgentById(realEstateAgendId).observe(getViewLifecycleOwner(), realEstateAgents -> {

            if (realEstateAgents.getFirstname() != null) {
                firstnameEditText.setText(realEstateAgents.getFirstname());
            }

            if (realEstateAgents.getLastname() != null) {
                lastnameEditText.setText(realEstateAgents.getLastname());
            }

            if (realEstateAgents.getAgencyName() != null) {
                if(autocompleteFragment!=null){
                    autocompleteFragment.setText(realEstateAgents.getAgencyName());
                }
            }

            if (realEstateAgents.getUrlPicture() != null) {
                urlPicture = realEstateAgents.getUrlPicture();
                // showImageInCircle(realEstateAgents.getUrlPicture());
                showImage(realEstateAgents.getUrlPicture());
            } else {
                Log.e(TAG, "getUrlPicture is null ");
            }


        });


    }

    // --------------
    // TAKE A PICTURE
    // --------------

    /**
     * Alert dialog for capture or select from galley
     */

    private void selectImage() {
        final CharSequence[] items = {
                getString(R.string.dialog_select_image_take_photo), MainApplication.getResourses().getString(R.string.dialog_select_image_choose_from_library),
                MainApplication.getResourses().getString(R.string.dialog_select_image_cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(MainApplication.getInstance().getResources().getString(R.string.dialog_select_image_take_photo))) {
                AddAgentDialogPermissionsDispatcher.dispatchTakePictureIntentWithPermissionCheck(this);
            } else if (items[item].equals(getString(R.string.dialog_select_image_choose_from_library))) {
                dispatchGalleryIntent();
            } else if (items[item].equals(getString(R.string.dialog_select_image_cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getContext() != null) {
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
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
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
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
                    if (selectedImage != null) {
                        urlPicture = imageCameraOrGallery.getRealPathFromUri(selectedImage);
                        showImageInCircle(urlPicture);
                    }

                    break;
                case REQUEST_CAMERA_PHOTO:

                    try {
                        mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (cameraFilePath != null) {
                        urlPicture = cameraFilePath;
                        showImageInCircle(cameraFilePath);
                    }


                    break;

            }

        }

    }

    private void showImageInCircle(String photoStringFromRoom) {

        Uri fileUri = Uri.parse(photoStringFromRoom);
        if (fileUri.getPath() != null) {
            Glide.with(MainApplication.getInstance().getApplicationContext())
                    .load(new File(fileUri.getPath()))
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_anon_user_48dp)
                    .into(agentPhoto);

        }
    }


    private void showImage(String photoUrl) {

        if (photoUrl != null) {

            File localFile = new File(photoUrl);

            File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String mFileName = "/" + localFile.getName();
            File goodFile = new File(storageDir, mFileName);
            if (goodFile.exists()) {
                if (goodFile.getPath() != null) {
                    imageInGlide(goodFile);
                }

            } else if (localFile.exists()) {
                if (localFile.getPath() != null) {
                    imageInGlide(localFile);
                }
            } else {

                Glide.with(MainApplication.getInstance().getApplicationContext())
                        .load(R.drawable.ic_anon_user_48dp)
                        .apply(RequestOptions.circleCropTransform())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("debago", "Exception is : " + e);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("debago", "onResourceReady 3");

                                return false;
                            }
                        })
                        .into(agentPhoto);

            }

        }
    }

    private void imageInGlide(File file){
        GlideApp.with(MainApplication.getInstance().getApplicationContext())
                .load(file)
                .apply(RequestOptions.circleCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        agentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("debago", "onResourceYEAH 5");

                        return false;
                    }
                })
                .into(agentPhoto);

    }
    // --------------
    // PERMISSION
    // --------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddAgentDialogPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // --------------
    // INTENT TO OPEN ACTIVITY
    // --------------

    private void startListPropertyActivity() {
        Intent intent = new Intent(getContext(), ListPropertyActivity.class);

        startActivity(intent);
    }

    // --------------
    // TEXTWATCHER
    // --------------

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (i2 == 0) {
            //put backgroung grey when we remove all text

            if (firstnameEditText.getText().hashCode() == charSequence.hashCode()) {
                firstnameEditText.setBackgroundResource(R.drawable.edit_text_design);
            } else if (lastnameEditText.getText().hashCode() == charSequence.hashCode()) {
                lastnameEditText.setBackgroundResource(R.drawable.edit_text_design);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable != null && !editable.toString().equalsIgnoreCase("")) {
            // Checking editable.hashCode() to understand which edittext is using right now

            if (firstnameEditText.getText().hashCode() == editable.hashCode()) {

                String value = editable.toString();
                firstnameEditText.removeTextChangedListener(this);

                if (value.length() == 0) {
                    // put backgroung grey when there is no action
                    firstnameEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {
                    // put backgroung white when they write
                    firstnameEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                firstnameEditText.addTextChangedListener(this);
            } else if (lastnameEditText.getText().hashCode() == editable.hashCode()) {

                String value = editable.toString();
                lastnameEditText.removeTextChangedListener(this);

                if (value.length() == 0) {

                    lastnameEditText.setBackgroundResource(R.drawable.edit_text_design);
                } else {

                    lastnameEditText.setBackgroundResource(R.drawable.edit_text_design_focused);
                }
                lastnameEditText.addTextChangedListener(this);
            }
        }
    }
}
