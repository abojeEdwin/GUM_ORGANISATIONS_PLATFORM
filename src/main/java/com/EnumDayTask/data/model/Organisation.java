package com.EnumDayTask.data.model;


import com.EnumDayTask.data.Enum.Plan_Limit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Organisation")
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    Plan_Limit planLimit;

    @OneToOne(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private OrganisationProfile organisationProfile;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private Admin admin;

}
