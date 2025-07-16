package HRMS_project.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.EmployeeLeaveRequest;
import HRMS_project.service.EmpLeaveService;

@RestController
@RequestMapping("/employee/leaves")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeLeaveController {

    @Autowired
    private EmpLeaveService leaveService;

    @PostMapping("/apply")
    public ResponseEntity<EmployeeLeaveRequest> applyLeave(@RequestBody EmployeeLeaveRequest request,
                                                   @RequestParam String email) {
        return new ResponseEntity<>(leaveService.applyLeave(email, request), HttpStatus.CREATED);
    }

    @GetMapping("/status")
    public List<EmployeeLeaveRequest> getMyLeaves(@RequestParam String email) {
        return leaveService.getLeavesForEmployee(email);
    }
}