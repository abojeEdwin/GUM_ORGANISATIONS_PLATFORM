package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RedisHash("BlackListedToken")
public interface BlackListedTokenRepo extends JpaRepository <BlackListedToken, Long>{

    Optional<BlackListedToken> findByToken(String token);

}
