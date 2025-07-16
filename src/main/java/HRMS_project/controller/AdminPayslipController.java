package HRMS_project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.Payslip;
import HRMS_project.service.PayslipService;

@RestController
@RequestMapping("/admin/payslip")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPayslipController {

    @Autowired private PayslipService payslipService;

    @PostMapping("/generate")
    public ResponseEntity<Payslip> createPayslip(
        @RequestParam String email,
        @RequestParam String name,
        @RequestParam String month,
        @RequestParam double basicPay,
        @RequestParam double deductions
    ) throws IOException {
        return new ResponseEntity<>(
            payslipService.generatePayslip(email, name, month, basicPay, deductions),
            HttpStatus.CREATED
        );
    }
}