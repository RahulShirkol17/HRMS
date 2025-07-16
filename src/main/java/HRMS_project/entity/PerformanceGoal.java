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
public class PerformanceGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "performance_goal_seq")
    @SequenceGenerator(name = "performance_goal_seq", sequenceName = "performance_goal_sequence", allocationSize = 1)
    private Long id;

    private String employeeEmail;
    private String managerEmail;

    private String goalTitle;
    private String description;

    private String employeeComment;
    private Integer managerRating; // 1-5 stars

    private boolean commentSubmitted = false;
    private boolean ratingGiven = false;

    private String performanceReportPath; // PDF path

    private LocalDate createdDate;
}