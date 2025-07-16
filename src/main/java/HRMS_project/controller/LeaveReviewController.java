package HRMS_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.ManagerLeaveRequest;
import HRMS_project.entity.Role.LeaveStatus;
import HRMS_project.service.MngLeaveService;

@RestController
@RequestMapping("/hr/manager/leaves")
@PreAuthorize("('HR_MANAGER')")
public class LeaveReviewController {

    @Autowired
    private MngLeaveService leaveService;

    @GetMapping("/pending")
    public List<ManagerLeaveRequest> getPendingLeaves() {
        return leaveService.getPendingLeaves();
    }

    @PostMapping("/reviews/{id}")
    public ResponseEntity<ManagerLeaveRequest> reviewLeave(@PathVariable Long id,
                                                    @RequestParam LeaveStatus status,
                                                    @RequestParam String comment) {
        return new ResponseEntity<>(leaveService.reviewLeave(id, status, comment), HttpStatus.OK);
    }
}