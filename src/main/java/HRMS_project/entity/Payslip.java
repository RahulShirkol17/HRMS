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
@Setter
@Getter
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payslip_seq")
    @SequenceGenerator(name = "payslip_seq", sequenceName = "payslip_sequence", allocationSize = 1)
    private Long id;
    
    private String employeeEmail;
    private String employeeName;

    private String month; // "June 2025"
    private double basicPay;
    private double deductions;
    private double netPay;

    private String pdfPath;

    private LocalDate generatedDate;

}