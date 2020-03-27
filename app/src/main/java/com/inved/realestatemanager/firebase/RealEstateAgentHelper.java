package com.inved.realestatemanager.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.utils.MainApplication;

public class RealEstateAgentHelper {

    private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "users";

// --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_GENERAL).document(ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext())).collection(SUB_COLLECTION_NAME);

    }

    // --- CREATE ---

    public static void createAgent(String realEstateAgendId, String firstname, String lastname, String urlPicture, String agencyName, String agencyPlaceId) {
        // 1 - Create Obj

        RealEstateAgents realEstateAgentsToCreate = new RealEstateAgents(realEstateAgendId, firstname, lastname,urlPicture, agencyName,agencyPlaceId);

        RealEstateAgentHelper.getUsersCollection().document(realEstateAgendId).set(realEstateAgentsToCreate);
    }

    public static RealEstateAgents resetAgent(RealEstateAgents realEstateAgents){

        String firstname = realEstateAgents.getFirstname();
        String lastname = realEstateAgents.getLastname();
        String urlPicture = realEstateAgents.getUrlPicture();
        String agencyName = realEstateAgents.getAgencyName();
        String agencyPlaceId = realEstateAgents.getAgencyPlaceId();
        String realEstateAgentId = realEstateAgents.getRealEstateAgentId();

        return new RealEstateAgents(realEstateAgentId,firstname, lastname, urlPicture, agencyName, agencyPlaceId);
    }

    public static RealEstateAgents resetAgentDialog(RealEstateAgents realEstateAgents1){

        String realEstateAgentId = realEstateAgents1.getRealEstateAgentId();
        String agencyPlaceId = realEstateAgents1.getAgencyPlaceId();
        String agencyName = realEstateAgents1.getAgencyName();
        String firstname = realEstateAgents1.getFirstname();
        String lastname = realEstateAgents1.getLastname();
        String urlPicture = realEstateAgents1.getUrlPicture();

        StorageDownload storageDownload = new StorageDownload();

        if (urlPicture != null && urlPicture.length() < 30) {
            String uri1 = storageDownload.beginDownload(urlPicture, realEstateAgentId);
            if (uri1 != null) {
                urlPicture = uri1;
            }
        }

        return new RealEstateAgents(realEstateAgentId, firstname, lastname, urlPicture, agencyName, agencyPlaceId);

    }

    // --- GET ---

    public static Query getAgentWhateverAgency(String realEstateAgentId){

        return FirebaseFirestore.getInstance().collectionGroup(SUB_COLLECTION_NAME)

                .whereEqualTo("realEstateAgentId",realEstateAgentId);

    }

    public static Query getAllAgents(){
        return RealEstateAgentHelper.getUsersCollection();


    }

// --- UPDATE ---

    static void updateUrlPicture(String urlPicture) {

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null){
                RealEstateAgentHelper.getUsersCollection()
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .update("urlPicture", urlPicture);
            }
        }


    }

}
