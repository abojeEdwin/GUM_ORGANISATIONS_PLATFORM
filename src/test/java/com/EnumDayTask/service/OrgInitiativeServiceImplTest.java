package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.AdminStatus;
import com.EnumDayTask.data.Enum.Invite_Status;
import com.EnumDayTask.data.Enum.Plan_Limit;
import com.EnumDayTask.data.Enum.UserRole;
import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.data.model.Invite;
import com.EnumDayTask.data.model.Manager;
import com.EnumDayTask.data.model.Organisation;
import com.EnumDayTask.dto.request.AdminLoginReq;
import com.EnumDayTask.dto.request.AdminSignupReq;
import com.EnumDayTask.dto.request.CreateOrganisationReq;
import com.EnumDayTask.dto.request.InviteManagerRequest;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;
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
        newManager.setEmail("newManager@email.com");
        newManager.setAdminId(verifiedAdmin.getId());
        Manager savedNewManager = orgInitiativeService.inviteManager(newManager);
        assertNotNull(savedNewManager);
    }
}