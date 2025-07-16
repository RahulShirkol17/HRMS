package HRMS_project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.EmployeeLeaveRequest;
import HRMS_project.entity.Role.LeaveStatus;
import HRMS_project.repository.EmpLeaveRequestRepository;

@Service
public class EmpLeaveService {

    @Autowired
    private EmpLeaveRequestRepository leaveRepo;

    public EmployeeLeaveRequest applyLeave(String email, EmployeeLeaveRequest request) {
        request.setEmployeeEmail(email);
        request.setStatus(LeaveStatus.PENDING);
        request.setAppliedDate(LocalDate.now());
        return leaveRepo.save(request);
    }

    public List<EmployeeLeaveRequest> getLeavesForEmployee(String email) {
        return leaveRepo.findByEmployeeEmail(email);
    }

    public List<EmployeeLeaveRequest> getPendingLeaves() {
        return leaveRepo.findByStatus(LeaveStatus.PENDING);
    }

    public EmployeeLeaveRequest reviewLeave(Long id, LeaveStatus status, String managerComment) {
        EmployeeLeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(status);
        leave.setManagerComment(managerComment);
        return leaveRepo.save(leave);
    }
}