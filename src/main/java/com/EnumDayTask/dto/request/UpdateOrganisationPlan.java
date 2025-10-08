package com.EnumDayTask.dto.request;


import com.EnumDayTask.data.Enum.Plan_Limit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrganisationPlan {

    private Plan_Limit planLimit;
    private Long adminId;

}
