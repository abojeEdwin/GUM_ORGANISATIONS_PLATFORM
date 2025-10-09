package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.EmailWorker_Status;
import com.EnumDayTask.data.Enum.Invite_Status;
import com.EnumDayTask.data.Enum.Plan_Limit;
import com.EnumDayTask.data.Enum.UserRole;
import com.EnumDayTask.data.model.*;
import com.EnumDayTask.data.repositories.*;
import com.EnumDayTask.dto.request.*;
import com.EnumDayTask.dto.response.AcceptInviteResponse;
import com.EnumDayTask.dto.response.InviteManagerResponse;
import com.EnumDayTask.dto.response.InviteMemberResponse;
import com.EnumDayTask.exception.*;
import com.EnumDayTask.util.AppUtils;
import com.EnumDayTask.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.EnumDayTask.util.AppUtils.*;


@Service
public class OrgInitiativeServiceImpl implements  OrgInitiativeService{

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private OrganisationRepo organisationRepo;
    @Autowired
    private OrganisationProfileRepo organisationProfileRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private InviteRepo inviteRepo;
    @Autowired
    private EmailOutboxRepo emailOutboxRepo;

    @Override
    public InviteManagerResponse inviteManager(InviteManagerRequest request) {
        Admin admin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        if(managerRepo.existsByEmail(request.getEmail())){throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);}
        Organisation foundOrganisation = organisationRepo.findByAdminId(request.getAdminId());
        if(foundOrganisation == null){throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);}

        long currentMembers = managerRepo.countByAdmin(admin) + memberRepo.countByAdmin(admin);
        Plan_Limit plan = foundOrganisation.getPlanLimit();

        boolean limitExceeded = false;
        if (plan == Plan_Limit.FREE && currentMembers >= 5) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.PRO && currentMembers >= 50) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.UNLIMITED){
            limitExceeded = false;
        }
        if (limitExceeded) {
            throw new LIMIT_EXCEEDED(LIMIT_EXCEEDED_FOR_PLAN);
        }

        Manager newManager = new Manager();
        newManager.setEmail(request.getEmail());
        newManager.setStatus(Invite_Status.PENDING);
        newManager.setRole(UserRole.MANAGER);
        newManager.setAdmin(admin);
        Manager savedManager = managerRepo.save(newManager);

        String token = jwtUtil.generateToken(savedManager);

        Invite newInvite  = new Invite();
        newInvite.setOrg_id(foundOrganisation.getId());
        newInvite.setEmail(request.getEmail());
        newInvite.setRole(UserRole.MANAGER);
        newInvite.setToken(token);
        newInvite.setUsed(false);
        Invite savedInvite = inviteRepo.save(newInvite);

        Email_Outbox newEmail = new Email_Outbox();
        newEmail.setEmail_status(EmailWorker_Status.PENDING);
        newEmail.setRecipient(savedInvite.getEmail());
        newEmail.setSender(admin.getEmail());
        newEmail.setSubject("Invite Manager");
        newEmail.setBody(savedInvite.getToken());
        newEmail.setCreatedAt(LocalDateTime.now());
        Email_Outbox savedEmail = emailOutboxRepo.save(newEmail);
        return new InviteManagerResponse(savedInvite.getToken(),savedManager.getEmail(),admin.getId());

    }

    @Override
    public InviteMemberResponse inviteMember(InviteMemberRequest request) {
        Admin foundAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        if(memberRepo.existsByEmail(request.getEmail())){throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);}
        Organisation foundOrganisation = organisationRepo.findByAdminId(request.getAdminId());
        if(foundOrganisation == null){throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);}

        long currentMembers = managerRepo.countByAdmin(foundAdmin) + memberRepo.countByAdmin(foundAdmin);
        Plan_Limit plan = foundOrganisation.getPlanLimit();

        boolean limitExceeded = false;
        if (plan == Plan_Limit.FREE && currentMembers >= 5) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.PRO && currentMembers >= 50) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.UNLIMITED){
            limitExceeded = false;
        }
        if (limitExceeded) {
            throw new LIMIT_EXCEEDED(LIMIT_EXCEEDED_FOR_PLAN);
        }

        Member newMember = new Member();
        newMember.setEmail(request.getEmail());
        newMember.setStatus(Invite_Status.PENDING);
        newMember.setRole(UserRole.MEMBER);
        newMember.setAdmin(foundAdmin);
        Member savedMember = memberRepo.save(newMember);

        String inviteToken = jwtUtil.generateToken(savedMember);

        Invite newInvite  = new Invite();
        newInvite.setOrg_id(foundOrganisation.getId());
        newInvite.setEmail(request.getEmail());
        newInvite.setRole(UserRole.MEMBER);
        newInvite.setToken(inviteToken);
        newInvite.setUsed(false);
        Invite savedInvite = inviteRepo.save(newInvite);

        Email_Outbox newEmail = new Email_Outbox();
        newEmail.setEmail_status(EmailWorker_Status.PENDING);
        newEmail.setRecipient(savedInvite.getEmail());
        newEmail.setSender(foundAdmin.getEmail());
        newEmail.setSubject("Invite Manager");
        newEmail.setBody(savedInvite.getToken());
        newEmail.setCreatedAt(LocalDateTime.now());
        Email_Outbox savedEmail = emailOutboxRepo.save(newEmail);
        return new InviteMemberResponse(savedInvite.getToken(),savedMember.getEmail(),foundAdmin.getId());
    }

    @Override
    public AcceptInviteResponse acceptInvite(AcceptInviteRequest request) {

        if (!jwtUtil.isTokenExpired(request.getToken())) {
            throw new TOKEN_EXPIRED(TOKEN_INVALID);
        }
        Invite invite = inviteRepo.findByToken(request.getToken())
                .orElseThrow(() -> new TOKEN_INVALID(TOKEN_INVALID));
        if (invite.getExpires_at() != null && invite.getExpires_at().isBefore(LocalDateTime.now())) {
            throw new INVITE_EXPIRED(INVITE_ALREADY_EXPIRED);}
        if (invite.isUsed()) {throw new TOKEN_ALREADY_USED(TOKEN_ALREADY_USED);}
        if (managerRepo.existsByEmail(invite.getEmail()) || memberRepo.existsByEmail(invite.getEmail())) {
            throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);}

        Organisation org = organisationRepo.findById(invite.getOrg_id())
                .orElseThrow(() -> new ORG_NOT_FOUND(ORGANISATION_NOT_FOUND));

        if (invite.getRole() == UserRole.MANAGER) {
            Manager manager = managerRepo.findByEmail(invite.getEmail())
                    .orElseThrow(() -> new MANAGER_NOT_FOUND(MANAGER_NOT_FOUND));
            manager.setPassword(AppUtils.hashPassword(request.getPassword()));
            manager.setStatus(Invite_Status.ACTIVE);
            managerRepo.save(manager);
        } else if (invite.getRole() == UserRole.MEMBER) {
            Member member = memberRepo.findByEmail(invite.getEmail())
                    .orElseThrow(() -> new ENTITY_NOT_FOUND("Pending member not found"));
            member.setPassword(passwordEncoder.encode(request.getPassword()));
            member.setStatus(Invite_Status.ACTIVE);
            memberRepo.save(member);
        }

        // 8. Mark invite as used
        invite.setUsed(true);
        invite.setAcceptedAt(LocalDateTime.now());
        inviteRepo.save(invite);

        // 9. Return response
        return new AcceptInviteResponse(
                invite.getEmail(),
                invite.getRole(),
                "Invite accepted successfully"
        );
    }

    @Override
    public Program createProgram(CreateProgramRequest request) {
        return null;
    }

    @Override
    public Program updateProgram(UpdateProgramRequest request) {
        return null;
    }

    @Override
    public Program archiveProgram(ArchiveProgramRequest request) {
        return null;
    }

    @Override
    public void deleteAll() {
        adminRepo.deleteAll();
        managerRepo.deleteAll();
        memberRepo.deleteAll();
        organisationRepo.deleteAll();
        organisationProfileRepo.deleteAll();
        emailOutboxRepo.deleteAll();
        inviteRepo.deleteAll();
    }


}
