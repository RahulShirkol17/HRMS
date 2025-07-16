package HRMS_project.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timesheet_seq")
    @SequenceGenerator(name = "timesheet_seq", sequenceName = "timesheet_sequence", allocationSize = 1)
    private Long id;
    
    private String employeeEmail;
    
    @Column(name = "work_date")
    private LocalDate date;

    private LocalTime loginTime;
    private LocalTime logoutTime;

    private String status; // "Working", "Holiday", etc.

    private Duration workingDuration; // Calculated from login and logout

    @Transient
    public String getFormattedWorkingDuration() {
        if (workingDuration == null) return "";
        long hours = workingDuration.toHours();
        long minutes = workingDuration.toMinutesPart();
        long seconds = workingDuration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}