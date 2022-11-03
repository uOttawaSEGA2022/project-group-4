package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.handlers.InboxHandler;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboxActions {
    FirebaseFirestore database;
    FirebaseRepository firebaseRepository;
    final static private String COMPLAINTS_COLLECTION = "Complaints";

    private String chefId;

    public InboxActions(FirebaseFirestore database, FirebaseRepository firebaseRepository) {
        this.database = database;
        this.firebaseRepository = firebaseRepository;
        this.chefId = "";
    }

    /**
     * Get all complaints from Firebase
     * @param inboxHandler reference to instance of inbox handler to pass operation response
     */
    public void getAllComplaints(InboxHandler inboxHandler) {
        // get all complaints from Firestore and once done, call appropriate method in inboxHandler
        database.collection(COMPLAINTS_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Complaint> complaints = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("getAllComplaints", document.getId() + " => " + document.getData());
                        complaints.add(getComplaintObject(document.getData()));
                    }
                    // pass complaints to inbox handler
                    inboxHandler.createNewAdminInbox(complaints);
                } else {
                    inboxHandler.errorGettingComplaints("Error getting complaints from database: " + task.getException());
                }
            }
        });
    }

    private Complaint getComplaintObject(Map<String, Object> data) {
        // cast object values in data to string
        Map<String, String> complaintData = Utilities.convertMapValuesToString(data);
        // convert date
        Date dateSubmitted = Date.valueOf(complaintData.get("dateSubmitted"));
        // return complaint object
        return new Complaint(complaintData.get("id"), complaintData.get("title"), complaintData.get("description"), complaintData.get("clientId"), complaintData.get("chefId"), dateSubmitted);
    }

    /**
     * Add a complaint to firebase
     * @param complaint Complaint object of complaint to be added
     * @param inboxHandler reference to instance of inbox handler to pass operation response
     */
    public void addComplaint(Complaint complaint, InboxHandler inboxHandler) {
        // proceed only if preconditions satisfied
        if (Preconditions.isNotNull(complaint) && Preconditions.isNotNull(inboxHandler)) {
            Map<String, Object> complaintData = complaint.getComplaintDataMap();

            database.collection(COMPLAINTS_COLLECTION)
                    .add(complaintData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // update complaint id
                            complaint.setId(documentReference.getId());
                            inboxHandler.successAddingComplaint(complaint);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            inboxHandler.errorAddingComplaint("Failed to add complaint to database: " + e.getMessage());
                        }
                    });
        } else {
            // if initial preconditions fail
            // if inbox handler is valid
            if (Preconditions.isNotNull(inboxHandler)) {
                inboxHandler.errorAddingComplaint("Invalid complaint object provided");
            } else {
                Log.e("addComplaint", "Invalid object values for complaint and inboxHandler");
            }
        }
    }

    /**
     * Remove complaint from Firebase
     * @param complaintId id of complaint to be removed
     * @param inboxHandler reference to instance of inbox handler to pass operation response
     */
    public void removeComplaint(String complaintId, InboxHandler inboxHandler, boolean isSuspended, Date suspensionDate) {

        DocumentReference complaint = database.collection(COMPLAINTS_COLLECTION).document(complaintId);

        complaint.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                            chefId = String.valueOf(document.getData().get("chefId"));
                        }
                        else { //just in case bros
                            Log.e("null data", "Document data is null: can't retrieve chefId");
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        // proceed only if preconditions satisfied
        if (Preconditions.isNotEmptyString(complaintId) && Preconditions.isNotNull(inboxHandler)) {
            complaint
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            inboxHandler.successRemovingComplaint();
                            handleChefComplaint(chefId, isSuspended, suspensionDate); // updates chef info in firebase accordingly
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("removeComplaint", "Error deleting document", e);
                            inboxHandler.errorRemovingComplaint("Error removing document from database: " + e.getMessage());
                        }
                    });
        } else {
            // if initial preconditions fail
            // if inbox handler is valid
            if (Preconditions.isNotNull(inboxHandler)) {
                inboxHandler.errorRemovingComplaint("Invalid complaint id provided");
            } else {
                Log.e("removeComplaint", "Invalid object values for complaintId and inboxHandler");
            }
        }

    }

    /**
     * Method changes fields (isSuspended and suspensionDate) of chef in firebase based on admin response
     * to complaint
     * @param chefId id of the chef associated with the complaint
     * @param ban boolean that says whether the chef is banned or not
     * @param suspensionDate end date of suspension
     */
    private void handleChefComplaint(String chefId, boolean ban, Date suspensionDate){

        if (ban == true){

            DocumentReference chefUser = database.collection("Chefs").document(chefId);

            // Set the "isSuspended" field to ban boolean and the "suspensionDate" field to suspensionDate date
            chefUser
                    .update(
                            "isSuspended", ban,
                            "suspensionDate",suspensionDate)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error updating document", e);
                        }
                    });

        }








    }
}
