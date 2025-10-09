package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.Invite_Status;
import com.EnumDayTask.data.Enum.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Email
    private String email;
    private String password;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
    UserRole role;
    Invite_Status status;

}
