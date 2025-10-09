package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteRepo extends JpaRepository<Invite,Long> {

    Optional<Invite> findByToken(String token);
}
