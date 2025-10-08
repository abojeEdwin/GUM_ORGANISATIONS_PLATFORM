package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.Invite_Status;
import com.EnumDayTask.data.Enum.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    private String email;
    UserRole role;
    Invite_Status status;
    private String password;
    private String organisationId;
    private String invitedBy;


}
