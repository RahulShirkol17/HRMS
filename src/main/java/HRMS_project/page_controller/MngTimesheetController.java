package HRMS_project.page_controller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import HRMS_project.entity.Timesheet;
import HRMS_project.service.TimesheetService;

@Controller
public class MngTimesheetController {

	@Autowired
    private TimesheetService timesheetService;

    @GetMapping("/mng/timesheet")
    public String viewTimesheet(Model model, Principal principal) {
        String email = principal.getName();
        List<Timesheet> timesheetList = timesheetService.getTimesheet(email);

        boolean loggedIn = timesheetService.hasLoggedInToday(email);
        boolean loggedOut = timesheetService.hasLoggedOutToday(email);

        model.addAttribute("timesheetList", timesheetList);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedOut", loggedOut);

        return "/mng/timesheet";
    }

    @GetMapping("/mng/timesheet/login")
    public String logIn(Principal principal, RedirectAttributes redirectAttributes) {
        LocalDate today = LocalDate.now();

        if (today.getDayOfWeek() == DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY) {
            redirectAttributes.addFlashAttribute("errorMessage", "Timesheet login is disabled on weekends.");
            return "redirect:/emp/timesheet"; // ✅ Redirect properly so view model is fully loaded
        }

        String email = principal.getName();
        timesheetService.logIn(email);
        return "redirect:/mng/timesheet";
    }

    @GetMapping("/mng/timesheet/logout")
    public String logOut(Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        if (!timesheetService.hasLoggedInToday(email)) {
            redirectAttributes.addFlashAttribute("message", "You haven't logged in yet.");
        } else if (timesheetService.hasLoggedOutToday(email)) {
            redirectAttributes.addFlashAttribute("message", "Already logged out today.");
        } else {
            timesheetService.logOut(email);
            redirectAttributes.addFlashAttribute("message", "Logout successful!");
        }
        return "redirect:/mng/timesheet";
    }
}
