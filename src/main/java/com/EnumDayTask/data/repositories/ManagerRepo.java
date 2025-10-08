package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash("Manager")
public interface ManagerRepo extends JpaRepository<Manager, Long> {
}
