package HRMS_project.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import HRMS_project.entity.JobApplication;
import HRMS_project.entity.JobPosting;
import HRMS_project.repository.JobApplicationRepository;
import HRMS_project.repository.JobPostingRepository;

@Service
public class JobApplicationService {

    @Autowired private JobApplicationRepository applicationRepo;
    @Autowired private JobPostingRepository jobRepo;
    @Autowired private JavaMailSender mailSender;

    private final String UPLOAD_DIR = "uploads/";

    public JobApplication applyToJob(Long jobId, MultipartFile resume, MultipartFile coverLetter,
                                     String candidateName, String candidateEmail) throws IOException {

        JobPosting job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String resumeName = UUID.randomUUID() + "_" + resume.getOriginalFilename();
        String coverLetterName = UUID.randomUUID() + "_" + coverLetter.getOriginalFilename();

        Path resumePath = Paths.get(UPLOAD_DIR + resumeName);
        Path coverLetterPath = Paths.get(UPLOAD_DIR + coverLetterName);

        Files.createDirectories(resumePath.getParent());
        Files.copy(resume.getInputStream(), resumePath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(coverLetter.getInputStream(), coverLetterPath, StandardCopyOption.REPLACE_EXISTING);

        JobApplication application = new JobApplication();
        application.setCandidateEmail(candidateEmail);
        application.setCandidateName(candidateName);
        application.setResumePath(resumeName);                    // ✅ just filename
        application.setCoverLetterPath(coverLetterName);
        application.setJob(job);
        application.setAppliedDate(LocalDate.now());

        applicationRepo.save(application);

        sendConfirmationEmail(candidateEmail, job.getTitle());

        return application;
    }

    private void sendConfirmationEmail(String email, String jobTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Job Application Submitted");
        message.setText("Thank you for applying for the position: " + jobTitle);
        mailSender.send(message);
    }

    public List<JobApplication> getApplicationsByJobId(Long jobId) {
        return applicationRepo.findByJob_Id(jobId);
    }
    
    public List<JobApplication> findByCandidateEmail(String email) {
        return applicationRepo.findByCandidateEmail(email);
    }
}