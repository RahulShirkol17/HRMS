package HRMS_project.page_controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import HRMS_project.entity.Payslip;
import HRMS_project.service.PayslipService;

@Controller
@RequestMapping("/emp")
public class EmpPayslipControllers {

    @Autowired
    private PayslipService payslipService;

    // Show list of payslips for logged-in employee
    @GetMapping("/payslips")
    public String getPayslips(Model model, Principal principal) {
        String email = principal.getName(); // assuming email is username
        List<Payslip> payslips = payslipService.getPayslips(email);
        model.addAttribute("payslips", payslips);
        return "emp/payslips"; // thymeleaf page
    }

    // Download PDF by payslip ID
    @GetMapping("/payslip/download/{id}")
    public ResponseEntity<Resource> downloadPayslip(@PathVariable Long id) throws IOException {
        Resource file = payslipService.downloadPayslip(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
