package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListedTokenRepo extends JpaRepository <BlackListedToken, Long>{

    Optional<BlackListedToken> findByToken(String token);

}
