package com.EnumDayTask.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchiveProgramRequest {
    private long adminId;
    private long managerId;
    private long organisationId;
}
