package HRMS_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import HRMS_project.service.PersonalDetailService;

@Controller
@RequestMapping("/hr/approval/personal")
@PreAuthorize("hasRole('HR_MANAGER')")
public class PersonalDetailsApprovalController {

    @Autowired
    private PersonalDetailService personalDetailService;

    @GetMapping("/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        personalDetailService.approveRequest(id);
        return "redirect:/hr/approvals";
    }

    @GetMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id,
                                @RequestParam(defaultValue = "Rejected by HR") String comments) {
        personalDetailService.rejectRequest(id, comments);
        return "redirect:/hr/approvals";
    }
}
