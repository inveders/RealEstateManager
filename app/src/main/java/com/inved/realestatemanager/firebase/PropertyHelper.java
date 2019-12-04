package com.inved.realestatemanager.firebase;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

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

    public static void createProperty(String typeProperty, double pricePropertyInDollar,
                                      double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                      int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                                      String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                                      String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                                      boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                                      String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                                      String photoDescription4, String photoDescription5, String realEstateAgentEmail, long realEstateAgentId) {
        // 1 - Create Obj

        Property propertyToCreate = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5,realEstateAgentEmail, realEstateAgentId);

        PropertyHelper.getPropertyCollection().document().set(propertyToCreate);
    }

    // --- GET ---

    public static Query getAllProperties() {
        return PropertyHelper.getPropertyCollection();
    }

    public static Query getPropertyFirebaseId(String photoUri1){
        return PropertyHelper.getPropertyCollection()
                .whereEqualTo("photoUri1",photoUri1);
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
}
