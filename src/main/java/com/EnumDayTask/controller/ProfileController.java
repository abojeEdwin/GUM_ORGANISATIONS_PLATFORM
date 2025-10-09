package com.EnumDayTask.controller;


import com.EnumDayTask.data.model.Organisation;
import com.EnumDayTask.data.model.OrganisationProfile;
import com.EnumDayTask.dto.request.CreateOrganisationReq;
import com.EnumDayTask.dto.request.UpdateOrganisationPlan;
import com.EnumDayTask.dto.request.UpdateProfileReq;
import com.EnumDayTask.dto.response.ApiResponse;
import com.EnumDayTask.service.OrganisationProfileServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/org")
@AllArgsConstructor
public class ProfileController {

    @Autowired
    private OrganisationProfileServiceImpl organisationProfileService;


    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse> updateProfile (@RequestBody @Valid UpdateProfileReq request){
        return ResponseEntity.ok(organisationProfileService.updateProfile(request));
    }

    @GetMapping("/me")
    public ResponseEntity<OrganisationProfile> viewProfile(@RequestParam long adminId){
        return ResponseEntity.ok(organisationProfileService.viewProfile(adminId));
    }

    @PostMapping("/create-organisation")
    public ResponseEntity<Organisation> createOrganisation(@Valid @RequestBody CreateOrganisationReq request){
        return ResponseEntity.ok(organisationProfileService.createOrganisation(request));
    }

    @PostMapping("/manage-organisation-plan")
    public ResponseEntity<Organisation> managePlan(@Valid @RequestBody UpdateOrganisationPlan request){
        return ResponseEntity.ok(organisationProfileService.manageOrganisationPlan(request));
    }

    @GetMapping("/view-organisation")
    public ResponseEntity<Organisation> viewOrganisation(@RequestParam long adminId){
        return ResponseEntity.ok(organisationProfileService.viewOrganisation(adminId));
    }

}
