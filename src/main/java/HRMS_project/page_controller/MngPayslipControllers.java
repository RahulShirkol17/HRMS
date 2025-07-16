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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import HRMS_project.entity.Payslip;
import HRMS_project.service.PayslipService;

@Controller
@RequestMapping("/mng")
public class MngPayslipControllers {

	@Autowired
    private PayslipService payslipService;

	@GetMapping("/payslips")
    public String getPayslips(Model model, Principal principal) {
        String email = principal.getName(); 
        List<Payslip> payslips = payslipService.getPayslips(email);
        model.addAttribute("payslips", payslips);
        return "mng/payslips"; 
    }

    @GetMapping("/payslip/download/{id}")
    public ResponseEntity<Resource> downloadPayslip(@PathVariable Long id) throws IOException {
        Resource file = payslipService.downloadPayslip(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
