package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Org_Profile")
@Getter
@Setter
public class OrganisationProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private Admin admin;

    private String logoUrl;
    private String description;
    private String industry;
    private String website;
    private ProfileCompleteness profileCompleteness;

     public OrganisationProfile(Admin admin) {
            this.admin = admin;
        this.profileCompleteness = ProfileCompleteness.ZERO;
    }


}
