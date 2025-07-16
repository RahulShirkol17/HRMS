package HRMS_project.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.JobPosting;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByTitleContainingIgnoreCaseOrDepartmentContainingIgnoreCase(String title, String department);
}