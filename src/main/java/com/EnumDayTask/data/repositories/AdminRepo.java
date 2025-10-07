package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Admin> findById(long id);

}
