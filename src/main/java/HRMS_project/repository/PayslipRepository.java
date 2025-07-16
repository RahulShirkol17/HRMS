package HRMS_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.Payslip;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    List<Payslip> findByEmployeeEmail(String email);
    Optional<Payslip> findByEmployeeEmailAndMonth(String email, String month);
}