package HRMS_project.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CandidateApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_app_seq")
    @SequenceGenerator(name = "candidate_app_seq", sequenceName = "candidate_application_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    private User candidate; // where role = CANDIDATE

    @ManyToOne
    private JobPosting jobPosting;

    private LocalDate appliedDate;
}
