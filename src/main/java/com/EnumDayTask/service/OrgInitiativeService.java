package com.EnumDayTask.service;

import com.EnumDayTask.data.model.Program;
import com.EnumDayTask.dto.request.*;
import com.EnumDayTask.dto.response.AcceptInviteResponse;
import com.EnumDayTask.dto.response.InviteManagerResponse;
import com.EnumDayTask.dto.response.InviteMemberResponse;

public interface OrgInitiativeService {

    InviteManagerResponse inviteManager(InviteManagerRequest request);
    InviteMemberResponse inviteMember(InviteMemberRequest request);
    AcceptInviteResponse acceptInvite(AcceptInviteRequest request);
    Program createProgram(CreateProgramRequest request);
    Program updateProgram(UpdateProgramRequest request);
    Program archiveProgram(ArchiveProgramRequest request);
    void deleteAll();

}
