package com.EnumDayTask.service;

import com.EnumDayTask.data.Enum.ProfileCompleteness;
import com.EnumDayTask.data.model.Admin;
import com.EnumDayTask.data.model.Organisation;
import com.EnumDayTask.data.model.OrganisationProfile;
import com.EnumDayTask.data.repositories.AdminRepo;
import com.EnumDayTask.data.repositories.OrganisationProfileRepo;
import com.EnumDayTask.data.repositories.OrganisationRepo;
import com.EnumDayTask.dto.request.CreateOrganisationReq;
import com.EnumDayTask.dto.request.UpdateOrganisationPlan;
import com.EnumDayTask.dto.request.UpdateProfileReq;
import com.EnumDayTask.dto.response.ApiResponse;
import com.EnumDayTask.exception.ADMIN_NOT_FOUND;
import com.EnumDayTask.exception.INVALID_URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.EnumDayTask.util.AppUtils.*;

@Service
public class OrganisationProfileServiceImpl implements OrganisationProfileService {

    @Autowired
    private OrganisationProfileRepo organisationProfileRepo;
    @Autowired
    private OrganisationRepo organisationRepo;
    @Autowired
    private AdminRepo adminRepo;

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[\\w\\d.-]+\\.[a-zA-Z]{2,6}(/[\\w\\d./?=#&%-]*)?$"
    );

    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    @Override
    public ApiResponse updateProfile(UpdateProfileReq request) {
        OrganisationProfile foundProfile = organisationProfileRepo.findByAdminId(request.getAdminId());
        if (foundProfile == null) {
            throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);
        }
        Admin managedAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new ADMIN_NOT_FOUND(NO_ADMIN_FOUND));

        if (request.getLogoUrl() != null && !request.getLogoUrl().isEmpty() && !isValidUrl(request.getLogoUrl())) {
            throw new INVALID_URL(INVALID_URL_FORMAT);
        }
        if (request.getWebsite() != null && !request.getWebsite().isEmpty() && !isValidUrl(request.getWebsite())) {
            throw new INVALID_URL(INVALID_URL_FORMAT);
        }

        foundProfile.setLogoUrl(request.getLogoUrl());
        foundProfile.setIndustry(request.getIndustry());
        foundProfile.setWebsite(request.getWebsite());
        foundProfile.setDescription(request.getDescription());
        foundProfile.setAdmin(managedAdmin);

        List<String> missingFields = new ArrayList<>();
        int setFields = 0;

        if (foundProfile.getLogoUrl() != null && !foundProfile.getLogoUrl().isEmpty()) {
            setFields++;
        } else {
            missingFields.add("logoUrl");
        }
        if (foundProfile.getIndustry() != null && !foundProfile.getIndustry().isEmpty()) {
            setFields++;
        } else {
            missingFields.add("industry");
        }
        if (foundProfile.getWebsite() != null && !foundProfile.getWebsite().isEmpty()) {
            setFields++;
        } else {
            missingFields.add("website");
        }
        if (foundProfile.getDescription() != null && !foundProfile.getDescription().isEmpty()) {
            setFields++;
        } else {
            missingFields.add("description");
        }

        ProfileCompleteness completeness;
        switch (setFields) {
            case 1:
                completeness = ProfileCompleteness.TWENTY;
                break;
            case 2:
                completeness = ProfileCompleteness.SIXTY;
                break;
            case 3:
                completeness = ProfileCompleteness.EIGHTY;
                break;
            case 4:
                completeness = ProfileCompleteness.HUNDRED;
                break;
            default:
                completeness = ProfileCompleteness.ZERO;
        }
        foundProfile.setProfileCompleteness(completeness);
        OrganisationProfile savedProfile = organisationProfileRepo.save(foundProfile);

        ApiResponse response = new ApiResponse();
        response.setMessage(PROFILE_UPDATED_SUCCESSFULLY);
        response.setData(savedProfile);
        response.setMissingFields(missingFields);
        response.setProfileCompleteness(completeness);

        return response;
    }

    @Override
    public OrganisationProfile viewProfile(long adminId) {
        return organisationProfileRepo.findByAdminId(adminId);
    }

    @Override
    public Organisation createOrganisation(CreateOrganisationReq request) {
        Organisation foundOrganisation = organisationRepo.findByAdminId(request.getAdminId());
        if (foundOrganisation == null) {
            throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);
        }
        Admin managedAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new ADMIN_NOT_FOUND(NO_ADMIN_FOUND));
        foundOrganisation.setName(request.getName());
        foundOrganisation.setPlanLimit(request.getPlanLimit());
        foundOrganisation.setAdmin(managedAdmin);
        return organisationRepo.save(foundOrganisation);
    }

    @Override
    public Organisation manageOrganisationPlan(UpdateOrganisationPlan request) {
        Organisation foundOrganisation = organisationRepo.findByAdminId(request.getAdminId());
        if (foundOrganisation == null) {
            throw new ADMIN_NOT_FOUND(NO_ADMIN_FOUND);
        }
        Admin managedAdmin = adminRepo.findById(request.getAdminId())
                .orElseThrow(() -> new ADMIN_NOT_FOUND(NO_ADMIN_FOUND));
        foundOrganisation.setPlanLimit(request.getPlanLimit());
        foundOrganisation.setAdmin(managedAdmin);
        return organisationRepo.save(foundOrganisation);
    }

    @Override
    public Organisation viewOrganisation(long adminId) {
        return organisationRepo.findByAdminId(adminId);
    }

    @Override
    public void deleteAll() {
        organisationProfileRepo.deleteAll();
        organisationRepo.deleteAll();
        adminRepo.deleteAll();
    }
}
