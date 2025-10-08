package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.AdminStatus;
import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.dto.request.AdminLoginReq;
import com.EnumDayTask.dto.request.AdminSignupReq;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AdminAuthServiceImplTest {

    @Autowired
    private AdminAuthServiceImpl adminAuthService;


    @BeforeEach
    void setUp() {
        adminAuthService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        adminAuthService.deleteAll();
    }

    @Test
    public void testAdminCanSignUpWithValidCredentials(){
        AdminSignupReq admin = new AdminSignupReq();
        admin.setEmail("abojeedwin@gmail.com");
        admin.setPassword("SecurePassword123!");
        AdminSignupRes res = adminAuthService.signup(admin);
        assertNotNull(res);
        assertEquals(res.getMessage(), "Account created successfully");

    }

    @Test
    public void testAdminCanVerifyEmail(){
        AdminSignupReq admin = new AdminSignupReq();
        admin.setEmail("abojeedwin@gmail.com");
        admin.setPassword("SecurePassword123!");
        AdminSignupRes res = adminAuthService.signup(admin);
        assertNotNull(res);
        assertEquals(res.getMessage(), "Account created successfully");
        Admin verifiedAdmin = adminAuthService.verifyEmail(res.getToken());
        assertNotNull(verifiedAdmin);
        assertEquals(AdminStatus.VERIFIED , verifiedAdmin.getStatus());
    }

    @Test
    public void testAdminCanLoginSafely(){
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
        assertNotNull(response.getToken());
    }



}