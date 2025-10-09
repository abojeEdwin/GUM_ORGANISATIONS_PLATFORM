package com.EnumDayTask.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProgramRequest {

    private String title;
    private String description;
    private long adminId;
    private long managerId;
    private long organisationId;

}
