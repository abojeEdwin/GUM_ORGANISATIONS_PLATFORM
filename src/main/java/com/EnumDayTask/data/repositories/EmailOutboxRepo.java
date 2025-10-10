package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.Enum.EmailWorker_Status;
import com.EnumDayTask.data.model.Email_Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmailOutboxRepo extends JpaRepository<Email_Outbox,Long> {

    List<Email_Outbox> findByStatus(EmailWorker_Status status);
}
