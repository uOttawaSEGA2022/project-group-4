package com.example.mealer_project.data.sources.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.handlers.InboxHandler;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.example.mealer_project.utils.Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public InboxActions(FirebaseFirestore database, FirebaseRepository firebaseRepository) {
        this.database = database;
        this.firebaseRepository = firebaseRepository;
    }

    public void getAllComplaints(InboxHandler inboxHandler) {
        // get all complaints from Firestore and once done, call appropriate method in inboxHandler
        database.collection("Complaint").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
}
