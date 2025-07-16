package HRMS_project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.ManagerLeaveRequest;
import HRMS_project.entity.Role.LeaveStatus;
import HRMS_project.repository.MngLeaveRequestRepository;

@Service
public class MngLeaveService {

    @Autowired
    private MngLeaveRequestRepository leaveRepo;

    public ManagerLeaveRequest applyLeave(String managerEmail, ManagerLeaveRequest request) {
        request.setManagerEmail(managerEmail);
        request.setStatus(LeaveStatus.PENDING);
        request.setAppliedDate(LocalDate.now());
        return leaveRepo.save(request);
    }

    public List<ManagerLeaveRequest> getLeavesForManager(String managerEmail) {
        return leaveRepo.findByManagerEmail(managerEmail);
    }

    public List<ManagerLeaveRequest> getPendingLeaves() {
        return leaveRepo.findByStatus(LeaveStatus.PENDING);
    }

    public ManagerLeaveRequest reviewLeave(Long id, LeaveStatus status, String hrComment) {
        ManagerLeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(status);
        leave.setHrComment(hrComment);
        return leaveRepo.save(leave);
    }
}
