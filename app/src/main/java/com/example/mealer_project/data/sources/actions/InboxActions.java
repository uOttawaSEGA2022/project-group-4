package com.example.mealer_project.data.sources.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.handlers.InboxHandler;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InboxActions {
    FirebaseFirestore database;
    FirebaseRepository firebaseRepository;
    final static private String COMPLAINTS_COLLECTION = "Complaints";

    public InboxActions(FirebaseFirestore database, FirebaseRepository firebaseRepository) {
        this.database = database;
        this.firebaseRepository = firebaseRepository;
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
                        try {
                            complaints.add(getComplaintObject(document.getId(), document.getData()));
                        } catch (Exception e) {
                            inboxHandler.errorGettingComplaints("Failed to get complaints!");
                            Log.e("getAllComplaints", "Failed to get complaints. Error: " + e.getMessage());
                        }
                    }
                    // pass complaints to inbox handler
                    inboxHandler.createNewAdminInbox(complaints);
                } else {
                    inboxHandler.errorGettingComplaints("Error getting complaints from database: " + task.getException());
                }
            }
        });
    }

    /**
     * Uses the provided data and returns a new Complaint instance
     * @param complaintId id of complaint
     * @param data Map containing all other required complaint data with the required property (key) names
     * @return a new Complaint object with associated data
     * @throws ParseException throws ParseException if creation of Complaint fails due to invalid dateSubmitted format
     */
    private Complaint getComplaintObject(String complaintId, Map<String, Object> data) throws ParseException {
        // cast object values in data to string
        Map<String, String> complaintData = Utilities.convertMapValuesToString(data);
        // return complaint object
        return new Complaint(
                complaintId,
                complaintData.get(Complaint.COMPLAINT_PROPERTY.title.toString()),
                complaintData.get(Complaint.COMPLAINT_PROPERTY.description.toString()),
                complaintData.get(Complaint.COMPLAINT_PROPERTY.clientId.toString()),
                complaintData.get(Complaint.COMPLAINT_PROPERTY.chefId.toString()),
                complaintData.get(Complaint.COMPLAINT_PROPERTY.dateSubmitted.toString())
        );
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

            Log.e("complaint", "adding complaint");

            database.collection(COMPLAINTS_COLLECTION)
                    .add(complaintData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // update complaint id
                            complaint.setId(documentReference.getId());
                            inboxHandler.successAddingComplaint(complaint);
                            Log.e("complaint", "added complaint");
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
            }
            Log.e("addComplaint", "Invalid object values for complaint and inboxHandler");
        }
    }

    /*public void addSampleComplaint(Complaint complaint) {
        Map<String, Object> complaintData = complaint.getComplaintDataMap();
        database.collection(COMPLAINTS_COLLECTION)
                .add(complaintData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // update complaint id
                        complaint.setId(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("addSampleComplaint", "Failed to add complaint to database: " + e.getMessage());
                    }
                });
    }*/

    /**
     * Remove complaint from Firebase
     * @param complaintId id of complaint to be removed
     * @param inboxHandler reference to instance of inbox handler to pass operation response
     */
    public void removeComplaint(String complaintId, InboxHandler inboxHandler) {

        // proceed only if preconditions satisfied
        if (Preconditions.isNotEmptyString(complaintId) && Preconditions.isNotNull(inboxHandler)) {
            database.collection(COMPLAINTS_COLLECTION).document(complaintId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            inboxHandler.successRemovingComplaint();
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

}
