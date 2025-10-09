package com.EnumDayTask.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptInviteRequest {

    private String token;
    private String password;
}
