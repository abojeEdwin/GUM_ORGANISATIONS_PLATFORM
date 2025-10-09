package com.EnumDayTask.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteManagerRequest {

    private long adminId;
    private String email;
}
