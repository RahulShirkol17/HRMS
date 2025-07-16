package HRMS_project.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import HRMS_project.entity.Timesheet;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    Optional<Timesheet> findByEmployeeEmailAndDate(String email, LocalDate date);

    List<Timesheet> findByEmployeeEmail(String email);
}