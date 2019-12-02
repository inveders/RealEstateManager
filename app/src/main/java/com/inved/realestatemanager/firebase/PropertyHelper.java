package com.inved.realestatemanager.firebase;


public class PropertyHelper {

  /*  private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "property";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getPropertyCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_GENERAL)
                .document(ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext()))
                .collection(SUB_COLLECTION_NAME);

    }

    // --- CREATE ---

    public static void createProperty(long propertyId, String typeProperty, double pricePropertyInDollar,
                                      double surfaceAreaProperty, String numberRoomsInProperty, String numberBathroomsInProperty,
                                      int numberBedroomsInProperty, String fullDescriptionProperty, String streetNumber,
                                      String streetName, String zipCode, String townProperty, String country, String addressCompl, String pointOfInterest,
                                      String statusProperty, String dateOfEntryOnMarketForProperty, String dateOfSaleForPorperty,
                                      boolean selected, String photoUri1, String photoUri2, String photoUri3, String photoUri4,
                                      String photoUri5, String photoDescription1, String photoDescription2, String photoDescription3,
                                      String photoDescription4, String photoDescription5, long realEstateAgentId) {
        // 1 - Create Obj

        Property propertyToCreate = new Property(typeProperty, pricePropertyInDollar,
                surfaceAreaProperty, numberRoomsInProperty,
                numberBathroomsInProperty, numberBedroomsInProperty,
                fullDescriptionProperty, streetNumber, streetName, zipCode, townProperty, country, addressCompl, pointOfInterest,
                statusProperty, dateOfEntryOnMarketForProperty,
                dateOfSaleForPorperty, selected, photoUri1, photoUri2, photoUri3, photoUri4, photoUri5, photoDescription1, photoDescription2,
                photoDescription3, photoDescription4, photoDescription5, realEstateAgentId);

        PropertyHelper.getPropertyCollection().document(String.valueOf(propertyId)).set(propertyToCreate);
    }

    // --- GET ---

    public static Query getAllProperties() {
        return PropertyHelper.getPropertyCollection();
    }
*/


}
