package HRMS_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.PerformanceGoal;

@Repository
public interface PerformanceGoalRepository extends JpaRepository<PerformanceGoal, Long> {
    List<PerformanceGoal> findByEmployeeEmail(String email);
    List<PerformanceGoal> findByManagerEmail(String email);
}