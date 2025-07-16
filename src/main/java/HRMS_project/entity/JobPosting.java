package HRMS_project.entity;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_posting_seq")
    @SequenceGenerator(name = "job_posting_seq", sequenceName = "job_posting_sequence", allocationSize = 1)
    private Long id;

    private String title;
    private String description;
    private String department;
    private int numberOfOpenings;

    private LocalDate createdAt;

    // getters and setters
}