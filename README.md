# Mealer App

An extraordinary food ordering app providing a one-stop-shop solution for clients to order their favorite meals from exceptional chefs near them. 🌯
Being built as part of course SEG2105 at University of Ottawa by SEG2105 Group 4.

**Test Accounts:**

Admin: madmin@mealer.com, Seg@2105

Client: kapil@sharma.com, Ottawa@123

Chef: onedirection@life.never, Ottawa@123

## Application Architecture

**General Details**

- Programming Language(s): Java
- Database: Cloud Firestore (Firebase)
- External Dependencies: None
- APIs: None

### Project File Structure & Code Organization

    app
    |-- main
    |     |-- App
    |     |-- AppInstance
    |
    |-- data
    |     |-- entities_models
    |             |-- UserEntityModel, ClientEntityModel, creditCardEntityModel
    |     |-- models
    |             |-- Admin, User, Client, Chef, Address, CreditCard, Meal, UserRoles
    |     |-- handlers
    |             |-- DataHandlers, UserDataHandler
    |     |-- sources
    |             |-- FirebaseRepository
    |
    |-- ui
    |     |-- IntroScreen
    |     |-- LoginScreen
    |     |-- SignupScreen
    |     |-- VoidChequeScreen
    |     |-- WelcomeScreen
    |
    |-- utils
    |     |-- Response
    |     |-- Result
    |
    |-- MainActivity.java

### Logic Execution Strategy

**Problem Statement**: Multiple entities or objects of data representation (User, Client, Chef, Meal, etc.) have to be accessable and modifiable from different parts of the application, and changes in data should be represented on the user interface efficiently.

**Adopted Solution**: By taking inspiration from Chain-of-responsibility design pattern, Modal-View-Controller design pattern and the Observer pattern, we resorted to implement a code structure and data handling pattern that helps achieve loose coupling, lesser dependency on external frameworks (like Firebase) and more modularization.

### High-Level Representation of Application Strategy

![Mealer Project Application Strategy](https://docs.google.com/drawings/d/e/2PACX-1vSjb7Dh6yt7NINm1oyG4JZqcJKNNaZ4Ek4Mgypyj7lVFRqYiH1aKTMrRxHELmo_hCV3tgwwdJrA8GKf/pub?w=1440&h=1080)

Every app instance would have UI components (activities), which will ONLY contain code needed for user interface. UI components, use the App instance, to make calls to appropriate data handlers, when it needs to work with internal or external app data. Data handlers, act as intermediary between the UI and the database. Data handlers also handle data validation, creation of instance variables of data models, and the management of side effects (ex: call to suspend a chef, may require data changes at multiple places). Firebase repository contains all Firebase-specific code, i.e., methods and dependencies that are specific to Firebase.

This level of abstraction provides the freedom where the software owner can decide to move on to a different database provider (or add more providers), without the need for a large number of code changes within the internal application logic (especially, multiple UI components).

In a traditional android app, services or frameworks like Firebase are very tightly coupled into the application logic, for example, firebase methods are executed at multiple places within multiple UI activities. Moving away from a particular framework for any reason in such an application has multiple barriers like technical complexity and resource intensiveness.

### Data Strategy

Each of the application's primary data objects has a model stored in the _models_ folder, which represents the attributes and behaviour of the entity.

_entities_models_ are helper classes which provide a representation of corresponding entity object, without any logic or functionality that the entity object might implement. They're used to store unvalidated information of an entity and to facilitate easier transfer of such information through application. This information then could be used to instantiate an object of the corresponding entity object.

Example: The model User has a UserEntityModel. UserEntityModel is used to store unvalidated user information, which could later be used to instantiate a User object. If instantiation fails, we always have access to the user information from the EntityModel.


## Creaters, Designers, Artists, Developers...

SEG2105 Group 4:

- Justin Wang (Team Lead)
- Anjali Mohammed
- Amy Huang
- Kristen Duong
- Rahul Atre
- Pranav Kural
