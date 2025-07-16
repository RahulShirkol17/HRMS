package HRMS_project.page_controller;

import HRMS_project.entity.PerformanceGoal;
import HRMS_project.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mng/performance")
public class ManagerPerformanceControllers {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/goals")
    public String viewGoals(Model model, Principal principal) {
        String managerEmail = principal.getName();
        List<PerformanceGoal> goals = performanceService.getGoalsByManager(managerEmail);
        model.addAttribute("goals", goals);
        model.addAttribute("newGoal", new PerformanceGoal());
        return "mng/manager-goals";
    }

    @PostMapping("/create")
    public String createGoal(@ModelAttribute PerformanceGoal newGoal, Principal principal) {
        String managerEmail = principal.getName();
        performanceService.createGoal(managerEmail, newGoal.getEmployeeEmail(), newGoal.getGoalTitle(), newGoal.getDescription());
        return "redirect:/mng/performance/goals";
    }

    @PostMapping("/rate/{id}")
    public String rateGoal(@PathVariable Long id, @RequestParam int rating) throws IOException {
        performanceService.rateGoal(id, rating);
        return "redirect:/mng/performance/goals";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long id, HttpServletRequest request) throws IOException {
        Resource resource = performanceService.downloadReport(id);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        contentType = (contentType != null) ? contentType : "application/octet-stream";
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + StringUtils.getFilename(resource.getFilename()) + "\"")
                .header("Content-Type", contentType)
                .body(resource);
    }
}
