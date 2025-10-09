package com.EnumDayTask.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AppUtils {



    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String ACCOUNT_CREATED_SUCCESSFULLY = "Account created successfully";
    public static final String VERIFICATION_RESENT = "Verification email sent successfully";
    public static final String TOKEN_INVALID = "Token invalid";
    public static final String TOKEN_ALREADY_USED = "Token already used";
    public static final String TOKEN_ALREADY_EXPIRED = "Token expired";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String EMAIL_IS_NOT_VERIFIED = "Email is not verified";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String RATE_LIMIT_EXCEEDED = "You have exceeded the maximum number of login attempts. Please try again later.";
    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final int LOCKOUT_DURATION = 15;
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Profile updated successfully";
    public static final String NO_ADMIN_FOUND = "Admin not found";
    public static final String INVALID_URL_FORMAT ="Invalid url format";


    public static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[\\w.-]+\\.[a-zA-Z]{2,6}(/[\\w./?=#&%-]*)?$"
    );

    public static boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }


    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isEmpty() || encodedPassword == null || encodedPassword.isEmpty()) {return false;}
        try {return passwordEncoder.matches(rawPassword, encodedPassword);} catch (IllegalArgumentException e) {return false;}}
}
