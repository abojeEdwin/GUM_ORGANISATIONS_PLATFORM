package com.EnumDayTask.dto.response;


import com.EnumDayTask.data.Enum.ProfileCompleteness;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse{

    private Object data;
    private String message;
    private List<String> missingFields;
    private ProfileCompleteness  profileCompleteness;

}
