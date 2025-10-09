package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.Enum.EmailWorker_Status;
import com.EnumDayTask.data.model.Email_Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailOutboxRepo extends JpaRepository<Email_Outbox,Long> {

    List<Email_Outbox> findByEmailStatus(EmailWorker_Status status);
}
