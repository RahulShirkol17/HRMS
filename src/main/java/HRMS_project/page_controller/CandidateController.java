package HRMS_project.page_controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import HRMS_project.entity.JobApplication;
import HRMS_project.entity.JobPosting;
import HRMS_project.service.JobApplicationService;
import HRMS_project.service.JobPostingService;

@Controller
@RequestMapping("/cnd")
public class CandidateController {
	
	@Autowired
	JobApplicationService app;
	
	@Autowired
	JobPostingService post;

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "cnd/dashboard";
    }

    @GetMapping("/jobs")
    public String listJobs(Model model) {
        List<JobPosting> jobs = post.getAllJobs(); // or filter active jobs
        model.addAttribute("jobs", jobs);
        return "cnd/job-list";
    }

    @GetMapping("/applied-jobs")
    public String myApplications(Model model, Principal principal) {
        String email = principal.getName();
        List<JobApplication> applications = app.findByCandidateEmail(email);
        model.addAttribute("applications", applications);
        return "cnd/applied-jobs";
    }
    
    @GetMapping("/apply/{jobId}")
    public String showApplicationForm(@PathVariable Long jobId, Model model) {
        JobPosting job = post.getJobById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        model.addAttribute("job", job);
        return "cnd/apply-job";
    }

    // Handle form submission
    @PostMapping("/apply/{jobId}")
    public String applyToJob(@PathVariable Long jobId,
                             @RequestParam("candidateName") String candidateName,
                             @RequestParam("candidateEmail") String candidateEmail,
                             @RequestParam("resume") MultipartFile resume,
                             @RequestParam("coverLetter") MultipartFile coverLetter,
                             Model model) {
        try {
            JobApplication application = app.applyToJob(jobId, resume, coverLetter, candidateName, candidateEmail);
            model.addAttribute("message", "Application submitted successfully!");
            return "cnd/application-success"; // Ensure this file exists
        } catch (IOException e) {
            // Re-fetch job to avoid Thymeleaf error on model
            JobPosting job = post.getJobById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            model.addAttribute("job", job);
            model.addAttribute("error", "Failed to submit application. " + e.getMessage());
            return "cnd/apply-job";
        }
    }
}