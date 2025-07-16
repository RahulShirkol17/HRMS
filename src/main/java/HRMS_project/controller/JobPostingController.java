package HRMS_project.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.JobPosting;
import HRMS_project.service.JobPostingService;

@RestController
@RequestMapping("/hr/jobs")
@PreAuthorize("hasRole('HR_MANAGER')")
public class JobPostingController {

    @Autowired
    private JobPostingService jobService;

    @GetMapping
    public List<JobPosting> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PostMapping
    public ResponseEntity<JobPosting> createJob(@RequestBody JobPosting job) {
        return new ResponseEntity<>(jobService.createJob(job), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJob(@PathVariable Long id, @RequestBody JobPosting job) {
        return jobService.updateJob(id, job)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<JobPosting> searchJobs(@RequestParam String keyword) {
        return jobService.searchJobs(keyword);
    }
}