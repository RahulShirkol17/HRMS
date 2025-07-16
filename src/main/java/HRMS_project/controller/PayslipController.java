package HRMS_project.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.Payslip;
import HRMS_project.service.PayslipService;

@RestController
@RequestMapping("/employee/payslip")
@PreAuthorize("hasRole('EMPLOYEE')")
public class PayslipController {

    @Autowired private PayslipService payslipService;

    @GetMapping("/list")
    public List<Payslip> getAllPayslips(@RequestParam String email) {
        return payslipService.getPayslips(email);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource resource = payslipService.downloadPayslip(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}