package com.inved.realestatemanager.controller.dialogs;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.domain.UriToStringConversion;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddAgentDialog extends DialogFragment {

    private static final String MAP_API_KEY = BuildConfig.GOOGLE_MAPS_API_KEY;
    private static final int REQUEST_CAMERA_PHOTO = 456;
    private static final int REQUEST_GALLERY_PHOTO = 455;
    private static final String TAG = "debago";
    private String cameraFilePath;
    private Bundle bundle;
    private UriToStringConversion uriToStringConversion = new UriToStringConversion();
    //View Model
    private PropertyViewModel propertyViewModel;

    //UI
    private String urlPicture;

    //Widget
    @BindView(R.id.add_agent_photo)
    ImageView agentPhoto;

    @BindView(R.id.add_agent_add_photo_button)
    Button addPhotoButton;

    @BindView(R.id.add_agent_edittext_lastname)
    EditText lastnameEditText;

    @BindView(R.id.add_agent_edittext_firstname)
    EditText firstnameEditText;

    @BindView(R.id.textview_agency_name_edit)
    TextView agencyNameTextview;

    AutocompleteSupportFragment autocompleteFragment;

    @BindView(R.id.add_agent_dialog_add_new_agent)
    TextView addActionButton;

    @BindView(R.id.agent_add_dialog_close)
    ImageButton cancelSearchButton;

    private String agencyName;
    private String agencyPlaceId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_add_agent_dialog, container, false);
        ButterKnife.bind(this, view);

        this.configureViewModel();

        if (getDialog() != null) {
            getDialog().setTitle(getString(R.string.add_agent_dialog_title));
        }

        bundle = getArguments();
        if (bundle != null) {
            addActionButton.setText(getString(R.string.add_agent_dialog_edit_text));
            long realEstateAgentId = bundle.getLong("myLong", 0);
            if (realEstateAgentId != 0) {
                editRealEstateAgent(realEstateAgentId);
            }

        }

        autocompleteAgency();

        addPhotoButton.setOnClickListener(v -> selectImage());
        cancelSearchButton.setOnClickListener(v -> getDialog().cancel());
        addActionButton.setOnClickListener(v -> this.createNewRealEstateAgent());

        return view;
    }

    private void autocompleteAgency() {

        if(getActivity()!=null){
            Places.initialize(getActivity(), MAP_API_KEY);
            // Initialize the AutocompleteSupportFragment.
            autocompleteFragment = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_agency);

            // Specify the types of place data to return.
            if (autocompleteFragment != null) {
                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));
                autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
                autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

                // Set up a PlaceSelectionListener to handle the response.
                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());
                        agencyName = place.getName();
                        agencyPlaceId = place.getId();
                    }

                    @Override
                    public void onError(@NonNull Status status) {
                        Log.d(TAG, "An error occurred: " + status);
                    }
                });
            }else{
                Log.d(TAG, "Null activity");
            }
        }


    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        this.propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);
    }

    // Get all items for a user
    private void createNewRealEstateAgent() {

        if (firstnameEditText.getText().toString().isEmpty()) {
            firstnameEditText.setError(getString(R.string.set_error_add_agent_firstname));
        } else if (lastnameEditText.getText().toString().isEmpty()) {
            lastnameEditText.setError(getString(R.string.set_error_add_agent_lastname));
        } else if (agencyName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.set_error_add_agent_agency), Toast.LENGTH_SHORT).show();
        } else {
            String firstname = firstnameEditText.getText().toString();
            String lastname = lastnameEditText.getText().toString();
            String email;
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            }else{
                email=null;
            }

            if (getContext() != null) {
                if (agencyName != null) {
                    ManageAgency.saveAgencyPlaceId(getContext(), agencyName);
                }
                if (agencyPlaceId != null) {
                    ManageAgency.saveAgencyName(getContext(), agencyPlaceId);
                }

            }

            RealEstateAgents realEstateAgents = new RealEstateAgents(firstname, lastname, urlPicture, agencyName, agencyPlaceId,email);

            RealEstateAgentHelper.createAgent(realEstateAgents.getRealEstateAgentId(), firstname, lastname, urlPicture,agencyName,agencyPlaceId,email);

            if (bundle != null) {

                this.propertyViewModel.updateRealEstateAgent(realEstateAgents);

                Toast.makeText(getContext(), getString(R.string.add_agent_dialog_update_confirm_text), Toast.LENGTH_SHORT).show();

            } else {

                this.propertyViewModel.createRealEstateAgent(realEstateAgents);

                Toast.makeText(getContext(), getString(R.string.add_agent_dialog_add_confirm_text), Toast.LENGTH_SHORT).show();
                startListPropertyActivity();

            }

            if (getDialog() != null) {
                getDialog().dismiss();
            }
        }


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
                AddAgentDialogPermissionsDispatcher.dispatchTakePictureIntentWithPermissionCheck(this);

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
        if (getContext() != null) {
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);

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
                    Uri uriToConvert = data.getData();
                    Log.d("debago", "uri to convert : " + uriToConvert);
                    String selectedImage = "file://" + uriToStringConversion.getRealPathFromURI(getContext(), uriToConvert);

                    if (selectedImage.contains("WhatsApp")) {
                        Toast.makeText(getContext(), getString(R.string.whatsapp_photo_not_possible), Toast.LENGTH_SHORT).show();
                        agentPhoto.setImageResource(R.drawable.ic_anon_user_48dp);
                    } else {
                        agentPhoto.setImageURI(Uri.parse(selectedImage));
                        if (uriToStringConversion.getRealPathFromURI(getContext(), uriToConvert) != null) {
                            showImageInCircle(selectedImage);
                            urlPicture = selectedImage;
                        }
                    }

                    break;
                case REQUEST_CAMERA_PHOTO:

                    if (cameraFilePath != null) {
                        urlPicture = cameraFilePath;
                        showImageInCircle(cameraFilePath);

                    }

                    break;

            }

        }

    }


    /**
     * Create file with current timestamp name
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        if (getContext() != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
            // Save a file: path for using again
            cameraFilePath = "file://" + mFile.getAbsolutePath();
            return mFile;
        }

        return null;
    }


    //PERMISSION

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddAgentDialogPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // Get all items for a user
    private void editRealEstateAgent(long realEstateAgendId) {

        if(autocompleteFragment.getView()!=null){
            autocompleteFragment.getView().setVisibility(View.GONE);
            agencyNameTextview.setVisibility(View.VISIBLE);
        }


        propertyViewModel.getRealEstateAgentById(realEstateAgendId).observe(getViewLifecycleOwner(), realEstateAgents -> {

            if (realEstateAgents.getFirstname() != null) {
                firstnameEditText.setText(realEstateAgents.getFirstname());
            }

            if (realEstateAgents.getLastname() != null) {
                lastnameEditText.setText(realEstateAgents.getLastname());
            }

            if (realEstateAgents.getAgencyName() != null) {
                agencyNameTextview.setText(realEstateAgents.getAgencyName());
            }

            if (realEstateAgents.getUrlPicture() != null) {
                urlPicture = realEstateAgents.getUrlPicture();
                showImageInCircle(realEstateAgents.getUrlPicture());
            }


        });


    }

    private void showImageInCircle(String photoStringFromRoom) {

        Uri fileUri = Uri.parse(photoStringFromRoom);
        if (fileUri.getPath() != null) {
            Glide.with(MainApplication.getInstance().getApplicationContext())
                    .load(new File(fileUri.getPath()))
                    .apply(RequestOptions.circleCropTransform())
                    .into((agentPhoto));
        }
    }

    private void startListPropertyActivity() {
        Intent intent = new Intent(getContext(), ListPropertyActivity.class);

        startActivity(intent);
    }

}
