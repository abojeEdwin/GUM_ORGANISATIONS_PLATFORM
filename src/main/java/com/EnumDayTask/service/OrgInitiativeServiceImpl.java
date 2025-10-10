package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.*;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private BlackListedTokenRepo blackListedTokenRepo;
    @Autowired
    private ProgramRepo programRepo;

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
        newEmail.setStatus(EmailWorker_Status.PENDING);
        newEmail.setRecipient(savedInvite.getEmail());
        newEmail.setSender(admin.getEmail());
        newEmail.setSubject("Invite Manager");
        newEmail.setBody(savedInvite.getToken());
        newEmail.setCreatedAt(LocalDateTime.now());
        Email_Outbox savedEmail = emailOutboxRepo.save(newEmail);
        return new InviteManagerResponse(savedInvite.getToken(),savedManager.getEmail(),admin.getId(),savedManager.getId());

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
        newEmail.setStatus(EmailWorker_Status.PENDING);
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

        if (jwtUtil.isTokenExpired(request.getToken())) {
            throw new TOKEN_EXPIRED(TOKEN_INVALID);
        }
        Invite invite = inviteRepo.findByToken(request.getToken())
                .orElseThrow(() -> new TOKEN_INVALID(TOKEN_INVALID));
        if (invite.getExpires_at() != null && invite.getExpires_at().isBefore(LocalDateTime.now())) {
            throw new INVITE_EXPIRED(INVITE_ALREADY_EXPIRED);}
        if (invite.isUsed()) {throw new TOKEN_ALREADY_USED(TOKEN_ALREADY_USED);}

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
                    .orElseThrow(() -> new MEMBER_NOT_FOUND(MEMBER_NOT_FOUND));
            member.setPassword(AppUtils.hashPassword(request.getPassword()));
            member.setStatus(Invite_Status.ACTIVE);
            memberRepo.save(member);
        }
        BlackListedToken blackListedToken = new BlackListedToken();
        blackListedToken.setToken(request.getToken());
        blackListedTokenRepo.save(blackListedToken);
        invite.setUsed(true);
        inviteRepo.save(invite);
        return new AcceptInviteResponse(INVITE_ACCEPTED_SUCCESSFULLY, invite.getEmail(), invite.getRole());

    }

    @Override
    @Transactional
    public Program createProgram(CreateProgramRequest request) {
        Admin foundAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        Organisation foundOrganisation = organisationRepo.findByAdminId(request.getAdminId());
        if(foundOrganisation == null){throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);}

        long numberOfPrograms = foundOrganisation.getPrograms().size();
        Plan_Limit plan = foundOrganisation.getPlanLimit();

        boolean limitExceeded = false;
        if (plan == Plan_Limit.FREE && numberOfPrograms >= 3) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.PRO && numberOfPrograms >= 20) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.UNLIMITED){
            limitExceeded = false;
        }
        if (limitExceeded) {
            throw new LIMIT_EXCEEDED(LIMIT_EXCEEDED_FOR_PLAN);
        }

        Program program = new Program();
        program.setDescription(request.getDescription());
        program.setTitle(request.getTitle());
        program.setStatus(ProgramStatus.ACTIVE);
        program.setOrganisation(foundOrganisation);
        Program savedProgram = programRepo.save(program);

        return savedProgram;
    }

    @Override
    public Program updateProgram(UpdateProgramRequest request) {
        Admin foundAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        Organisation foundOrganisation = organisationRepo.findById(request.getOrganisationId()).orElseThrow(() -> new ORG_NOT_FOUND(ORGANISATION_NOT_FOUND));
        if(foundOrganisation == null){throw new ORG_NOT_FOUND(ORGANISATION_NOT_FOUND);}
        Manager foundManager = managerRepo.findById(request.getManagerId()).orElseThrow(() -> new MANAGER_NOT_FOUND(MANAGER_NOT_FOUND));
        Program foundProgram = programRepo.findById(request.getProgramId());
        if(foundProgram == null){ throw new PROGRAM_NOT_FOUND(PROGRAM_NOT_FOUND);}

        long numberOfPrograms = foundOrganisation.getPrograms().size();
        Plan_Limit plan = foundOrganisation.getPlanLimit();

        boolean limitExceeded = false;
        if (plan == Plan_Limit.FREE && numberOfPrograms >= 3) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.PRO && numberOfPrograms >= 20) {
            limitExceeded = true;
        } else if (plan == Plan_Limit.UNLIMITED){
            limitExceeded = false;
        }
        if (limitExceeded) {
            throw new LIMIT_EXCEEDED(LIMIT_EXCEEDED_FOR_PLAN);
        }

        foundProgram.setTitle(request.getTitle());
        foundProgram.setDescription(request.getDescription());
        foundProgram.setStatus(ProgramStatus.ACTIVE);
        foundProgram.setOrganisation(foundOrganisation);
        foundProgram = programRepo.save(foundProgram);
        return foundProgram;
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
        programRepo.deleteAll();
        blackListedTokenRepo.deleteAll();
    }


}
