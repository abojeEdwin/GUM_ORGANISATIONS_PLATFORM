package com.EnumDayTask.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteManagerResponse {

    private String token;
    private String managerEmail;
    private long adminId;
}
