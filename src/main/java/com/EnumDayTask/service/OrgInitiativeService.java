package com.EnumDayTask.service;

import com.EnumDayTask.data.model.Manager;
import com.EnumDayTask.data.model.Member;
import com.EnumDayTask.data.model.Program;
import com.EnumDayTask.dto.request.ArchiveProgramRequest;
import com.EnumDayTask.dto.request.CreateProgramRequest;
import com.EnumDayTask.dto.request.InviteManagerRequest;
import com.EnumDayTask.dto.request.UpdateProgramRequest;

public interface OrgInitiativeService {

    Manager inviteManager(InviteManagerRequest request);
    Member inviteMember(String email);
    void acceptInvite(String token);
    Program createProgram(CreateProgramRequest request);
    Program updateProgram(UpdateProgramRequest request);
    Program archiveProgram(ArchiveProgramRequest request);
    void deleteAll();

}
