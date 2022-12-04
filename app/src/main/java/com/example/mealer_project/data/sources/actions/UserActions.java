package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Admin;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.screens.ComplaintScreen;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.utils.Response;

import static com.example.mealer_project.data.handlers.MealHandler.dbOperations.ADD_MEAL_TO_OFFERED_LIST;
import static com.example.mealer_project.data.sources.FirebaseCollections.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Locale;

public class UserActions {

    FirebaseFirestore database;




    public UserActions(FirebaseFirestore database) {
        this.database = database;
    }

    protected void getUserById(String userId, LoginScreen loginScreen) {
        // first check if Admin
        // then check if Client
        // then check if Chef
        DocumentReference userReference = database.collection(ADMIN_COLLECTION).document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getData() != null){
                            Response r = makeAdminFromFirebase(document, userId);
                            if (loginScreen != null && r.isSuccess()) {
                                loginScreen.showNextScreen();
                            } else if (loginScreen != null){
                                Log.e("Login failed for admin", r.getErrorMessage());
                                loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN, "Login failed for admin: " + r.getErrorMessage());
                            }

                            if (r.isError()) {
                                Log.e("getUserById: ", r.getErrorMessage());
                            }
                        }

                    } else {
                        // if user not in Admin collection, check Chef
                        getChefById(userId, loginScreen);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    protected void getChefById(String userId, LoginScreen loginScreen) {
        DocumentReference userReference = database.collection(CHEF_COLLECTION).document(userId);

        // get Chef's data
        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){

                            // if chef is suspended, we don't need to store Chef data locally

                            // get value of isSuspended for check
                            boolean isSuspended = (Boolean) document.getData().get("isSuspended");

                            Response r = makeChefFromFirebase(document);

                            // if chef is suspended, return right away with empty chef
                            if (isSuspended) {
                                // get suspension date
                                if (document.getData().get("suspensionDate") != null) {
                                    String chefSuspensionDate = String.valueOf((document.getData().get("suspensionDate")));

                                    // inform UI that Chef is suspended and provide it suspension date
                                    loginScreen.handleSuspendedChefLogin(chefSuspensionDate, String.valueOf(document.getData().get("firstName")));
                                } else {
                                    loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN,"Could not retrieve a valid date for suspended chef");
                                }
                                // return
                                new Response(false, "Chef is suspended");
                            }

                            // if Chef is not suspended, we create the Chef instance
                            else {
                                if (loginScreen != null && r.isSuccess()) {
                                    // load Chef's meals
                                    // app's user should have been set to the Chef by this point (App.getUser() = logged in chef)
                                    App.getPrimaryDatabase().ORDERS.loadChefOrders(userId);
                                    App.getPrimaryDatabase().MEALS.loadChefMeals(loginScreen);

                                } else if (loginScreen != null){
                                    Log.e("Login failed for chef", r.getErrorMessage());
                                    loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN,"Login failed, " + r.getErrorMessage());
                                }
                            }
                        }

                    } else {
                        getClientById(userId, loginScreen);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    protected void getClientById(String userId, LoginScreen loginScreen) {
        DocumentReference userReference = database.collection(CLIENT_COLLECTION).document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                            Response r = makeClientFromFirebase(document);
                            if (loginScreen != null && r.isSuccess()) {
                                App.getPrimaryDatabase().ORDERS.loadClientOrders(userId);
                                loginScreen.showNextScreen();
                            } else if (loginScreen != null){
                                Log.e("Login failed for client", r.getErrorMessage());
                                loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN,"Login failed for user: " + r.getErrorMessage());
                            }

                            if (r.isError()) {
                                Log.e("getUserById: ", r.getErrorMessage());
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private Response makeAdminFromFirebase(DocumentSnapshot document, String uid){

        try{
            if (document.getData() == null) {
                throw new NullPointerException("makeAdminFromFirebase: invalid document object");
            }

            String firstName = String.valueOf(document.getData().get("firstName"));
            String lastName = String.valueOf(document.getData().get("lastName"));
            String email = String.valueOf(document.getData().get("email"));

            App.getAppInstance().setUser(new Admin(uid, firstName, lastName, email));

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeAdminFromFirebase: " + e.getMessage());
        }
    }

    private Response makeClientFromFirebase(DocumentSnapshot document){

        try{

            if (document.getData() == null) {
                throw new NullPointerException("makeClientFromFirebase: invalid document object");
            }

            UserEntityModel newUser = new UserEntityModel();
            AddressEntityModel newAddress = new AddressEntityModel();
            CreditCardEntityModel newCreditCard = new CreditCardEntityModel();

            newUser.setFirstName(String.valueOf(document.getData().get("firstName")));
            newUser.setLastName(String.valueOf(document.getData().get("lastName")));
            newUser.setEmail(String.valueOf(document.getData().get("email")));
            newUser.setUserId(document.getId());
            newUser.setRole(UserRoles.CLIENT);

            newAddress.setStreetAddress(String.valueOf(document.getData().get("addressStreet")));
            newAddress.setCity(String.valueOf(document.getData().get("addressCity")));
            newAddress.setCountry(String.valueOf(document.getData().get("country")));
            newAddress.setPostalCode(String.valueOf(document.getData().get("postalCode")));

            newCreditCard.setBrand(String.valueOf(document.getData().get("creditCardBrand")));
            newCreditCard.setName(String.valueOf(document.getData().get("creditCardName")));
            newCreditCard.setNumber(String.valueOf(document.getData().get("creditCardNumber")));
            newCreditCard.setExpiryMonth(Integer.parseInt(document.getData().get("creditCardExpiryMonth").toString()));
            newCreditCard.setExpiryYear(Integer.parseInt(document.getData().get("creditCardExpiryYear").toString()));
            newCreditCard.setCvc(String.valueOf(document.getData().get("creditCardCvc")));

            Address address = new Address(newAddress);
            CreditCard creditCard = new CreditCard(newCreditCard);

            Client newClient = new Client(newUser, address, creditCard);

            App.getAppInstance().setUser(newClient);


            Log.e("clientS", "Set user as: " + App.getUser().getRole() + "id: " + App.getUser().getUserId());

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeClientFromFirebase: " + e.getMessage());
        }
    }

    protected Response makeChefFromFirebase(DocumentSnapshot document){

        try {

            if (document.getData() == null) {
                throw new NullPointerException("makeChefFromFirebase: invalid document object");
            }

            UserEntityModel newUser = new UserEntityModel();
            AddressEntityModel newAddress = new AddressEntityModel();

            newUser.setFirstName(String.valueOf(document.getData().get("firstName")));
            newUser.setLastName(String.valueOf(document.getData().get("lastName")));
            newUser.setEmail(String.valueOf(document.getData().get("email")));
            newUser.setUserId(document.getId());
            newUser.setRole(UserRoles.CHEF);
            newAddress.setStreetAddress(String.valueOf(document.getData().get("addressStreet")));
            newAddress.setCity(String.valueOf(document.getData().get("addressCity")));
            newAddress.setCountry(String.valueOf(document.getData().get("country")));
            newAddress.setPostalCode(String.valueOf(document.getData().get("postalCode")));

            String description = String.valueOf(document.getData().get("description"));
            String voidCheque = String.valueOf(document.getData().get("voidCheque"));

            Address address = new Address(newAddress);

            Chef newChef = new Chef(newUser, address, description, voidCheque);
            // if chef has ratings, add ratings
            Object chefRating = document.getData().get("ratingSum");
            if (chefRating != null) {
                newChef.setChefRatingSum(Double.parseDouble(String.valueOf(chefRating)));
                newChef.setNumOfRatings(Integer.parseInt(String.valueOf(document.getData().get("numOfRatings"))));
            }

            newChef.setIsSuspended((Boolean) document.getData().get("isSuspended"));

            // if suspension date is NOT null (meaning we do have a value)
            newChef.setSuspensionDate(
                    document.getData().get("suspensionDate") != null ?
                    DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(String.valueOf((document.getData().get("suspensionDate")))):
                    null);

            App.getAppInstance().setUser(newChef);

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeChefFromFirebase: " + e.getMessage());
        }
    }

    /**
     * Method changes fields (isSuspended and suspensionDate) of chef in firebase based on admin response
     * to complaint, delete meals and complaints of that chef is suspended indefinitely
     * @param chefId id of the chef associated with the complaint
     * @param isSuspended boolean whether chef is suspended
     * @param suspensionDate end date of suspension
     */

    public void updateChefSuspension(String chefId, boolean isSuspended, String suspensionDate){

        // Set the "isSuspended" field to ban boolean and the "suspensionDate" field to suspensionDate date
        database.collection(CHEF_COLLECTION).document(chefId)
                .update(
                        "isSuspended", isSuspended,
                        "suspensionDate",suspensionDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        database.collection(MEALS_COLLECTION + "/" + chefId + "/" + CHEF_MEALS_COLLECTION)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                if (suspensionDate == "01/01/9999"){ //  chef banned indefinitely

//                                                    database.collection(MEALS_COLLECTION + "/" + chefId + "/" + CHEF_MEALS_COLLECTION)  // delete all meals of this chef
//                                                            .document(document.getId())
//                                                            .delete()
//                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
//
//                                                                    Log.d("Success", "Meals successfully deleted for indefinitely banned chef!");
//                                                                }
//                                                            })
//                                                            .addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception e) {
//                                                                    Log.w("Error", "Error deleting meals for indefinitely banned chef", e);
//                                                                }
//                                                            });

                                                    database.collection("Complaints")  // delete all complaints of this chef
                                                            .whereEqualTo("chefId", chefId)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                                            database.collection("Complaints")
                                                                                    .document(document.getId())
                                                                                    .delete()
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            Log.d("Success", "Complaints successfully deleted for indefinitely banned chef!");
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w("Error", "Error deleting complaints for indefinitely banned chef", e);
                                                                                        }
                                                                                    });;
                                                                        }
                                                                    } else {
                                                                        Log.d(TAG, "Error getting complaints for chef with id: " + chefId, task.getException());
                                                                    }
                                                                }
                                                            });


                                                }
                                                else{  // chef NOT banned indefinitely so simply set meals to not offered

                                                    database.collection(MEALS_COLLECTION + "/" + chefId + "/" + CHEF_MEALS_COLLECTION)
                                                            .document(document.getId())
                                                            .update("isOffered", false)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    Log.d("Success", "Meals successfully updated to not offered!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("Error", "Error updating meals", e);
                                                                }
                                                            });
                                                }

                                            }
                                        } else {
                                            Log.e("updateChefSus", "Error getting meals from chef");

                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating chef suspension", e);
                    }
                });
    }

    public void getClientAndChefNamesByIds(String clientId, String chefId, ComplaintScreen complaintScreen) {
        // first get client name
        database
                .collection(CLIENT_COLLECTION)
                .document(clientId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.getData() != null){
                                    complaintScreen.dbOperationSuccessHandler(
                                                    ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,
                                                    document.getData().get("firstName")
                                                    + " " +
                                                    document.getData().get("lastName")
                                            );
                                } else {
                                    Log.e("getClientChefName", "document data null");
                                    complaintScreen.dbOperationFailureHandler(
                                            ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,"unable to process request");
                                }
                            } else {
                                Log.e("getClientChefName", "client not found for id: " + clientId);
                                complaintScreen.dbOperationFailureHandler(
                                        ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,"unable to process request");
                            }
                        } else {
                            Log.e(TAG, "getClientChefName failed with ", task.getException());
                        }
                    }
                });

        // first get client name
        database
                .collection(CHEF_COLLECTION)
                .document(chefId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.getData() != null){
                                    complaintScreen.dbOperationSuccessHandler(
                                            ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,
                                                    document.getData().get("firstName")
                                                            + " " +
                                                            document.getData().get("lastName")
                                            );
                                } else {
                                    Log.e("getClientChefName", "document data null");
                                    complaintScreen.dbOperationFailureHandler(
                                            ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,"unable to process request");
                                }
                            } else {
                                Log.e("getClientChefName", "chef not found for provided id: " + chefId);
                                complaintScreen.dbOperationFailureHandler(
                                        ComplaintScreen.dbOperations.GET_CLIENT_AND_CHEF_NAMES,"unable to process request");
                            }
                        } else {
                            Log.e(TAG, "getClientChefName failed with ", task.getException());
                        }
                    }
                });
    }
}
