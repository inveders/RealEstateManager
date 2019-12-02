package com.inved.realestatemanager.firebase;

public class RealEstateAgentHelper {

  /*  private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "users";

// --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_GENERAL).document(ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext())).collection(SUB_COLLECTION_NAME);

    }

    // --- CREATE ---

    public static Task<Void> createAgent(long realEstateAgentId, String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId) {
        // 1 - Create Obj

        RealEstateAgents realEstateAgentsToCreate = new RealEstateAgents(firstname, lastname,urlPicture, agencyName,agencyPlaceId);

        return RealEstateAgentHelper.getUsersCollection().document(String.valueOf(realEstateAgentId)).set(realEstateAgentsToCreate);
    }

    // --- GET ---

    public static Query getAgentWhateverAgency(String uid){

        return FirebaseFirestore.getInstance().collectionGroup(SUB_COLLECTION_NAME)

                .whereEqualTo("uid",uid);

    }

    public static Query getAllAgents(){
        return RealEstateAgentHelper.getUsersCollection()
                .orderBy("uid", Query.Direction.ASCENDING);


    }

    // --- DELETE ---

    public static Task<Void> deleteAgent(long realEstateAgentId) {
        return RealEstateAgentHelper.getUsersCollection().document(String.valueOf(realEstateAgentId)).delete();
    }*/
}
