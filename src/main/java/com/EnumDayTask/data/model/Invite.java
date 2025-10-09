package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name="invite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invite {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;
    private String email;
    UserRole role;
    private long org_id;
    LocalDateTime expires_at;
    boolean isUsed;

}
