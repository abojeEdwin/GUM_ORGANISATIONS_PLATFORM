package com.EnumDayTask.service;

import com.EnumDayTask.data.model.Manager;
import com.EnumDayTask.data.model.Member;

public interface OrgInitiativeService {

    Manager inviteManager(String email);
    Member inviteMember(String email);
    Program createProgram();
    Program updateProgram();

}
