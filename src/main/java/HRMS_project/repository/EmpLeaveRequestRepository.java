package HRMS_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.EmployeeLeaveRequest;
import HRMS_project.entity.Role.LeaveStatus;

@Repository
public interface EmpLeaveRequestRepository extends JpaRepository<EmployeeLeaveRequest, Long> {
    List<EmployeeLeaveRequest> findByEmployeeEmail(String email);
    List<EmployeeLeaveRequest> findByStatus(LeaveStatus status);
}