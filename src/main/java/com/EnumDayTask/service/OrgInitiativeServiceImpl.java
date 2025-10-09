package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.Invite_Status;
import com.EnumDayTask.data.Enum.Plan_Limit;
import com.EnumDayTask.data.Enum.UserRole;
import com.EnumDayTask.data.model.*;
import com.EnumDayTask.data.repositories.*;
import com.EnumDayTask.dto.request.ArchiveProgramRequest;
import com.EnumDayTask.dto.request.CreateProgramRequest;
import com.EnumDayTask.dto.request.InviteManagerRequest;
import com.EnumDayTask.dto.request.UpdateProgramRequest;
import com.EnumDayTask.exception.ADMIN_NOT_FOUND;
import com.EnumDayTask.exception.EMAIL_IN_USE;
import com.EnumDayTask.exception.INVALID_CREDENTIAL;
import com.EnumDayTask.exception.LIMIT_EXCEEDED;
import com.EnumDayTask.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Manager inviteManager(InviteManagerRequest request) {
        Admin admin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        Manager manager = managerRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));
        if(manager.getEmail().equals(request.getEmail())){throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);}

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

        String token = jwtUtil.

        Invite newInvite  = new Invite();
        newInvite.setOrg_id(foundOrganisation.getId());
        newInvite.setEmail(request.getEmail());
        newInvite.setRole(UserRole.MANAGER);
        newInvite.set
        return managerRepo.save(newManager);
    }

    @Override
    public Member inviteMember(String email) {
        return null;
    }

    @Override
    public void acceptInvite(String token) {

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
    }


}
