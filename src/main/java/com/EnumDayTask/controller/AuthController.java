package com.EnumDayTask.controller;


import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.dto.request.AdminLoginReq;
import com.EnumDayTask.dto.request.AdminSignupReq;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;
import com.EnumDayTask.service.AdminAuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/v1/auth")
@RestController
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AdminAuthServiceImpl adminAuthService;

    @PostMapping("/signup")
    public ResponseEntity<AdminSignupRes> signup (AdminSignupReq request){
        return ResponseEntity.ok(adminAuthService.signup(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Admin> verifyEmail (String token){
        return ResponseEntity.ok(adminAuthService.verifyEmail(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<AdminLoginRes> login (AdminLoginReq request){
        return ResponseEntity.ok(adminAuthService.login(request));
    }

    @PostMapping("/logout")
    public void logout (String token){
        adminAuthService.logout(token);
    }

}
