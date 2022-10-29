package com.example.mealer_project.data.handlers;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.List;

public class InboxHandler {
     /**
      * Method to update App's AdminInbox
      * Retrieves all complaints from the database and creates a new AdminInbox
      * Stores the inbox by setting AppInstance's adminInbox to this new inbox
      * @return Response object indicating success or failure
      */
     public Response updateAdminInbox() {
          // check if current user does not have access (user is not an admin)
          if (userHasAccess().isError()) {
               return userHasAccess();
          }

          // attempt to get all complaints
          Result<List<Complaint>, String> getAllComplaintsResult = getAllComplaints();
          // if getting list of all complaints failed
          if (getAllComplaintsResult.isError()) {
               // set response to be failure, and send back error message
               return new Response(false, getAllComplaintsResult.getErrorObject());
          }

          // get the list of complaints if getting complaints was successful
          List<Complaint> complaints = getAllComplaintsResult.getSuccessObject();

          try {
               // validate complaints
               if (Preconditions.isNotEmptyList(complaints)) {
                    // instantiate a new admin inbox
                    AdminInbox adminInbox = new AdminInbox(complaints.size());

                    // add all complaints to the admin inbox
                    for (Complaint complaint: complaints) {
                         adminInbox.addComplaint(complaint);
                    }

                    // set the new App's admin inbox
                    App.setAdminInbox(adminInbox);

                    // send response to indicate operation success
                    return new Response(true);
               } else {
                    throw new NullPointerException("No complaints available for admin inbox");
               }
          } catch (Exception e) {
               // indicate operation failure and return the error message
               return new Response(false, e.getMessage());
          }
     }

     /**
      * Get all complaints from database
      * @return Result object - if success, contains list of complaints, else error message as string
      */
     public Result<List<Complaint>, String> getAllComplaints() {
          // check if current user does not have access (user is not an admin)
          if (userHasAccess().isError()) {
               return new Result<>(null, userHasAccess().getErrorMessage());
          }
          // attempt to get all complaints
          try {
               return App.getPrimaryDatabase().INBOX.getAllComplaints();
          } catch (Exception e) {
               // set result to be failure, and send back error message
               return new Result<>(null, e.getMessage());
          }
     }

     /**
      * Checks if current user has access to admin only resources
      * @return Response object indicating success or failure (with error message if any)
      */
     private Response userHasAccess() {
          if (App.userIsAdmin()) {
               return new Response(true);
          } else {
               return new Response(false, "Access denied. User is not an admin.");
          }
     }
}
