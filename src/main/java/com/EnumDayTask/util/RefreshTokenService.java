package com.EnumDayTask.util;


import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.data.model.RefreshToken;
import com.EnumDayTask.data.repositories.RefreshTokenRepository;
import com.EnumDayTask.exception.TOKEN_EXPIRED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration-minutes}")
    private Long refreshExpirationMinutes;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public RefreshToken createRefreshToken(Admin admin) {
        refreshTokenRepository.deleteByAdmin(admin);

        String token = jwtUtil.generateToken(admin);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAdmin(admin);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(refreshExpirationMinutes));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new TOKEN_EXPIRED("Refresh token expired. Please log in again.");
        }
        return token;
    }
}