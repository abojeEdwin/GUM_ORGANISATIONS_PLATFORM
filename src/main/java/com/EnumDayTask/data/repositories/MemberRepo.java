package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepo extends JpaRepository<Member, Long> {
}
