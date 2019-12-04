package com.inved.realestatemanager.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StorageHelper {

    private static StorageReference riversRef;

    // --- COLLECTION REFERENCE ---

    private static StorageReference getStorageReference() {
        String email = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }

        if (email != null) {
            return FirebaseStorage.getInstance().getReference(email);
        }
        return null;

    }

    //UPLOAD A FILE
    public static void uploadFile(String imagePath, int number, String documentId) {
        Uri file = Uri.fromFile(new File(imagePath));
        riversRef = Objects.requireNonNull(getStorageReference()).child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Log.d("debago", "failure upload image");
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            Log.d("debago", "success upload image");
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            // Continue with the task to get the download URL
            return riversRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                if (downloadUri != null) {
                    String downloadURL = downloadUri.toString();

                    //We have to pure here the url in firestore
                    switch (number) {
                        case 1:
                            PropertyHelper.updatePhotoUri1(downloadURL, documentId);
                            break;
                        case 2:
                            PropertyHelper.updatePhotoUri2(downloadURL, documentId);
                            break;
                        case 3:
                            PropertyHelper.updatePhotoUri3(downloadURL, documentId);
                            break;
                        case 4:
                            PropertyHelper.updatePhotoUri4(downloadURL, documentId);
                            break;
                        case 5:
                            PropertyHelper.updatePhotoUri5(downloadURL, documentId);
                            break;
                        case 6:
                            RealEstateAgentHelper.updateUrlPicture(downloadURL);
                            break;
                        default:
                            // code block
                    }

                }
            } else {
                // Handle failures
                // ...
                Log.d("debago", "failure url retrieving");
            }
        });
    }

    //DOWNLOAD A FILE
    public static void downloadFile(String imagePath) throws IOException {
        riversRef = Objects.requireNonNull(getStorageReference()).child(imagePath);

        File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Successfully downloaded data to local file
                    // ...
                    Log.d("debago", "download successful");
                }).addOnFailureListener(exception -> {
            // Handle failed download
            // ...
            Log.d("debago", "download failed");
        });

    }
}
