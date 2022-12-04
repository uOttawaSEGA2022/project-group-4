package com.example.mealer_project.data.models.inbox;

import com.example.mealer_project.data.entity_models.ComplaintEntityModel;
import com.example.mealer_project.utils.Utilities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Complaint class to create instances of complaints which will be stored in an inbox
 * Implements Comparator to enable sorting of complaints by date submitted
 */
public class Complaint implements Comparator<Complaint>, Serializable {
    private String errorMsg = "";

    // instance variables
    private String id;
    private String title;
    private String description;
    private String clientId;
    private String chefId;
    private String orderId;
    private boolean isResolved;
    private Date dateSubmitted;

    /**
     * Using enum to define property names of a complaint in a structured (and more rigid) manner
     * Prevents use of hard-coded string throughout application where property of a complaints to be used
     * Used in InboxActions, as property names here are same as field names on firebase
     */
    public enum COMPLAINT_PROPERTY {
        id,
        title,
        description,
        clientId,
        chefId,
        dateSubmitted
    }

    /**
     * Constructor to create a new complaint instance by providing values for all instance variables
     * @param id complaint id
     * @param title title of complaint
     * @param description description of complaint
     * @param clientId id of client who submitted the complaint
     * @param chefId id of chef regarding whom complaint has been submitted
     * @param dateSubmitted date on which complaint is submitted
     */
    public Complaint(String id, String title, String description, String clientId, String chefId, Date dateSubmitted) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setClientId(clientId);
        this.setChefId(chefId);
        this.setDateSubmitted(dateSubmitted);
        this.setResolved(false);
    }

    public Complaint(String id, String title, String description, String clientId, String chefId, String dateSubmitted) throws ParseException {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setClientId(clientId);
        this.setChefId(chefId);
        this.setDateSubmitted(dateSubmitted);
        this.setResolved(false);
    }

    /**
     * Constructor to create a new complaint instance by providing ComplaintEntityModel
     * @param complaintData ComplaintEntityModel containing unvalidated date
     */
    public Complaint(ComplaintEntityModel complaintData) throws ParseException {
//        setId(complaintData.getId());
        this.setTitle(complaintData.getTitle());
        this.setDescription(complaintData.getDescription());
        this.setClientId(complaintData.getClientId());
        this.setChefId(complaintData.getChefId());
        // set the date submitted, receives value as string, throws ParseException if format is incorrect
        this.setDateSubmitted(complaintData.getDateSubmitted());
        this.setResolved(false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        // Process: validating the title
        if (validateName(title) == true) { //valid

            this.title = title; //setting title

        }
        else { //invalid

            // Output: error message
            throw new IllegalArgumentException(errorMsg);

        }

    }

    /**
     * this helper method validates the title and checks that it's not empty
     * @param title the title of the complaint
     * @return whether the title is valid or not
     */
    private boolean validateName(String title) {

        // Process: checking title length
        if (title.length() > 0) { //at least 1 char
            
            if (title.length() > 50) { //too long

                errorMsg = "Please limit the title to 50 characters"; //updating error msg

                return false;

            }

            return true;

        }
        else { //nothing inputted

            errorMsg = "Complaint must be titled"; //updating error msg

            return false;

        }

    }

    public String getDescription() {
        return description;
    }

    /**
     * Set/Change the description of the complaint
     * @param description of complaint
     */
    public void setDescription(String description) {

        if (description.length() >= 20) { //valid

            this.description = description;

        }
        else { //too short or nothing inputted

            throw new IllegalArgumentException("Description should be at least 20 characters long");

        }

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    private void setDateSubmitted(String dateSubmitted) throws ParseException {
        // mm-dd--yyyy
        // this.dateSubmitted = Utilities.getDateFromString(dateSubmitted);
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public Map<String, Object> getComplaintDataMap() {
        HashMap<String, Object> complaintDataMap = new HashMap<>();
        complaintDataMap.put(COMPLAINT_PROPERTY.title.toString(), this.title);
        complaintDataMap.put(COMPLAINT_PROPERTY.description.toString(), this.description);
        complaintDataMap.put(COMPLAINT_PROPERTY.clientId.toString(), this.clientId);
        complaintDataMap.put(COMPLAINT_PROPERTY.chefId.toString(), this.chefId);
        complaintDataMap.put(COMPLAINT_PROPERTY.dateSubmitted.toString(), this.dateSubmitted);
        return complaintDataMap;
    }

    /**
     * Allows complaint instances to be sorted by date submitted
     * @return 0 if submitted on same date, -1 is complaint1 was submitted earlier, 1 if complaint1 was submitted later
     */
    @Override
    public int compare(Complaint complaint1, Complaint complaint2) {
        return complaint2.getDateSubmitted().compareTo(complaint1.getDateSubmitted());
    }
}
