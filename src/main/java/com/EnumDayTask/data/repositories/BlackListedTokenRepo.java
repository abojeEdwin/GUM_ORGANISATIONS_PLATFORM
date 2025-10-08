package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListedTokenRepo extends JpaRepository <BlackListedToken, Long>{

    Optional<BlackListedToken> findByToken(String token);

}
