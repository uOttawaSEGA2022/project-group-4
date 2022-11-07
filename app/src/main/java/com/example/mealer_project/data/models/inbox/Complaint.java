package com.example.mealer_project.data.models.inbox;

import com.example.mealer_project.data.entity_models.ComplaintEntityModel;

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
    };

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
        setId(id);
        setTitle(title);
        setDescription(description);
        setClientId(clientId);
        setChefId(chefId);
        setDateSubmitted(dateSubmitted);
        setResolved(false);
    }

    public Complaint(String id, String title, String description, String clientId, String chefId, String dateSubmitted) throws ParseException {
        setId(id);
        setTitle(title);
        setDescription(description);
        setClientId(clientId);
        setChefId(chefId);
        setDateSubmitted(dateSubmitted);
        setResolved(false);
    }

    /**
     * Constructor to create a new complaint instance by providing ComplaintEntityModel
     * @param complaintData ComplaintEntityModel containing unvalidated date
     */
    public Complaint(ComplaintEntityModel complaintData) throws ParseException {
//        setId(complaintData.getId());
        setTitle(complaintData.getTitle());
        setDescription(complaintData.getDescription());
        setClientId(complaintData.getClientId());
        setChefId(complaintData.getChefId());
        // set the date submitted, receives value as string, throws ParseException if format is incorrect
        setDateSubmitted(complaintData.getDateSubmitted());
        setResolved(false);
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
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        this.dateSubmitted = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(dateSubmitted);
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
        return complaint1.getDateSubmitted().compareTo(complaint2.getDateSubmitted());
    }
}
