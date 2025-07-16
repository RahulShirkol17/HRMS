package HRMS_project.page_controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import HRMS_project.entity.PersonalDetailsRequest;
import HRMS_project.entity.User;
import HRMS_project.entity.UserDetails;
import HRMS_project.entity.Role.RequestStatus;
import HRMS_project.service.UserDetailsService;
import HRMS_project.service.UserService;

@Controller
@RequestMapping("/mng")
@PreAuthorize("hasRole('MANAGER')")
public class MngDashboardController {

	@Autowired
    private UserDetailsService userService;
	
	@Autowired
    private UserService userservice;
	
	@GetMapping("/dashboard")
    public String showDashboard() {
        return "mng/dashboard";
    }
    
    @GetMapping("/personal-details-mng")
    public String showEmployeeDetails(Model model, Principal principal) {
        String email = principal.getName();

        User user = userservice.findByEmail(email); // required user
        UserDetails details = userService.getUserDetailsByEmail(email); // assumes fetching via user

        if (details == null) {
            details = new UserDetails();
            details.setYearsOfExperience(0); // default value
        }

        details.setUser(user); // ✅ always set user

        model.addAttribute("userDetails", details);
        return "mng/personal-details-mng";
    }
    
    @PostMapping("/personal-details/update")
    public String submitPersonalDetailsUpdate(@ModelAttribute PersonalDetailsRequest personalDetails, Principal principal) {
        String email = principal.getName();
        User user = userservice.findByEmail(email);

        PersonalDetailsRequest existing = null;

        if (personalDetails.getId() != null) {
            existing = userService.getUserDetailById(personalDetails.getId());
        }

        if (existing == null) {
            existing = new PersonalDetailsRequest();
            existing.setUser(user);
        }

        // Update values
        existing.setMobileNumber(personalDetails.getMobileNumber());
        existing.setAddress(personalDetails.getAddress());
        existing.setBaseLocation(personalDetails.getBaseLocation());
        existing.setYearsOfExperience(personalDetails.getYearsOfExperience());
        existing.setStatus(RequestStatus.PENDING);

        userService.saveUserDetails(existing);

        return "redirect:/mng/personal-details-mng?success";
    }
}
