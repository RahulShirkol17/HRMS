package HRMS_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.Timesheet;
import HRMS_project.service.TimesheetService;

@RestController
@RequestMapping("/employee/timesheet")
@PreAuthorize("hasRole('EMPLOYEE')")
public class TimesheetControllers {

    @Autowired
    private TimesheetService timesheetService;

    @PostMapping("/login")
    public ResponseEntity<Timesheet> logIn(@RequestParam String email) {
        return new ResponseEntity<>(timesheetService.logIn(email), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<Timesheet> logOut(@RequestParam String email) {
        return new ResponseEntity<>(timesheetService.logOut(email), HttpStatus.OK);
    }

    @GetMapping("/history")
    public List<Timesheet> getTimesheetHistory(@RequestParam String email) {
        return timesheetService.getTimesheet(email);
    }
}