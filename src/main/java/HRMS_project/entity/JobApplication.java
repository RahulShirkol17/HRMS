package HRMS_project.entity;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_application_seq")
    @SequenceGenerator(name = "job_application_seq", sequenceName = "job_application_sequence", allocationSize = 1)
    private Long id;

    private String candidateEmail;
    private String candidateName;

    private String resumePath;
    private String coverLetterPath;

    private LocalDate appliedDate;

    @ManyToOne
    @JoinColumn(name = "job_posting_id")
    private JobPosting job;

    // getters and setters
}