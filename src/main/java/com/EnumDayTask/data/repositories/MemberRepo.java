package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.data.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    long countByAdmin(Admin admin);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);

}
