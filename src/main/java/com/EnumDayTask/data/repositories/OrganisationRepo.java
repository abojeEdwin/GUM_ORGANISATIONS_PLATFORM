package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrganisationRepo extends JpaRepository <Organisation, Long>{

    Organisation findByAdminId(Long adminId);
    boolean existsByAdminId(Long adminId);

}
