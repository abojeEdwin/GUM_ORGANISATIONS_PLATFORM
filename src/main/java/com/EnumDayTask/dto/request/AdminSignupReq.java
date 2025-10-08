package com.EnumDayTask.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignupReq {

    @Email
    private String email;
    @NotNull(message = "Password cannot be blank")
    private String password;

}

