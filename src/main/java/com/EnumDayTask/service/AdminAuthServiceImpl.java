package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.AdminStatus;
import com.EnumDayTask.data.Enum.UserRole;
import com.EnumDayTask.data.model.*;
import com.EnumDayTask.data.repositories.AdminRepo;
import com.EnumDayTask.data.repositories.BlackListedTokenRepo;
import com.EnumDayTask.data.repositories.RefreshTokenRepository;
import com.EnumDayTask.data.repositories.VerificationTokenRepo;
import com.EnumDayTask.dto.request.AdminLoginReq;
import com.EnumDayTask.dto.request.AdminSignupReq;
import com.EnumDayTask.dto.response.AdminLoginRes;
import com.EnumDayTask.dto.response.AdminSignupRes;
import com.EnumDayTask.exception.*;
import com.EnumDayTask.util.AppUtils;
import com.EnumDayTask.util.JwtUtil;
import com.EnumDayTask.util.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.EnumDayTask.util.AppUtils.*;


@Service
@AllArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {


    private AdminRepo adminRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BlackListedTokenRepo blacklistedTokenRepo;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private VerificationTokenRepo verificationTokenRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    private void saveVerificationToken(Admin admin, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserEmail(admin.getEmail());
        verificationToken.setUsed(false);
        verificationToken.setExpiryDate(jwtUtils.extractExpiration(token));
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public AdminSignupRes signup(AdminSignupReq request) {
        Optional<Admin> foundAdmin = adminRepo.findByEmail(request.getEmail());
        if (foundAdmin.isPresent()) {
            Admin admin = foundAdmin.get();
            if (admin.getStatus() == AdminStatus.VERIFIED) {
                throw new EMAIL_IN_USE(EMAIL_ALREADY_EXISTS);
            } else {
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(admin);
                saveVerificationToken(admin, refreshToken.getToken());
                AdminSignupRes response = new AdminSignupRes();
                response.setToken(refreshToken.getToken());
                response.setMessage(VERIFICATION_RESENT);
                return response;
            }
        }
        String hashedPassword = AppUtils.hashPassword(request.getPassword());
        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(hashedPassword);
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(AdminStatus.PENDING_VERIFICATION);
        Admin savedAdmin = adminRepo.save(admin);

        Organisation organisation = new Organisation(savedAdmin);
        OrganisationProfile profile = new OrganisationProfile(savedAdmin);
        savedAdmin.setOrganisation(organisation);
        savedAdmin.setAdminProfile(profile);
        adminRepo.save(savedAdmin);

        String token = jwtUtils.generateToken(savedAdmin);
        saveVerificationToken(savedAdmin, token);
        AdminSignupRes response = new AdminSignupRes();
        response.setToken(token);
        response.setMessage(ACCOUNT_CREATED_SUCCESSFULLY);
        return response;

    }

    @Override
    public Admin verifyEmail(String token) {
        if (blacklistedTokenRepo.findByToken(token).isPresent()) {
            throw new TOKEN_INVALID(AppUtils.TOKEN_INVALID);}
        boolean isExpired = jwtUtils.isTokenExpired(token);
        if(isExpired){throw new TOKEN_EXPIRED(TOKEN_ALREADY_EXPIRED);}

        String email = jwtUtils.extractEmail(token);
        VerificationToken storedToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TOKEN_INVALID(AppUtils.TOKEN_INVALID));
        if (storedToken.isUsed()) {throw new TOKEN_ALREADY_USED(TOKEN_ALREADY_USED);}

        Admin foundAdmin = adminRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(AppUtils.USER_NOT_FOUND));
        foundAdmin.setStatus(AdminStatus.VERIFIED);
        storedToken.setUsed(true);
        verificationTokenRepository.save(storedToken);
        return adminRepo.save(foundAdmin);
    }

    @Override
    public AdminLoginRes login(AdminLoginReq request) {
        Admin admin = adminRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new INVALID_CREDENTIAL(INVALID_CREDENTIALS));

        if (admin.getLockoutTime() != null && admin.getLockoutTime().isAfter(LocalDateTime.now())) {
            throw new RATE_LIMITED(RATE_LIMIT_EXCEEDED);
        } else if (admin.getLockoutTime() != null && admin.getLockoutTime().isBefore(LocalDateTime.now())) {
            admin.setFailedLoginAttempts(0);
            admin.setLockoutTime(null);
            adminRepo.save(admin);}
        if (admin.getStatus() != AdminStatus.VERIFIED) {
            throw new EMAIL_NOT_VERIFIED(EMAIL_IS_NOT_VERIFIED);}
        if (!AppUtils.verifyPassword(request.getPassword(), admin.getPassword())) {
            admin.setFailedLoginAttempts(admin.getFailedLoginAttempts() + 1);
            if (admin.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                admin.setLockoutTime(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION));}
            adminRepo.save(admin);
            throw new INVALID_CREDENTIAL(INVALID_CREDENTIALS);
        }
        admin.setFailedLoginAttempts(0);
        admin.setLockoutTime(null);
        adminRepo.save(admin);


        String accessToken = jwtUtils.generateToken(admin);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(admin);

        AdminLoginRes response = new AdminLoginRes();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setMessage(LOGIN_SUCCESSFUL);
        return response;
    }

    @Override
    public void logout(String token) {
        if (blacklistedTokenRepo.findByToken(token).isPresent()) {
            throw new TOKEN_INVALID("Token has already been invalidated");}
        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);
        blacklistedTokenRepo.save(blacklistedToken);
    }

    @Override
    public void deleteAll() {
        adminRepo.deleteAll();
        verificationTokenRepository.deleteAll();
        blacklistedTokenRepo.deleteAll();
        refreshTokenRepository.deleteAll();
    }
}
