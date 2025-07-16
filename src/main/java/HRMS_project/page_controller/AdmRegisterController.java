package HRMS_project.page_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdmRegisterController {
	
	@GetMapping("/adm/register")
    //@PreAuthorize("hasRole('ADMIN')")
    public String showRegisterPage() {
        return "admin/register"; // Loads register.html
    }
}
