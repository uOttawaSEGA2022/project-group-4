# Mealer App

An extraordinary food ordering app providing a one-stop-shop solution for clients to order their favorite meals from exceptional chefs near them. 🌯
Being built as part of course SEG2105 at University of Ottawa by SEG2105 Group 4.

**Test Accounts:**
O
Admin: madmin@mealer.com, Seg@2105
Client: kapil@sharma.com, Ottawa@123
Chef: onedirection@life.never, Ottawa@123

## Table of contents:

- [Mealer App](#mealer-app)
  - [Application Architecture](#application-architecture)
    - [Project File Structure & Code Organization](#project-file-structure--code-organization)
    - [Application Architecture Strategy](#application-architecture-strategy)
    - [High-Level Representation of Application Strategy](#high-level-representation-of-application-strategy)
    - [Data Strategy](#data-strategy)
    - [Dispatcher-Action-Handler Pattern](#dispatcher-action-handler-pattern)
  - [Creaters, Designers, Artists, Developers](#creaters-designers-artists-developers)

## Application Architecture

**General Details**

- Programming Language(s): Java
- Database: Cloud Firestore (Firebase)
- External Dependencies: None
- APIs: None

### Project File Structure & Code Organization

    app
    |-- main
    |     |-- App           [instantiates an app instance & provide access to app-level data]
    |     |-- AppInstance   [initializing the required app classes - database, handlers etc.]
    |                       [also stored the current user and other app-level data]
    |
    |-- data
    |     |-- entity_models [contains classes to store unvalidated data related to a model]
    |     |-- models        [contains classes representing data models]
    |     |-- handlers      [handlers for working with data locally and remotely]
    |     |-- sources       [app's remote data sources, like Firebase]
    |         |-- actions   [actions to work with remote data of a model, like MealActions]
    |
    |-- ui                  [all UI components]
    |   |
    |   |-- core            [non-UI components facilitating UI components]
    |   |                   [contains interfaces like StatefulView]
    |   |
    |   |-- screens         [all UI screens]
    |
    |-- utils
    |     |-- Response      [when need to indicate success or failure & return error message]
    |     |-- Result        [when need to return an object on both success or failure]
    |     |-- Preconditions [set of specialized utility methods for verifying preconditions]
    |     |-- Utilities     [Utilities used across app]
    |
    |-- MainActivity.java   [Entry point]

### Application Architecture Strategy

**Problem Statement**: Multiple entities or objects of data representation (User, Client, Chef, Meal, etc.) have to be accessable and modifiable from different parts of the application, and changes in data should be represented on the user interface efficiently.

**Adopted Solution**: By taking inspiration from Chain-of-responsibility design pattern, Modal-View-Controller design pattern and the Dispatcher-Action-Handler pattern, we resorted to implement a code structure and data handling pattern that helps achieve loose coupling, lesser dependency on external frameworks (like Firebase) and more modularization.

### High-Level Representation of Application Strategy

<img src="https://docs.google.com/drawings/d/e/2PACX-1vSjb7Dh6yt7NINm1oyG4JZqcJKNNaZ4Ek4Mgypyj7lVFRqYiH1aKTMrRxHELmo_hCV3tgwwdJrA8GKf/pub?w=960&amp;h=720" alt="Mealer Project Application Strategy" width="500" />

Every app instance would have UI components (activities), which will ONLY contain code needed for user interface. UI components, use the App instance, to make calls to appropriate **data handlers**, when it needs to work with internal or external app data. Data handlers, act as **intermediary between the UI and the database**. Data handlers also handle data validation, creation of instance variables of data models, and the management of side effects (ex: call to suspend a chef, may require data changes at multiple places).

Data handlers futher interact with remote data through **Action handlers**. Firebase repository contains all Firebase-specific code, i.e., methods and dependencies that are specific to Firebase. It also initializes action handlers for the models whose data is stored remotely (example: MealHandler for working with remote meals data, InboxHandler for working with Admin's inbox data, and so on).

This level of abstraction provides the freedom where the software owner can decide to move on to a different database provider (or add more providers), without the need for a large number of code changes within the internal application logic (especially, multiple UI components).

In a traditional android app, services or frameworks like Firebase are very tightly coupled into the application logic, for example, firebase methods are executed at multiple places within multiple UI activities. Moving away from a particular framework for any reason in such an application has multiple barriers like technical complexity and resource intensiveness.

### Data Strategy

Each of the application's primary data objects has a model stored in the _models_ folder, which represents the attributes and behaviour of the entity.

_entity_models_ are helper classes which provide a representation of corresponding entity object, without any logic or functionality that the entity object might implement. They're used to store unvalidated information of an entity and to facilitate easier transfer of such information through application. This information then could be used to instantiate an object of the corresponding entity object.

Example: The model User has a UserEntityModel. UserEntityModel is used to store unvalidated user information, which could later be used to instantiate a User object. If instantiation fails, we always have access to the user information from the EntityModel.

### Dispatcher-Action-Handler Pattern

Dispatch Action Handler (DAH) Pattern is a custom pattern we implemented inspired from state management technologies used in web applications.

**Problem statements:**

1. Each data handler provides for execution handling of a multitude of operations related to one or more model. A lot of these methods have to do similar precondition checks, and some do not perform execution of code more than a couple of lines. This leads to repetition of code and unnecessary extension of code files of handlers, making debugging and code management and review more difficult.
2. Handle disprepencies between local and remote data. We want to avoid a situation where remote data fails to update but local app data is updated, so user is unaware of unsaved and unprocessed changes, which could have huge consequences for the user.

**Solution:** _Dispatch-Action-Handler Pattern_

A Data handler defined (publically accessible) list of database operations it performs (ex: public enum). It also has to define three methods: **dispatch** (for handling initial call for an action), **handleActionSuccess** (for handling success response of an operation from Action handler), and **handleActionFailure** (for handling failure response of an operation from Action handler).

Any UI component which needs to execute a data modification operation, calls the appropriate data handler's **dispatch** method, providing it first: **type of operation** (which it defines using list of allowed operations publically exposed by the data handler), second: **payload** (which is any data that it needs to provide as input for successful execution of the action), third: **this** (pass its own instance given that the UI screen making call already implements interface **StatefulView**, so it can be informed of success or failure).

**StatefulView** interface defined methods _dbOperationSuccessHandler_ & _dbOperationFailureHandler_ which every UI component making calls for data updates (especially remote data) needs to implement. Such a pattern of callbacks is required to integrate our application with Firebase's non-blocking asynchoronous code execution strategy.

**Actions handler** executes the database operation, and calls the **handleActionSuccess** of _data handler_ which made the call to the action (data handler is globally accessible, doesn't have to pass its instance down), passing it first: **type of operation** for which it is a success, and **payload** (which is any result of the operation). If there is a failure, it calls **handleActionFailure** instead with type of opertion which failed, and an error message.

**handleActionSuccess** method of the data handler implements the logic to solve our second problem, i.e., once data has been updated remotely, only then _handleActionSuccess_ gets called with appropriate operation type and payload (data), based on which then local data is updated. Hence, we prioritize the update of remote data (considering it to be the **single source of truth (SSOT)**)

Below images display execution cycle when using DAH pattern.

Dispatch Action Call Execution:

<img src="https://docs.google.com/drawings/d/e/2PACX-1vQLi-nnOXPIEWBanxijBuXaaJjW-TKR8-DiG6xtLyRztvnZyNbVe41pwLobkEO6SMuUlt-oVcit1RWm/pub?w=1440&amp;h=1080" alt="Dispatch Action Call Execution" width="500"/>

Dispatch Action Response Handling:

<img src="https://docs.google.com/drawings/d/e/2PACX-1vRpGCc2T-jvPLcs52VotdXkJwOT-ku7l0sT6xLC_hlJg7QUL-9xXpe8W1NiBTd1UcRl3AGTyxGFk3pQ/pub?w=1440&amp;h=1080" alt="Dispatch Action Response Handling" width="500">

## Creaters, Designers, Artists, Developers

SEG2105 Group 4:

- Justin Wang (Team Lead) 🤖
- Anjali Mohammed 💜
- Amy Huang 🤸‍♀️
- Kristen Duong 🤡
- Rahul Atre 👀
- Pranav Kural 🚀
