package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.*;
import com.EnumDayTask.data.model.*;
import com.EnumDayTask.dto.request.*;
import com.EnumDayTask.dto.response.AcceptInviteResponse;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;
import com.EnumDayTask.dto.response.InviteManagerResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class OrgInitiativeServiceImplTest {

    @Autowired
    private OrgInitiativeServiceImpl orgInitiativeService;
    @Autowired
    private AdminAuthServiceImpl adminAuthService;
    @Autowired
    private OrganisationProfileServiceImpl organisationProfileService;

    @BeforeEach
    void setUp() {
        orgInitiativeService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        orgInitiativeService.deleteAll();
    }


    @Test
    public void testAdminInviteManager(){
        AdminSignupReq admin = new AdminSignupReq();
        admin.setEmail("abojeedwin@gmail.com");
        admin.setPassword("SecurePassword123!");
        AdminSignupRes res = adminAuthService.signup(admin);
        assertNotNull(res);
        assertEquals(res.getMessage(), "Account created successfully");

        Admin verifiedAdmin = adminAuthService.verifyEmail(res.getToken());
        assertNotNull(verifiedAdmin);
        assertEquals(AdminStatus.VERIFIED , verifiedAdmin.getStatus());

        AdminLoginReq loginReq = new AdminLoginReq();
        loginReq.setEmail(verifiedAdmin.getEmail());
        loginReq.setPassword("SecurePassword123!");
        AdminLoginRes response = adminAuthService.login(loginReq);
        assertEquals(response.getMessage(), "Login successful");
        assertNotNull(response);
        assertNotNull(response.getAccessToken());

        CreateOrganisationReq organisation = new CreateOrganisationReq();
        organisation.setName("Semicolon");
        organisation.setPlanLimit(Plan_Limit.FREE);
        organisation.setAdminId(verifiedAdmin.getId());
        Organisation orgResponse = organisationProfileService.createOrganisation(organisation);
        assertNotNull(orgResponse);
        assertEquals(orgResponse.getName(), "Semicolon");
        assertEquals(orgResponse.getPlanLimit(), Plan_Limit.FREE);
        assertEquals(orgResponse.getAdmin().getId(), verifiedAdmin.getId());

        InviteManagerRequest newManager = new InviteManagerRequest();
        newManager.setEmail("abojeedwin@gmail.com");
        newManager.setAdminId(verifiedAdmin.getId());
        InviteManagerResponse savedNewManager = orgInitiativeService.inviteManager(newManager);
        assertNotNull(savedNewManager);

    }

    @Test
    public void testManagerCanAcceptInviteRequest(){
        AdminSignupReq admin = new AdminSignupReq();
        admin.setEmail("abojeedwin@gmail.com");
        admin.setPassword("SecurePassword123!");
        AdminSignupRes res = adminAuthService.signup(admin);
        assertNotNull(res);
        assertEquals(res.getMessage(), "Account created successfully");

        Admin verifiedAdmin = adminAuthService.verifyEmail(res.getToken());
        assertNotNull(verifiedAdmin);
        assertEquals(AdminStatus.VERIFIED , verifiedAdmin.getStatus());

        AdminLoginReq loginReq = new AdminLoginReq();
        loginReq.setEmail(verifiedAdmin.getEmail());
        loginReq.setPassword("SecurePassword123!");
        AdminLoginRes response = adminAuthService.login(loginReq);
        assertEquals(response.getMessage(), "Login successful");
        assertNotNull(response);
        assertNotNull(response.getAccessToken());

        CreateOrganisationReq organisation = new CreateOrganisationReq();
        organisation.setName("Semicolon");
        organisation.setPlanLimit(Plan_Limit.FREE);
        organisation.setAdminId(verifiedAdmin.getId());
        Organisation orgResponse = organisationProfileService.createOrganisation(organisation);
        assertNotNull(orgResponse);
        assertEquals(orgResponse.getName(), "Semicolon");
        assertEquals(orgResponse.getPlanLimit(), Plan_Limit.FREE);
        assertEquals(orgResponse.getAdmin().getId(), verifiedAdmin.getId());

        InviteManagerRequest newManager = new InviteManagerRequest();
        newManager.setEmail("abojechoko@gmail.com");
        newManager.setAdminId(verifiedAdmin.getId());
        InviteManagerResponse savedNewManager = orgInitiativeService.inviteManager(newManager);
        assertNotNull(savedNewManager);

        AcceptInviteRequest acceptInviteRequest = new AcceptInviteRequest();
        acceptInviteRequest.setPassword("SecurePassword123!");
        acceptInviteRequest.setToken(savedNewManager.getToken());
        AcceptInviteResponse acceptInviteResponse = orgInitiativeService.acceptInvite(acceptInviteRequest);
        assertEquals(acceptInviteResponse.getMessage(), "Invite accepted successfully");
        assertEquals(acceptInviteResponse.getRole(), UserRole.MANAGER);
        assertEquals(acceptInviteResponse.getEmail(), "abojechoko@gmail.com");

    }

    @Test
    public void testManagerCanCreateProgram(){
        AdminSignupReq admin = new AdminSignupReq();
        admin.setEmail("abojeedwin@gmail.com");
        admin.setPassword("SecurePassword123!");
        AdminSignupRes res = adminAuthService.signup(admin);
        assertNotNull(res);
        assertEquals(res.getMessage(), "Account created successfully");

        Admin verifiedAdmin = adminAuthService.verifyEmail(res.getToken());
        assertNotNull(verifiedAdmin);
        assertEquals(AdminStatus.VERIFIED , verifiedAdmin.getStatus());

        AdminLoginReq loginReq = new AdminLoginReq();
        loginReq.setEmail(verifiedAdmin.getEmail());
        loginReq.setPassword("SecurePassword123!");
        AdminLoginRes response = adminAuthService.login(loginReq);
        assertEquals(response.getMessage(), "Login successful");
        assertNotNull(response);
        assertNotNull(response.getAccessToken());

        CreateOrganisationReq organisation = new CreateOrganisationReq();
        organisation.setName("Semicolon");
        organisation.setPlanLimit(Plan_Limit.FREE);
        organisation.setAdminId(verifiedAdmin.getId());
        Organisation orgResponse = organisationProfileService.createOrganisation(organisation);
        assertNotNull(orgResponse);
        assertEquals(orgResponse.getName(), "Semicolon");
        assertEquals(orgResponse.getPlanLimit(), Plan_Limit.FREE);
        assertEquals(orgResponse.getAdmin().getId(), verifiedAdmin.getId());

        InviteManagerRequest newManager = new InviteManagerRequest();
        newManager.setEmail("abojechoko@gmail.com");
        newManager.setAdminId(verifiedAdmin.getId());
        InviteManagerResponse savedNewManager = orgInitiativeService.inviteManager(newManager);
        assertNotNull(savedNewManager);

        AcceptInviteRequest acceptInviteRequest = new AcceptInviteRequest();
        acceptInviteRequest.setPassword("SecurePassword123!");
        acceptInviteRequest.setToken(savedNewManager.getToken());
        AcceptInviteResponse acceptInviteResponse = orgInitiativeService.acceptInvite(acceptInviteRequest);
        assertEquals(acceptInviteResponse.getMessage(), "Invite accepted successfully");
        assertEquals(acceptInviteResponse.getRole(), UserRole.MANAGER);
        assertEquals(acceptInviteResponse.getEmail(), "abojechoko@gmail.com");


        CreateProgramRequest newProgram = new CreateProgramRequest();
        newProgram.setAdminId(verifiedAdmin.getId());
        newProgram.setOrganisationId(orgResponse.getId());
        newProgram.setDescription("Semicolon");
        newProgram.setTitle("Making leaders");
        Program savedProgram = orgInitiativeService.createProgram(newProgram);
        assertNotNull(savedProgram);
        assertEquals(savedProgram.getStatus(), ProgramStatus.ACTIVE);

    }
}