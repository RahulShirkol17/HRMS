package HRMS_project.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import HRMS_project.entity.Role.LeaveStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ManagerLeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mng_leave_request_seq")
    @SequenceGenerator(name = "mng_leave_request_seq", sequenceName = "mng_leave_request_sequence", allocationSize = 1)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) // or EAGER
    @JoinColumn(name = "user_id")
    private User user;

    private String managerEmail;
    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    private String hrComment;

    private LocalDate appliedDate;

    // getters and setters
}