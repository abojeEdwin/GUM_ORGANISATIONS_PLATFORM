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
    * Completeness calculation : 0%, 20%, 60%, 80%, 100%

# - RBAC(Role Based Access Control)
    * Admin : Create, edit, and delete all users; view all reports; configure global settings.
    * Manager : Manage members; approve/reject tasks; view members performance.
    * Member : Create and manage own content; view limited data; request approvals.
    * System : Trigger scheduled operations; email worker, verify; update statuses automatically.

- Plans
- Email Outbox
- Programs