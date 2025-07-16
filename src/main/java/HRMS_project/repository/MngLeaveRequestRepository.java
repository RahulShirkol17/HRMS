package HRMS_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.ManagerLeaveRequest;
import HRMS_project.entity.Role.LeaveStatus;

@Repository
public interface MngLeaveRequestRepository extends JpaRepository<ManagerLeaveRequest, Long> {
    List<ManagerLeaveRequest> findByManagerEmail(String email);
    List<ManagerLeaveRequest> findByStatus(LeaveStatus status);
}