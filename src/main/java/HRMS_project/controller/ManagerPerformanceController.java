package HRMS_project.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.PerformanceGoal;
import HRMS_project.service.PerformanceService;

@RestController
@RequestMapping("/manager/performance")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerPerformanceController {

    @Autowired private PerformanceService service;

    @PostMapping("/set")
    public PerformanceGoal setGoal(@RequestParam String managerEmail,
                                   @RequestParam String employeeEmail,
                                   @RequestParam String title,
                                   @RequestParam String description) {
        return service.createGoal(managerEmail, employeeEmail, title, description);
    }

    @GetMapping("/goals")
    public List<PerformanceGoal> getMyTeamGoals(@RequestParam String email) {
        return service.getGoalsByManager(email);
    }

    @PostMapping("/rate/{goalId}")
    public PerformanceGoal rateGoal(@PathVariable Long goalId, @RequestParam int rating) throws IOException {
        return service.rateGoal(goalId, rating);
    }
}