package HRMS_project.page_controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import HRMS_project.entity.ManagerLeaveRequest;
import HRMS_project.service.MngLeaveService;

@Controller
public class MngLeaveController {

	@Autowired
    private MngLeaveService leaveService;

    // Show leave request form and list
    @GetMapping("/mng/leaves")
    public String viewLeavePage(Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("leaveRequest", new ManagerLeaveRequest());
        model.addAttribute("leaveList", leaveService.getLeavesForManager(email));
        return "mng/leaves";
    }

    // Apply for a new leave
    @PostMapping("/mng/leaves/apply")
    public String applyLeave(@ModelAttribute ManagerLeaveRequest leaveRequest, Principal principal) {
        String email = principal.getName();
        leaveService.applyLeave(email, leaveRequest);
        return "redirect:/mng/leaves";
    }
}
