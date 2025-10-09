package com.EnumDayTask.dto.response;


import com.EnumDayTask.data.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptInviteResponse {

    private String message;
    private String email;
    UserRole role;

}
