package HRMS_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.JobApplication;
import HRMS_project.service.JobApplicationService;

@RestController
@RequestMapping("/hr/applications")
@PreAuthorize("hasRole('HR_MANAGER')")
public class ApplicationViewController {

    @Autowired private JobApplicationService applicationService;

    @GetMapping("/{jobId}")
    public List<JobApplication> getAllApplications(@PathVariable Long jobId) {
        return applicationService.getApplicationsByJobId(jobId);
    }
}