package com.EnumDayTask.data.repositories;

import com.EnumDayTask.data.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepo extends JpaRepository<Program,Long> {
    Program findById(long id);

}
