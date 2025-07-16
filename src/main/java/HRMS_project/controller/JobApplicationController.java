package HRMS_project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import HRMS_project.entity.JobApplication;
import HRMS_project.service.JobApplicationService;

@RestController
@RequestMapping("/candidate/apply")
@PreAuthorize("hasRole('CANDIDATE')")
public class JobApplicationController {

    @Autowired private JobApplicationService applicationService;

    @PostMapping("/{jobId}")
    public ResponseEntity<JobApplication> applyToJob(
            @PathVariable Long jobId,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("coverLetter") MultipartFile coverLetter,
            @RequestParam String name,
            @RequestParam String email
    ) {
        try {
            JobApplication app = applicationService.applyToJob(jobId, resume, coverLetter, name, email);
            return new ResponseEntity<>(app, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}