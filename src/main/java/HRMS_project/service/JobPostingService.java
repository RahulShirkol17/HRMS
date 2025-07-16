package HRMS_project.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.JobPosting;
import HRMS_project.repository.JobPostingRepository;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobRepo;

    public List<JobPosting> getAllJobs() {
        return jobRepo.findAll();
    }

    public JobPosting createJob(JobPosting job) {
        job.setCreatedAt(LocalDate.now());
        return jobRepo.save(job);
    }
    
    public Optional<JobPosting> getJobById(Long id) {
        return jobRepo.findById(id);
    }

    public Optional<JobPosting> updateJob(Long id, JobPosting updatedJob) {
        return jobRepo.findById(id).map(job -> {
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setDepartment(updatedJob.getDepartment());
            job.setNumberOfOpenings(updatedJob.getNumberOfOpenings());
            return jobRepo.save(job);
        });
    }

    public void deleteJob(Long id) {
        jobRepo.deleteById(id);
    }

    public List<JobPosting> searchJobs(String keyword) {
        return jobRepo.findByTitleContainingIgnoreCaseOrDepartmentContainingIgnoreCase(keyword, keyword);
    }
}