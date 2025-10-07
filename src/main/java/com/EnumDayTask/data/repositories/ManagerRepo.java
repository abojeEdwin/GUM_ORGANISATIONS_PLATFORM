package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepo extends JpaRepository<Manager, Long> {
}
