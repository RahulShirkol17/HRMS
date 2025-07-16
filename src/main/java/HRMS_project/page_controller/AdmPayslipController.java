package HRMS_project.page_controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import HRMS_project.entity.Payslip;
import HRMS_project.service.PayslipService;

@Controller
@RequestMapping("/adm/payslip")
public class AdmPayslipController {

    @Autowired
    private PayslipService payslipService;

    @GetMapping("/form")
    public String showPayslipForm(Model model) {
        model.addAttribute("payslip", new Payslip());
        return "admin/generate-payslip";
    }

    @PostMapping("/generate")
    public String generatePayslip(@ModelAttribute Payslip payslip, RedirectAttributes redirectAttributes) {
        try {
            Payslip saved = payslipService.generatePayslip(
                    payslip.getEmployeeEmail(),
                    payslip.getEmployeeName(),
                    payslip.getMonth(),
                    payslip.getBasicPay(),
                    payslip.getDeductions()
            );
            redirectAttributes.addFlashAttribute("success", "Payslip generated successfully for " + saved.getEmployeeName());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error generating payslip: " + e.getMessage());
        }
        return "redirect:/adm/payslip/form";
    }

}
