# 1.PRODUCT REQUIREMENT DOCUMENT 
This document defines the requirements for the Authentication, Profile, Role Based Access Control (RBAC), Plans & Limits , Email Outbox, and Programs setup for Enum Organisation Platform.

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
    * Only one profile per organsation.
    * /org/me must return profile, completeness, + missing fields.

# - RBAC(Role Based Access Control)
    * Admin : Creates Organisation, Invite managers and Members, can edit and delete all users; manage billing and plans limit ; access to global settings/features.
    * Manager : Can view members; can create,update and manage programs; cannot change billing or plan, invited by Admin.
    * Member : Read only access to programs; cannot invite others; invited by Admin or Manager.
    * System : Trigger scheduled operations; email worker, verify, send notification, health checks, metric collection; update statuses automatically.

# -Programs
    * Create Program : title, description, start date, end date, status (draft, active, completed).
    * Update Program : only by Admin and Manager.
    * List Programs : all roles can view; Members read-only.
    * Archive Program : only by Admin.
    * List Programs : paginated list with filters (status, query).


# -Plans & Limits
    * Free : 5 members, 3 programs
    * Pro : 50 members, 20 programs
    * Enterprise : Unlimited members, Unlimited programs


# -Endpoints(Minimum)
    * POST /v1/auth/signup : Signup
    * GET  /v1/auth/verify?token= : Verify email
    * POST /v1/auth/login : Login
    * POST /v1/auth/logout : Logout
    * GET /v1/org/me : Get organisation profile and completeness
    * PUT /v1/org/me : Update organisation profile
    * POST /v1/org/invite : Invite user (Admin only)
    * GET /v1/org/members : List users (Admin and Manager)
    * POST /v1/org/create-program : Create program (Admin and Manager)
    * PUT /v1/org/update-program/:id : Update program (Admin and Manager)
    * POST /v1/org/archive-program/:id : Archive program (Admin only)
    * GET /v1/org/view-programs : List programs (All roles)

# -Error Envelope
    {
        "error": {
            "code": "LIMIT_EXCEEDED",
            "message": "Plan limit exceeded",
            "details": [{"field" : "members", "limit", 5 }],
            "trace_id": "req_123"
        }
    }

    Acceptance Criteria
        * If an error happens the Error Code must be the standard format
        * Trace Id included in every error.
        * Message in plain English.

# Error Code Catalogue
    * EMAIL_IN_USE
    * EMAIL_NOT_VERIFIED
    * TOKEN_INVALID
    * TOKEN_EXPIRED
    * RATE_LIMITED
    * INVALID_ROLE
    * NOT_FOUND
    * VALIDATION_ERROR
    * RESENT_VERIFICATION_TOKEN
    * INVALID_CREDENTIALS
    * NOT_AUTHENTICATED
    * TOKEN_ALREADY_USED
    

# User Stories By Feature
    - Authentication
        Feature : Signup, Verify, Login, Logout, Refresh Token
        User Stories 
            * As an Admin of the Org, I want to be able to signup and verify my email so i can create an organisation.
            * As an Admin of the Org, I want to log in and out securely so i can manage my org safely.
            * As a User, i want refresh tokens so i can stay logged in without re-entering credentials frequently.
            
        Edge Cases
            * signup with existing email -> 409 EMAIL_IN_USE
            * signup with unverified email -> 202 RESENT_VERIFICATION_TOKEN
            * invalid/expired/used token -> 400/410 TOKEN_INVALID/TOKEN_EXPIRED
            * login before verification -> 403 EMAIL_NOT_VERIFIED
            * wrong password -> 401 INVALID_CREDENTIALS
            * rate limited -> 429 RATE_LIMITED
            * Logout Twice -> idempotent(no error)

        Acceptance Criteria
            * User cannot log in until verified
            * Duplicate signup with unverified email resends token
            * Refresh token rotation ensures reuse of old refresh fails
            * Logout always revokes active session


    - Organisation Profile
        Feature: Upsert fields(logoUrl, description, industry, website), completeness
        User Stories
            * As an Admin of the Org, I want to be able to update my profile so i can manage my org.
            * As an Admin of the Org , I want to be able to view my profile and see a completeness % and know the missing fields.

        Acceptance Criteria
            * Only one profile per organsation.
            * /org/me must return profile, completeness, + missing fields.
            * Only Admin can update profile.
            * Latest profile update overrides previous.

        Edge Cases
            * Submitting invalid website -> VALIDATION_ERROR.
            * uploading invalid logo file type -> 400 INVALID_FILE_TYPE
            * uploading profile multiple times -> always update and not duplicate    
            * no profile set yet -> /org/me returns 0% completeness and all fields missing

    - RBAC
        Features :Roles(ADMIN,MANAGER,MEMBER)
        User Stories
            * As an Admin, I want to be able to invitr members so my team can collaborate.
            * As a Manager, I want to be manage programs so i can run org initiatives.
            * As a Member, I want to be able to view/read and not write programs so i can stay updated.

        Edge Cases
            * Invite token invalid/expired -> 400/410 TOKEN_INVALID/TOKEN_EXPIRED.
            * Invite reuse after acceptance -> TOKEN_ALREADY_USED
            * accepting invite with existing email -> conflict

        Acceptance Criteria
            * Only Admin can invite members and managers.
            * Only Admin and Manager can create programs.
            * Only Admin and Manager can update programs.
            * Accepting invite activates user under correct role.
            * Only Admin can manage billing and plans.  

    - Programs
        Features: Create, Update, List, Archive , Filter by status,
        User Stories
            * As an Admin or Manager, I want to be able to create programs so i can run org initiatives.
            * As a Member, I want to be able to view programs so i can stay updated.
            * As an Admin, I want to be able to archive programs so i can keep things tidy.
        
        Acceptance Criteria
            * Programs can be created , updated and archived.
            * Query filters can be applied to list programs.
            * Error shown clearly if limits or validation errors occur.

        Edge Cases
            * archiving already archived program -> error(idempotent)
            * creating program exceeding plan limit -> 403 LIMIT_EXCEEDED
            * invalid dates -> 422 VALIDATION_ERROR



    - Plans & Limits
        Feature: Free, Pro, Enterprise
        > Free : 5 members, 3 programs
        > Pro : 50 members, 20 programs
        > Enterprise : Unlimited members, Unlimited programs

        User Stories
            * As an Admin, I want to know my plan limits so i can know when to upgrade.
    
        - Acceptance Criteria
            * Limits are enforced on member adding and program creation.
            * Response includes actionable error message if limits are exceeded.
        
        Edge Cases
            * Attempt to exceed limits -> error LIMIT_EXCEEDED.
            * Downgrading plan when usage exceeds limits -> must block limit untill reduced.



    Email Outbox
        Feature: Queue messages into DB table, email worker simulates sending emails, retries supported.
        User Stories
            * As a System, I want to be able to send emails so i can notify users.
        
        Edges Cases
            * If worker crashes mid-send -> status stays queued .
            * Retry attempts should be counted

        Acceptance Criteria
            * Every queued email either either becomes sent or failed.
            * Worker must handle retires gracefully.



    Health & Metrics
        Feature: Health checks( /.well-known/health: {status, db, redis} ), /metrics
        User Stories
            * As a Developer, i want healthcheck and metrics so  i can monitor the system.
        Acceptance Criteria
            * Health checks must always reflects dependecies.
            * Metrics must be available at /metrics
        
        Edge Cases
            * DB down -> health shows db: not_ok.
            Redis down -> health shows redis: not_ok