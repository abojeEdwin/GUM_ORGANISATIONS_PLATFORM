package com.EnumDayTask.service;

import com.EnumDayTask.data.model.OrganisationProfile;
import com.EnumDayTask.dto.request.UpdateProfileReq;

public interface OrganisationProfileService {

    OrganisationProfile updateProfile(UpdateProfileReq request);

}
