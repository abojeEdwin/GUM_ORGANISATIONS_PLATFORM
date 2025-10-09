package com.EnumDayTask.util;


import com.EnumDayTask.data.Enum.EmailWorker_Status;
import com.EnumDayTask.data.model.Email_Outbox;
import com.EnumDayTask.data.repositories.EmailOutboxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailOutboxWorker {

    private final EmailOutboxRepo emailOutboxRepo;

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void processPendingEmails() {
        List<Email_Outbox> pendingEmails = emailOutboxRepo.findByEmailStatus(EmailWorker_Status.PENDING);

        for (Email_Outbox email : pendingEmails) {
            try {
                sendEmail(email);
                email.setEmail_status(EmailWorker_Status.SENT);
                emailOutboxRepo.save(email);
                System.out.println("Email sent successfully to " + email.getRecipient());

            } catch (Exception e) {
                email.setEmail_status(EmailWorker_Status.FAILED);
                email.setErrorMessage(e.getMessage());
                emailOutboxRepo.save(email);
                System.err.println("Failed to send email to " + email.getRecipient() + ": " + e.getMessage());
            }
        }
    }

    private void sendEmail(Email_Outbox email) {
        System.out.println("Sending email to: " + email.getRecipient());
        System.out.println("Subject: " + email.getSubject());
        System.out.println("Body: " + email.getBody());
    }

}
