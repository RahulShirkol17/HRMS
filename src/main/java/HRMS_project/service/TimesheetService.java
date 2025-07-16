package HRMS_project.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.Timesheet;
import HRMS_project.repository.TimesheetRepository;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepo;

    public Timesheet logIn(String email) {
        LocalDate today = LocalDate.now();
        Timesheet sheet = timesheetRepo.findByEmployeeEmailAndDate(email, today)
                .orElse(new Timesheet());

        sheet.setEmployeeEmail(email);
        sheet.setDate(today);
        sheet.setLoginTime(LocalTime.now());
        sheet.setStatus("Working");

        return timesheetRepo.save(sheet);
    }

    public Timesheet logOut(String email) {
        Timesheet sheet = timesheetRepo.findByEmployeeEmailAndDate(email, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Login record not found"));

        sheet.setLogoutTime(LocalTime.now());
        sheet.setWorkingDuration(Duration.between(sheet.getLoginTime(), sheet.getLogoutTime()));

        return timesheetRepo.save(sheet);
    }

    public List<Timesheet> getTimesheet(String email) {
        return timesheetRepo.findByEmployeeEmail(email);
    }
    
    public boolean hasLoggedInToday(String email) {
        return timesheetRepo.findByEmployeeEmailAndDate(email, LocalDate.now()).isPresent();
    }

    public boolean hasLoggedOutToday(String email) {
        return timesheetRepo.findByEmployeeEmailAndDate(email, LocalDate.now())
                .map(ts -> ts.getLogoutTime() != null)
                .orElse(false);
    }
}