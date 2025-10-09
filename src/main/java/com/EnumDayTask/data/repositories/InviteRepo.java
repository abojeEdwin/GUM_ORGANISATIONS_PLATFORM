package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepo extends JpaRepository<Invite,Long> {
}
