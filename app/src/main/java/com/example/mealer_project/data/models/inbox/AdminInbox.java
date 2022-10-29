package com.example.mealer_project.data.models.inbox;

import android.util.Log;
import java.util.HashMap;
import java.util.List;

import com.example.mealer_project.utils.Preconditions;

/**
 * Complaints inbox of Admin
 */
public class AdminInbox implements Inbox {
    // store complaints in a Map for quickly accessing any complaint by it's id without need for traversal
    HashMap<String, Complaint> complaints;

    /**
     * Create a new admin inbox
     */
    public AdminInbox() {
        complaints = new HashMap<>();
    }

    /**
     * Create a new admin inbox when number of complaints is known
     * Additional advantage: avoids overhead of dynamic memory for array based maps
     */
    public AdminInbox(int size) {
        complaints = new HashMap<>(size);
    }

    /**
     * Create a new admin inbox by providing list of complaints
     * @param inboxComplaints list of complaints to be added to the inbox
     */
    public AdminInbox(List<Complaint> inboxComplaints) {
        complaints = new HashMap<>(inboxComplaints.size());
        this.addComplaints(inboxComplaints);
    }

    /**
     * Add a complaint to the inbox
     * @param complaint Complaint object to be added to inbox
     * @throws NullPointerException if provided complaint ID object is null
     */
    @Override
    public void addComplaint(Complaint complaint) throws NullPointerException {
        // validate complaint object
        if (complaint == null) {
            // log for programmer, and exception message for client
            Log.e("addComplaint", "addComplaint: Complaint object provided is null");
            throw new NullPointerException("Trying to add an invalid complaint!");
        }

        // add complaint
        complaints.put(complaint.getId(), complaint);
    }

    /**
     * Add multiple complaints by providing a list of complaints
     * @param complaints list of complaints to be added to the inbox
     * @throws NullPointerException if provided list of complaints is null
     */
    public void addComplaints(List<Complaint> complaints) throws NullPointerException {
        // validate complaint object
        if (Preconditions.isNotEmptyList(complaints)) {
            // log for programmer, and exception message for client
            Log.e("addComplaints", "addComplaints: List<Complaint> provided is null");
            throw new NullPointerException("No complaints provided to be added to the inbox!");
        }

        // add all complaints to the admin inbox
        for (Complaint complaint: complaints) {
            // add complaint, throws NullPointerException if complaint is null
            this.addComplaint(complaint);
        }
    }

    /**
     * Remove a complaint by ID
     * @param complaintId ID of complaint to be removed
     * @throws NullPointerException if provided complaint ID object is null
     */
    @Override
    public void removeComplaint(String complaintId) throws NullPointerException {
        if (Preconditions.isNotEmptyString(complaintId)) {
            // remove the complaint
            complaints.remove(complaintId);
        } else {
            // log for programmer, and exception message for client
            Log.e("removeComplaint", "complaintId provided is null");
            throw new NullPointerException("No complaint ID provided!");
        }
    }

    /**
     * Get a complaint by id
     * @param complaintId ID of the complaint to retrieve
     * @return Returns the complaint object which matched the provided complaint ID else false
     * @throws NullPointerException if provided complaint ID object is null
     */
    @Override
    public Complaint getComplaint(String complaintId) throws NullPointerException {
        if (Preconditions.isNotEmptyString(complaintId)) {
            // return the complaint object if it exists, else null
            return complaints.get(complaintId);
        } else {
            // log for programmer, and exception message for client
            Log.e("getComplaint", "complaintId provided is null");
            throw new NullPointerException("No complaint ID provided!");
        }
    }
}
