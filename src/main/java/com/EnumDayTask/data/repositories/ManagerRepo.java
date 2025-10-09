package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.data.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RedisHash("Manager")
public interface ManagerRepo extends JpaRepository<Manager, Long> {

     Optional<Manager> findByEmail(String email);
    long countByAdmin(Admin admin);
    boolean existsByEmail(String email);
}
