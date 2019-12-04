package com.inved.realestatemanager.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.inved.realestatemanager.models.RealEstateAgents;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

public class RealEstateAgentHelper {

    private static final String COLLECTION_GENERAL = "agency";
    private static final String SUB_COLLECTION_NAME = "users";

// --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_GENERAL).document(ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext())).collection(SUB_COLLECTION_NAME);

    }

    // --- CREATE ---

    public static Task<Void> createAgent(String firstname, String lastname, String urlPicture,String agencyName,String agencyPlaceId,String email) {
        // 1 - Create Obj

        RealEstateAgents realEstateAgentsToCreate = new RealEstateAgents(firstname, lastname,urlPicture, agencyName,agencyPlaceId,email);

        return RealEstateAgentHelper.getUsersCollection().document(email).set(realEstateAgentsToCreate);
    }

    // --- GET ---

    public static Query getAgentWhateverAgency(String email){

        return FirebaseFirestore.getInstance().collectionGroup(SUB_COLLECTION_NAME)

                .whereEqualTo("email",email);

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

    // --- DELETE ---

    public static Task<Void> deleteAgent(String email) {
        return RealEstateAgentHelper.getUsersCollection().document(email).delete();
    }
}
