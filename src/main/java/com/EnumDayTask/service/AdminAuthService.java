package com.EnumDayTask.service;


import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.dto.request.AdminLoginReq;
import com.EnumDayTask.dto.request.AdminSignupReq;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;

public interface AdminAuthService {

    AdminSignupRes signup(AdminSignupReq createAccountReq);
    Admin verifyEmail(String token);
    AdminLoginRes login(AdminLoginReq request);
    void logout(String token);
    void deleteAll();

}
