package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.OrganisationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationProfileRepo extends JpaRepository<OrganisationProfile, Long> {

    Optional<OrganisationProfile> findById(Long organisationId);

}
