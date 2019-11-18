package com.inved.realestatemanager.controller.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inved.realestatemanager.BuildConfig;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.MainActivity;
import com.inved.realestatemanager.controller.dialogs.DatePickerFragment;
import com.inved.realestatemanager.domain.DateOfDay;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.CreateUpdatePropertyViewModel;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageCreateUpdateChoice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CreateUpdatePropertyFragmentTwo extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CAMERA_PHOTO = 456;
    private static final int REQUEST_GALLERY_PHOTO = 455;

    private String cameraFilePath;

    private PropertyViewModel propertyViewModel;
    private TextView dateOfEntry;
    private Spinner agentNameSpinner;
    private EditText fullDescriptionEditText;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private ImageView photo5;

    private String photoUri = null;
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
    private String addressCompl;
    private long realEstateAgentId;
    private long propertyId;

    private Context context;
    private String selectedAgent;

    private List<String> spinnerAgentList = new ArrayList<>();

    private static final int REQUEST_CODE_DATE_PICKER = 11; // Used to identify the result

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_update_two, container, false);

        dateOfEntry = v.findViewById(R.id.activity_create_update_property_date_entry_text);
        agentNameSpinner = v.findViewById(R.id.create_update_spinner_real_estate_agent_text);

        photo1 = v.findViewById(R.id.activity_create_update_added_photo_one);
        photo2 = v.findViewById(R.id.activity_create_update_added_photo_two);
        photo3 = v.findViewById(R.id.activity_create_update_added_photo_three);
        photo4 = v.findViewById(R.id.activity_create_update_added_photo_four);
        photo5 = v.findViewById(R.id.activity_create_update_added_photo_five);
        fullDescriptionEditText = v.findViewById(R.id.activity_create_update_property_full_description_text);

        Button confirmButton = v.findViewById(R.id.create_update_confirm_button);
        confirmButton.setOnClickListener(view -> finishToCreateProperty());

        Button addPhotoButton = v.findViewById(R.id.activity_create_update_add_photo_button);

        addPhotoButton.setOnClickListener(view -> selectImage());


        this.updateUI();
        this.configureViewModel();
        this.retriveRealEstateAgents();
        this.datePickerInit();


        if (getActivity() != null) {
            context = getActivity();
        } else {
            context = MainApplication.getInstance().getApplicationContext();
        }
        if (ManageCreateUpdateChoice.getCreateUpdateChoice(context) != 0) {
            propertyId = ManageCreateUpdateChoice.getCreateUpdateChoice(context);

            this.updateUIwithDataFromDatabase(propertyId);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            CreateUpdatePropertyViewModel createUpdatePropertyViewModel = ViewModelProviders.of(getActivity()).get(CreateUpdatePropertyViewModel.class);
            createUpdatePropertyViewModel.getMyData().observe(getViewLifecycleOwner(), objects -> {

                this.typeProperty = objects.get(0).toString();
                this.numberRoomsInProperty = objects.get(1).toString();
                this.numberBathroomsInProperty = objects.get(2).toString();
                this.numberBedroomsInProperty = (int) objects.get(3);
                this.pricePropertyInDollar = Double.valueOf(objects.get(4).toString());
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

                this.propertyId = (long) objects.get(13);

            });
        }

    }


    private void updateUIwithDataFromDatabase(long propertyId) {
        propertyViewModel.getOneProperty(propertyId).observe(this, property -> {

            if (property.getDateOfEntryOnMarketForProperty().isEmpty() || property.getDateOfEntryOnMarketForProperty() == null) {
                datePickerInit();
            } else {
                dateOfEntry.setText(property.getDateOfEntryOnMarketForProperty());
            }

            propertyViewModel.getRealEstateAgentById(property.getRealEstateAgentId()).observe(this, realEstateAgents -> {
                String firstname = realEstateAgents.getFirstname();
                String lastname = realEstateAgents.getLastname();
                agentNameSpinner.setSelection(getIndexSpinner(agentNameSpinner, firstname + " " + lastname));
            });


            if (!property.getFullDescriptionProperty().isEmpty() || property.getFullDescriptionProperty() != null) {
                fullDescriptionEditText.setText(property.getFullDescriptionProperty());
            }

            photoUri1 = property.getPhotoUri1();
            photoUri2 = property.getPhotoUri2();
            photoUri3 = property.getPhotoUri3();
            photoUri4 = property.getPhotoUri4();
            photoUri5 = property.getPhotoUri5();
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

        Log.d("debaga", "before open camera");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity() != null) {
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
                    photo1.setImageURI(selectedImage);
                    if (selectedImage != null) {
                        photoUri = selectedImage.toString();
                    }
                    break;
                case REQUEST_CAMERA_PHOTO:

                    Log.d("debaga", "after open camera");
                    if (cameraFilePath != null) {
                        Log.d("debago", "urlPicture two camera file path : " + cameraFilePath);
                        photoUri = cameraFilePath;

                    }


                    break;
                case REQUEST_CODE_DATE_PICKER:
                    // get date from string
                    String selectedDate = data.getStringExtra("selectedDate");
                    // set the value of the editText
                    dateOfEntry.setText(selectedDate);
                    break;
            }

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

            updateUI();
        }


    }


    /**
     * Create file with current timestamp name
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        if (getActivity() != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
            // Save a file: path for using again
            cameraFilePath = "file://" + mFile.getAbsolutePath();
            return mFile;
        }

        return null;
    }


    //PERMISSION

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        CreateUpdatePropertyFragmentTwoPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }*/

    //REAL ESTATE AGENT MANAGEMENT AND SPINNER

    private void retriveRealEstateAgents() {
        if (propertyViewModel.getAllRealEstateAgents() != null) {
            propertyViewModel.getAllRealEstateAgents().observe(this, realEstateAgents -> {
                for (RealEstateAgents list : realEstateAgents) {
                    String firstname = list.getFirstname();
                    String lastname = list.getLastname();
                    String agentFirstnameLastname = firstname + " " + lastname;
                    spinnerAgentList.add(agentFirstnameLastname);
                }


            });

            //I fill agent spinner with firstname and lastname of the database programatically

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    MainApplication.getInstance().getApplicationContext(), android.R.layout.simple_spinner_item, spinnerAgentList);
            agentNameSpinner.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            agentNameSpinner.setOnItemSelectedListener(this);
            adapter.notifyDataSetChanged();


        }
    }

    //private method of your class
    private int getIndexSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {


                return i;
            }
        }

        return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // if (parent.getRealEstateAgentId() == R.id.create_update_spinner_real_estate_agent_text) {
        if (parent.getId() == R.id.create_update_spinner_real_estate_agent_text) {
            selectedAgent = agentNameSpinner.getSelectedItem().toString();
            SplitString splitString = new SplitString();
            String firstname = splitString.splitStringWithSpace(selectedAgent, 0);
            String lastname = splitString.splitStringWithSpace(selectedAgent, 1);
            this.propertyViewModel.getRealEstateAgentByName(firstname, lastname).observe(this, realEstateAgents -> realEstateAgentId = realEstateAgents.getRealEstateAgentId());
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //DATE PICKER

    private void datePickerInit() {
        dateOfEntry.setText(getString(R.string.create_update_choose_date));
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
        ManageCreateUpdateChoice.saveCreateUpdateChoice(MainApplication.getInstance().getApplicationContext(), 0);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void finishToCreateProperty() {

        if (selectedAgent == null) {
            Toast.makeText(context, getString(R.string.set_error_agent), Toast.LENGTH_SHORT).show();
        } else {
            selectedAgent = "Alexandra Gnimadi"; /**LE TEMPS DE TROUVER POURQUOI ON ARRIVE PAS A SELECTIONNER LE SPINENR*/
        }
        if (fullDescriptionEditText.getText().toString().trim().isEmpty()) {
            fullDescriptionEditText.setError(getString(R.string.set_error_street_number));
        } else if (dateOfEntry.getText().toString().trim().isEmpty()) {
            DateOfDay dateOfDay = new DateOfDay();
            dateOfEntry.setText(dateOfDay.getDateOfDay());
        } else {

            String statusProperty = getString(R.string.status_property_in_progress);
            String dateOfEntryOnMarketForProperty = dateOfEntry.getText().toString();
            String fullDescriptionText = fullDescriptionEditText.getText().toString();

            Property newProperty = new Property(typeProperty, pricePropertyInDollar,
                    surfaceAreaProperty, numberRoomsInProperty,
                    numberBathroomsInProperty, numberBedroomsInProperty,
                    fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                    statusProperty, dateOfEntryOnMarketForProperty,
                    null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                    photoDescription3, photoDescription4, photoDescription5, 1);

            if (ManageCreateUpdateChoice.getCreateUpdateChoice(MainApplication.getInstance().getApplicationContext()) != 0) {
                Log.d("debago","update property : "+typeProperty+" "+pricePropertyInDollar+" "+surfaceAreaProperty+" "+numberRoomsInProperty+" "+
                        numberBathroomsInProperty+" "+numberBedroomsInProperty+" "+fullDescriptionText+" "+streetNumber+" "+streetName+" "+zipCode+" "+
                        townProperty+" "+country+" "+addressCompl+" "+pointOfInterest+" "+statusProperty+" "+dateOfEntryOnMarketForProperty+" "+null+" "+
                        false+" "+photoUri1+" "+photoUri2+" "+photoUri3+" "+photoUri4+" "+photoUri5+" "+photoDescription1+" "+photoDescription2+
                        " "+photoDescription3+" "+photoDescription4+" "+photoDescription5+" "+1+" "+propertyId);


                this.propertyViewModel.updateProperty(typeProperty, pricePropertyInDollar,
                        surfaceAreaProperty, numberRoomsInProperty,
                        numberBathroomsInProperty, numberBedroomsInProperty,
                        fullDescriptionText, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                        statusProperty, dateOfEntryOnMarketForProperty,
                        null, false, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                        photoDescription3, photoDescription4, photoDescription5, 1,propertyId);
                Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_update), Toast.LENGTH_SHORT).show();
            } else {
                this.propertyViewModel.createProperty(newProperty);
                Toast.makeText(getContext(), getString(R.string.create_update_creation_confirmation_creation), Toast.LENGTH_SHORT).show();
                if (getContext() != null) {
                    ManageCreateUpdateChoice.saveCreateUpdateChoice(getContext(), 0);
                }

            }

            startMainActivity();
        }


    }

    private void updateUI() {

        Log.d("debago", "photoUri1 : " + photoUri1 + " photoUri2 : " + photoUri2 + " photoUri3 : " + photoUri3 + " photoUri4 : " + photoUri4 + " photoUri5 : " + photoUri5);

        String urlNoImage = "https://semantic-ui.com/images/wireframe/image.png";
        Uri fileUri;
        if (photoUri1 != null) {
            fileUri = Uri.parse(photoUri1);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .apply(RequestOptions.centerCropTransform())
                        .override(240, 240)
                        .fitCenter()
                        .into(photo1);

            }
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .override(240, 240)
                    .fitCenter()
                    .into(photo1);
        }

        if (photoUri2 != null) {
            fileUri = Uri.parse(photoUri2);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .override(50, 50)
                        .fitCenter()
                        .into(photo2);
            }
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .override(50, 50)
                    .fitCenter()
                    .into(photo2);
        }

        if (photoUri3 != null) {
            fileUri = Uri.parse(photoUri3);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .override(50, 50)
                        .fitCenter()
                        .into(photo3);
            }
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .override(50, 50)
                    .fitCenter()
                    .into(photo3);
        }

        if (photoUri4 != null) {
            fileUri = Uri.parse(photoUri4);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .override(50, 50)
                        .fitCenter()
                        .into(photo4);
            }
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .override(50, 50)
                    .fitCenter()
                    .into(photo4);
        }

        if (photoUri5 != null) {
            fileUri = Uri.parse(photoUri5);
            if (fileUri.getPath() != null) {
                Glide.with(this)
                        .load(new File(fileUri.getPath()))
                        .override(50, 50)
                        .fitCenter()
                        .into(photo5);
            }
        } else {
            Glide.with(this)
                    .load(urlNoImage)
                    .override(50, 50)
                    .fitCenter()
                    .into(photo5);
        }
    }

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(MainApplication.getInstance().getApplicationContext());
        this.propertyViewModel = ViewModelProviders.of(this, mViewModelFactory).get(PropertyViewModel.class);

    }

    private void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }


}
