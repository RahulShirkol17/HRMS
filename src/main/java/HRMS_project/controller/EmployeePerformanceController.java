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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.PerformanceGoal;
import HRMS_project.service.PerformanceService;

@RestController
@RequestMapping("/employee/performance")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeePerformanceController {

    @Autowired private PerformanceService service;

    @GetMapping("/goals")
    public List<PerformanceGoal> getMyGoals(@RequestParam String email) {
        return service.getGoalsForEmployee(email);
    }

    @PostMapping("/comment/{goalId}")
    public PerformanceGoal commentOnGoal(@PathVariable Long goalId, @RequestParam String comment) {
        return service.submitEmployeeComment(goalId, comment);
    }

    @GetMapping("/download/{goalId}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long goalId) throws IOException {
        Resource file = service.downloadReport(goalId);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}