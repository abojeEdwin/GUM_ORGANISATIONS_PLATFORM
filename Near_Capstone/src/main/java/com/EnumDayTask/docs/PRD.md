# 1.PRODUCT REQUIREMENT DOCUMENT 
This PRD defines the requirements for the Authentication, Profile, Role Based Access Control (RBAC), Plans, Email Outbox, and Programs setup for Gum Organisation Platform.

# Users & Roles
- Admin
- Manager
- Member
- System

# Functional Requirements

# - Authentication
    * Signup : signup with email and password -> pending verification, token generation
    * Verify : token must be valid, single use, with expiry
    * Login : only if verified, Creates session
    * Logout : revokes current session 

# - Profile
    * Fields : description, logo, industry, website.
    * Completeness calculation : 0%, 40%, 80%, 100%
    * Update : update profile
    * View : view profile
    * Delete : delete profile

- RBAC
- Plans
- Email Outbox
- Programs