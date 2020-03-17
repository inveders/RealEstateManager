package com.inved.realestatemanager.firebase;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.utils.MainApplication;

public class PropertyHelper {

    private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "property";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getPropertyCollection() {
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

        photoUri1=downloadPhoto(photoUri1,propertyId);
        photoUri2=downloadPhoto(photoUri2,propertyId);
        photoUri3=downloadPhoto(photoUri3,propertyId);
        photoUri4=downloadPhoto(photoUri4,propertyId);
        photoUri5=downloadPhoto(photoUri5,propertyId);


        return new Property(propertyId, typeProperty, pricePropertyInEuro,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForProperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);
    }

    private static String downloadPhoto(String photo, String propertyId){
        StorageDownload storageDownload = new StorageDownload();
        if (photo != null && photo.length() < 30) {
            return storageDownload.beginDownload(photo, propertyId);
        }
        return null;
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
