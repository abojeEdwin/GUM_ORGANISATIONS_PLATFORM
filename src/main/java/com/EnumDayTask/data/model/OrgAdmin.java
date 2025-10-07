package com.EnumDayTask.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="Org_Admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrgAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private int failedLoginAttempts;
    private LocalDateTime lockoutTime;

    @OneToOne(mappedBy = "talent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private OrgAdminProfile adminProfile;


    public void setAdminProfile(OrgAdminProfile adminProfile) {
        if (adminProfile == null) {
            if (this.adminProfile != null) {
                this.adminProfile.setAdmin(null);
            }
        } else {
            adminProfile.setAdmin(this);
        }
        this.adminProfile = adminProfile;
    }

}
