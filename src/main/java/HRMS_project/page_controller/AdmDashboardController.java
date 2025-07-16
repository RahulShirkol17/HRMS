package HRMS_project.page_controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adm")
@PreAuthorize("hasRole('ADMIN')")
public class AdmDashboardController {
	
	

    @GetMapping("/dashboard")
    //@PreAuthorize("hasRole('ADMIN')")
    public String showRegisterPage() {
        return "admin/dashboard"; // Loads register.html
    }
}
