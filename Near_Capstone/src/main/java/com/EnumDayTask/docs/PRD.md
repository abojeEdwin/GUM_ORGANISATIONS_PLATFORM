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
    * Only one profile per organsation.
    * /org/me must return profile, completeness, + missing fields.

# - RBAC(Role Based Access Control)
    * Admin : Creates Organisation ,Invite managers and Members, edit, and delete all users; manage billing and plans ; access to global settings/features.
    * Manager : Can view members; create,update and manage programs; cannot change billing or plan, invited by Admin.
    * Member : Read only access to programs; cannot invite others; invited by Admin or Manager.
    * System : Trigger scheduled operations; email worker, verify, send notification, health checks, metric collection; update statuses automatically.

# -Programs
    * Create Program : title, description, start date, end date, status (draft, active, completed).
    * Update Program : only by Admin and Manager.
    * List Programs : all roles can view; Members read-only.
    * Archive Program : only by Admin.
    * List Programs : paginated list with filters (status, query).

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

- Plans
- Email Outbox
- Programs