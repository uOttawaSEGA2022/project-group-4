package com.example.mealer_project.data.handlers;

import android.util.Log;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.List;

public class InboxHandler {
//     private InboxView inboxView;

     /**
      * Get App's admin inbox if current user has access
      * @return Result object - if operation successful, contains AdminInbox, else String containing error message
      */
     public Result<AdminInbox, String> getAdminInbox() {
          // check if current user does not have access (user is not an admin)
          if (userHasAccess().isError()) {
               return new Result<>(null, userHasAccess().getErrorMessage());
          }
          // attempt to get Admin's inbox
          try {
               return new Result<>(App.getAdminInbox(), null);
          } catch (IllegalAccessException e) {
               return new Result<>(null, "Could not retrieve admin inbox. Access denied.");
          }
     }

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

          // set inbox view
          // this.inboxView = inboxView;

          // make async call to fetch all complaints from database
          App.getPrimaryDatabase().INBOX.getAllComplaints(this);

          // return response once request to get all complaints has been submitted
          return new Response(true, "retrieving complaints for admin");
     }

     /**
      * Handles the response from async call made to obtain all complaints from database in updateAdminInbox method
      * Creates AdminInbox using the list of complaints and updates App's admin inbox
      * @param complaints list of complaints retrieved from database
      */
     public void createNewAdminInbox(List<Complaint> complaints){
          // validate complaints
          if (Preconditions.isNotEmptyList(complaints)) {
               // instantiate a new admin inbox by providing it list of complaints
               // set App's new admin inbox
               App.setAdminInbox(new AdminInbox(complaints));

               // call method in inboxView to update inbox so admin can see all complaints
               // inboxView.updateInboxUI();
          } else {
               // display error in inbox view
               // inboxView.failedToLoadComplaints("No complaints available for admin inbox");
          }
     }

     /**
      * Handle any error if it happens during retrieval of complaints from database
      * @param message error message
      */
     public void errorGettingComplaints(String message) {
          // display error on inbox view
          // inboxView.failedToLoadComplaints("Error getting complaints from database");
          Log.d("errorGettingComplaints", message );
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
