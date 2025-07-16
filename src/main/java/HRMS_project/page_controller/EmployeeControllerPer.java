package HRMS_project.page_controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import HRMS_project.entity.PerformanceGoal;
import HRMS_project.service.PerformanceService;

@Controller
@RequestMapping("/emp/performance")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeControllerPer {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/test-access")
    @ResponseBody
    public String test() {
        return "Access granted";
    }
    // Show list of goals assigned to employee
    @GetMapping("/goals")
    public String viewGoals(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged in as: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        String email = principal.getName();
        List<PerformanceGoal> goals = performanceService.getGoalsForEmployee(email);
        model.addAttribute("goals", goals);
        return "emp/performance-goals";
    }

    // Submit comment for a specific goal
    @PostMapping("/submit-comment")
    public String submitComment(@RequestParam Long goalId, @RequestParam String comment) {
        performanceService.submitEmployeeComment(goalId, comment);
        return "redirect:/emp/performance/goals";
    }

    // Download performance report if available
    @GetMapping("/download-report/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long id) throws IOException {
        Resource file = performanceService.downloadReport(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename='" + file.getFilename() + "'")
                .body(file);
    }
}