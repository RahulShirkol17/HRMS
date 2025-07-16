package HRMS_project.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import HRMS_project.entity.PerformanceGoal;
import HRMS_project.repository.PerformanceGoalRepository;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceGoalRepository goalRepo;

    private final String REPORT_DIR = "performance_reports/";

    public PerformanceGoal createGoal(String managerEmail, String employeeEmail, String title, String desc) {
        PerformanceGoal goal = new PerformanceGoal();
        goal.setEmployeeEmail(employeeEmail);
        goal.setManagerEmail(managerEmail);
        goal.setGoalTitle(title);
        goal.setDescription(desc);
        goal.setCreatedDate(LocalDate.now());
        return goalRepo.save(goal);
    }

    public PerformanceGoal submitEmployeeComment(Long goalId, String comment) {
        PerformanceGoal goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        goal.setEmployeeComment(comment);
        goal.setCommentSubmitted(true);
        return goalRepo.save(goal);
    }

    public PerformanceGoal rateGoal(Long goalId, int rating) throws IOException {
        PerformanceGoal goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        if (!goal.isCommentSubmitted()) {
            throw new IllegalStateException("Cannot rate before employee comments");
        }
        goal.setManagerRating(rating);
        goal.setRatingGiven(true);

        String filename = UUID.randomUUID() + "_performance_report.pdf";
        Path path = Paths.get(REPORT_DIR + filename);
        Files.createDirectories(path.getParent());

        String report = "Performance Review Report\n\n" +
                        "Goal: " + goal.getGoalTitle() + "\n" +
                        "Description: " + goal.getDescription() + "\n" +
                        "Employee Comment: " + goal.getEmployeeComment() + "\n" +
                        "Manager Rating: " + rating + " / 5\n";

        Files.write(path, report.getBytes());

        goal.setPerformanceReportPath(path.toString());

        return goalRepo.save(goal);
    }

    public Resource downloadReport(Long goalId) throws IOException {
        PerformanceGoal goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        if (!goal.isRatingGiven()) {
            throw new IllegalStateException("Report not available yet");
        }
        return new UrlResource(Paths.get(goal.getPerformanceReportPath()).toUri());
    }

    public List<PerformanceGoal> getGoalsForEmployee(String email) {
        return goalRepo.findByEmployeeEmail(email);
    }

    public List<PerformanceGoal> getGoalsByManager(String managerEmail) {
        return goalRepo.findByManagerEmail(managerEmail);
    }
}