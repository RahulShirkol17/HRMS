package HRMS_project.page_controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import HRMS_project.entity.EmployeeLeaveRequest;
import HRMS_project.service.EmpLeaveService;

@Controller
public class EmpLeaveController {

    @Autowired
    private EmpLeaveService leaveService;

    // Show leave request form and list
    @GetMapping("/emp/leaves")
    public String viewLeavePage(Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("leaveRequest", new EmployeeLeaveRequest());
        model.addAttribute("leaveList", leaveService.getLeavesForEmployee(email));
        return "emp/leaves";
    }

    // Apply for a new leave
    @PostMapping("/emp/leaves/apply")
    public String applyLeave(@ModelAttribute EmployeeLeaveRequest leaveRequest, Principal principal) {
        String email = principal.getName();
        leaveService.applyLeave(email, leaveRequest);
        return "redirect:/emp/leaves";
    }
}
