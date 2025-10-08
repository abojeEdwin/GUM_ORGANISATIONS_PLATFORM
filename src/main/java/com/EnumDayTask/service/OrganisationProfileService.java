package com.EnumDayTask.service;

import com.EnumDayTask.data.model.Organisation;
import com.EnumDayTask.data.model.OrganisationProfile;
import com.EnumDayTask.dto.request.CreateOrganisationReq;
import com.EnumDayTask.dto.request.UpdateOrganisationPlan;
import com.EnumDayTask.dto.request.UpdateProfileReq;
import com.EnumDayTask.dto.response.ApiResponse;

public interface OrganisationProfileService {

    ApiResponse updateProfile(UpdateProfileReq request);
    OrganisationProfile viewProfile(long adminId);
    Organisation createOrganisation(CreateOrganisationReq request);
    Organisation manageOrganisationPlan(UpdateOrganisationPlan request);
    Organisation viewOrganisation(long adminId);
    void deleteAll();
}
