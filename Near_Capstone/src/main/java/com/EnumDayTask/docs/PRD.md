# 1.PRODUCT REQUIREMENT DOCUMENT 
This PRD defines the requirements for the Authentication, Profile, Role Based Access Control (RBAC), Plans, Email Outbox, and Programs setup for Enum Organisation Platform.

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
    * Admin (Organisation)  : description, logo, industry, website.
    * Completeness calculation : 0%, 20%, 60%, 80%, 100%

# - RBAC(Role Based Access Control)
    * Admin : Invite managers and Members, edit, and delete all users; manage billing; access to global settings/features.
    * Manager : Can view members; create,update and manage programs; cannot change billing or plan, invited by Admin.
    * Member : Read only access to programs; cannot invite others; invited by Admin or Manager.
    * System : Trigger scheduled operations; email worker, verify, send notification, health checks, metric collection; update statuses automatically.

- Plans
- Email Outbox
- Programs