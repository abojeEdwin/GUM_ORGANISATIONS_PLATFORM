package com.EnumDayTask.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileReq {

    private String logoUrl;
    private String description;
    private String industry;
    private String website;
    private long adminId;

}
