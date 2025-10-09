package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.EmailWorker_Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Email_Outbox")
public class Email_Outbox {

    @Id
    private long id;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private EmailWorker_Status email_status;
    private LocalDateTime createdAt;
    private String errorMessage;

}
