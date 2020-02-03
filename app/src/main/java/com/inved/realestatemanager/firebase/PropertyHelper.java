package com.inved.realestatemanager.firebase;


import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;

public class PropertyHelper {

    private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "property";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getPropertyCollection() {
        Log.d("debago", "agencyPlaceId is " + ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext()));
        return FirebaseFirestore.getInstance().collection(COLLECTION_GENERAL)
                .document(ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext()))
                .collection(SUB_COLLECTION_NAME);


    }

    // --- CREATE ---

    public static void createProperty(String propertyId, String typeProperty, double pricePropertyInEuro,
                                      double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                      int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                                      String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                                      String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForProperty,
                                      boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                                      String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                                      String photoDescription4, String photoDescription5, String realEstateAgentId) {
        // 1 - Create


        Property propertyToCreate = new Property(propertyId, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForProperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

        PropertyHelper.getPropertyCollection().document(propertyId).set(propertyToCreate);

    }

    public static Property resetProperties(Property property) {

        String propertyId = property.getPropertyId();
        String typeProperty = property.getTypeProperty();
        double pricePropertyInEuro = property.getPricePropertyInEuro();
        double surfaceAreaProperty = property.getSurfaceAreaProperty();
        String numberRoomsInProperty = property.getNumberRoomsInProperty();
        String numberBathroomsInProperty = property.getNumberBathroomsInProperty();
        int numberBedroomsInProperty = property.getNumberBedroomsInProperty();
        String fullDescriptionProperty = property.getFullDescriptionProperty();

        String streetNumber = property.getStreetNumber();
        String streetName = property.getStreetName();
        String zipCode = property.getZipCode();
        String townProperty = property.getTownProperty();
        String country = property.getCountry();
        String addressCompl = property.getAddressCompl();
        String pointOfInterest = property.getPointOfInterest();
        String statusProperty = property.getStatusProperty();
        String dateOfEntryOnMarketForProperty = property.getDateOfEntryOnMarketForProperty();
        String dateOfSaleForProperty = property.getDateOfSaleForProperty();
        boolean selected = property.isSelected();
        String photoUri1 = property.getPhotoUri1();
        String photoUri2 = property.getPhotoUri2();
        String photoUri3 = property.getPhotoUri3();
        String photoUri4 = property.getPhotoUri4();
        String photoUri5 = property.getPhotoUri5();
        String photoDescription1 = property.getPhotoDescription1();
        String photoDescription2 = property.getPhotoDescription2();
        String photoDescription3 = property.getPhotoDescription3();
        String photoDescription4 = property.getPhotoDescription4();
        String photoDescription5 = property.getPhotoDescription5();
        String realEstateAgentId = property.getRealEstateAgentId();

        StorageDownload storageDownload = new StorageDownload();

        if (photoUri1 != null && photoUri1.length() < 30) {
            String uri1 = storageDownload.beginDownload(photoUri1, propertyId);
            if (uri1 != null) {
                photoUri1 = uri1;
            }
        }

        if (photoUri2 != null && photoUri2.length() < 30) {
            String uri2 = storageDownload.beginDownload(photoUri2, propertyId);
            if (uri2 != null) {
                photoUri2 = uri2;
            }
        }

        if (photoUri3 != null && photoUri3.length() < 30) {
            String uri3 = storageDownload.beginDownload(photoUri3, propertyId);
            if (uri3 != null) {
                photoUri3 = uri3;
            }
        }

        if (photoUri4 != null && photoUri4.length() < 30) {
            String uri4 = storageDownload.beginDownload(photoUri4, propertyId);
            if (uri4 != null) {
                photoUri4 = uri4;
            }
        }

        if (photoUri5 != null && photoUri5.length() < 30) {
            String uri5 = storageDownload.beginDownload(photoUri5, propertyId);
            if (uri5 != null) {
                photoUri1 = uri5;
            }
        }

        return new Property(propertyId, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForProperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);
    }

    // --- GET ---

    public static Query getAllProperties() {
        return PropertyHelper.getPropertyCollection();
    }

    public static Query getPropertyWithId(String propertyId) {
        return PropertyHelper.getPropertyCollection()
                .whereEqualTo("propertyId", propertyId);
    }

    // --- UPDATE ---

    static void updatePhotoUri1(String photoUri1Url, String documentId) {

        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("photoUri1", photoUri1Url);
    }

    static void updatePhotoUri2(String photoUri2Url, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("photoUri2", photoUri2Url);
    }

    static void updatePhotoUri3(String photoUri3Url, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("photoUri3", photoUri3Url);
    }

    static void updatePhotoUri4(String photoUri4Url, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("photoUri4", photoUri4Url);
    }

    static void updatePhotoUri5(String photoUri5Url, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("photoUri5", photoUri5Url);
    }

    public static void updateDateOfSale(String dateOfSale, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("dateOfSaleForProperty", dateOfSale);
    }

    public static void updateStatusProperty(String status, String documentId) {
        PropertyHelper.getPropertyCollection()
                .document(documentId)
                .update("statusProperty", status);
    }
}
