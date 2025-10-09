package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Email_Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOutboxRepo extends JpaRepository<Email_Outbox,Long> {
}
