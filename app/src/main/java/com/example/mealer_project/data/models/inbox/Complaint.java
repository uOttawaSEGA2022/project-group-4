package com.example.mealer_project.data.models.inbox;

import com.example.mealer_project.data.entity_models.ComplaintEntityModel;

import java.util.Comparator;
import java.util.Date;

/**
 * Complaint class to create instances of complaints which will be stored in an inbox
 * Implements Comparator to enable sorting of complaints by date submitted
 */
public class Complaint implements Comparator<Complaint> {
    // instance variables
    private String id;
    private String title;
    private String description;
    private String clientId;
    private String chefId;
    private Date dateSubmitted;

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
        this.id = id;
        this.title = title;
        this.description = description;
        this.clientId = clientId;
        this.chefId = chefId;
        this.dateSubmitted = dateSubmitted;
    }

    /**
     * Constructor to create a new complaint instance by providing ComplaintEntityModel
     * @param complaintData ComplaintEntityModel containing unvalidated date
     * @param complaintDate Date object representing the date on which complaint was submitted
     */
    public Complaint(ComplaintEntityModel complaintData, Date complaintDate) {
        setId(complaintData.getId());
        setTitle(complaintData.getTitle());
        setDescription(complaintData.getDescription());
        setClientId(complaintData.getClientId());
        setChefId(complaintData.getChefId());
        setDateSubmitted(complaintDate);
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

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    /**
     * Allows complaint instances to be sorted by date submitted
     * @return 0 if submitted on same date, -1 is complaint1 was submitted earlier, 1 if complaint1 was submitted later
     */
    @Override
    public int compare(Complaint complaint1, Complaint complaint2) {
        return complaint1.getDateSubmitted().compareTo(complaint2.dateSubmitted);
    }
}
