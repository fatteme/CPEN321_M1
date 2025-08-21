# Feature: User Management
This document provides a detailed description of the "User Management" feature which consists of three sub-features: 
- Sub-feature 1: Authenticate User 
- Sub-feature 2: View Profile
- Sub-feature 3: Sign Out
 
We describe each sub-feature using the following format:
- A short description of the sub-feature
- Primary actor(s)
- Success scenario(s)
- Failure scenario(s)

A success scenario is a numbered sequence of steps in the normal flow of events in the system. A failure scenario describes what can go wrong in each step of the success scenario and how this is handled. A failure scenario has the same number as its corresponding success scenario.  For example, if a failure scenario corresponds to step 3, it will be numbered 3a; the next failure scenario corresponding to step 3 will be numbered 3b, etc. 

## Sub-feature 1: Authenticate User
### Description
To access app features, a user must authenticate using External Authentication Service first. New users are automatically registered upon first authentication.

### Primary Actors
- User 
- External Authentication Service (Google Authentication Service)

### Success Scenario
1. User opens the application.
2. The system displays the authentication screen with the "Sign in with Google" button.
3. User taps the "Sign in with Google" button.
4. The system initiates Google authentication flow, prompting the user to provide their Google credentials. 
5. User completes the Google authentication process by providing their Google credentials. 
6. Google Authentication Service validates credentials and grants permission.
7. The system redirects the user to the main application screen which is blank with a profile icon at the top right.

### Failure Scenarios
- 4a. Google Authentication Service is unavailable. 
- 4a1. The system displays an error message "Authentication service temporarily unavailable. Please try again later."
- 4a2. The system automatically redirects the user back to the authentication screen.

## Sub-feature 2: View Profile
### Description
User must be able to view their profile information including their profile picture, email and name.

### Primary Actors
- User 

### Success Scenario
1. User taps on the profile icon at the top right of the main application screen.
2. The system displays the profile screen with user's profile picture, name, and email with a logout button at the bottom and a back icon at the top left.

### Failure Scenarios
- 2a. The system fails to fetch the user's profile information. 
- 2a1. The system displays an error message "Failed to fetch user information.”.
- 2a2. The system redirects User to the main application screen.


## Sub-feature 3: Sign Out 
### Description
An authenticated user can sign out. 

### Primary Actor
- User 

### Success Scenario
1. User taps on the profile icon at the top right of the main application screen.
2. User taps the “Sign Out” button on at the bottom of the screen. 
3. The system redirects the user to the sign in screen of the application.

### Failure Scenarios
None 

