package HRMS_project.page_controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import HRMS_project.entity.User;
import HRMS_project.entity.UserDetails;
import HRMS_project.entity.Role.RequestStatus;
import HRMS_project.entity.Role.Role;
import HRMS_project.repository.UserDetailsRepository;
import HRMS_project.repository.UserRepository;
import HRMS_project.service.UserDetailsService;

@Controller
@RequestMapping("/adm")
@PreAuthorize("hasRole('ADMIN')")
public class AdmUserController {
	
	@Autowired
    private UserDetailsService userService;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private UserDetailsRepository userDetailsRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("userDetailsList", userService.getAllUsersExceptAdmin());
        return "admin/users";  // View: templates/hr/userdetails.html
    }
    
    @GetMapping("users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userDetails", new UserDetails());
        model.addAttribute("roles", Role.values()); // for dropdown
        return "admin/add-user"; // Thymeleaf page (e.g., add-user.html)
    }

    // ✅ Save the User and UserDetails
    @PostMapping("users/add")
    public String addUser(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String designation,
            @RequestParam String role,
            @RequestParam String mobileNumber,
            @RequestParam String address,
            @RequestParam String baseLocation,
            @RequestParam int yearsOfExperience) {

		// Create and save User first
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.valueOf(role));
		user.setDesignation(designation);
		
		user = userRepository.saveAndFlush(user); // 🔥 Reassign to get the generated ID
		System.out.println("✅ User ID after save: " + user.getId());
		
		// Now create UserDetails
		UserDetails details = new UserDetails();
		details.setUser(user); // Automatically sets ID via @MapsId
		details.setId(user.getId()); 
		details.setMobileNumber(mobileNumber);
		details.setAddress(address);
		details.setBaseLocation(baseLocation);
		details.setYearsOfExperience(yearsOfExperience);
		details.setStatus(RequestStatus.APPROVED);
		details.setRequestedAt(LocalDateTime.now());
		userDetailsRepository.save(details);
		
		return "redirect:/adm/users";
	 }
    
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
        	userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
        }

        return "redirect:/adm/users";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserAndDetails(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
        }
        return "redirect:/adm/users";
    }
}
