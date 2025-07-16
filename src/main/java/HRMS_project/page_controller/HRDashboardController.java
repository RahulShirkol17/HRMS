package HRMS_project.page_controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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

import HRMS_project.entity.JobPosting;
import HRMS_project.entity.User;
import HRMS_project.entity.UserDetails;
import HRMS_project.entity.Role.LeaveStatus;
import HRMS_project.entity.Role.RequestStatus;
import HRMS_project.entity.Role.Role;
import HRMS_project.repository.JobApplicationRepository;
import HRMS_project.repository.UserDetailsRepository;
import HRMS_project.repository.UserRepository;
import HRMS_project.service.JobPostingService;
import HRMS_project.service.MngLeaveService;
import HRMS_project.service.UserDetailsService;

@Controller
@RequestMapping("/hr")
@PreAuthorize("hasRole('HR_MANAGER')")
public class HRDashboardController {

    @Autowired
    private MngLeaveService leaveRequestService;

    @Autowired
    private UserDetailsService userService;
    
    @Autowired
    private JobPostingService jobservice;
    
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    
    @Autowired
    private MngLeaveService leaveService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "hr/dashboard";
    }

    @GetMapping("/approvals")
    public String showApprovals(Model model) {
        model.addAttribute("personalDetailRequests", userService.getPendingRequests());
        model.addAttribute("leaveRequests", leaveRequestService.getPendingLeaves());
        return "hr/approvals";  // View: templates/hr/approvals.html
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("userDetailsList", userService.getAllUsersExceptHR());
        return "hr/users";  // View: templates/hr/userdetails.html
    }
    
    @GetMapping("/job-postings")
    public String showJobPostings(Model model) {
        List<JobPosting> jobs = jobservice.getAllJobs();
        model.addAttribute("jobs", jobs);
        model.addAttribute("newJob", new JobPosting()); // For the Add form only
        return "hr/job-postings";
    }
    
    @PostMapping("/job-postings/add")
    public String addJob(@ModelAttribute JobPosting job, RedirectAttributes redirectAttributes) {
        try {
            jobservice.createJob(job);
            redirectAttributes.addFlashAttribute("success", "Job added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add job.");
        }
        return "redirect:/hr/job-postings";
    }
    
    @GetMapping("/job-postings/delete/{id}")
    public String deleteJobPosting(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobservice.deleteJob(id);
            redirectAttributes.addFlashAttribute("success", "Job posting deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete job posting.");
        }
        return "redirect:/hr/job-postings";
    }
    
    @PostMapping("/job-postings/update")
    public String updateJob(@ModelAttribute JobPosting job, RedirectAttributes redirectAttributes) {
        try {
            jobservice.updateJob(job.getId(), job);
            redirectAttributes.addFlashAttribute("success", "Job updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update job.");
        }
        return "redirect:/hr/job-postings";
    }
    
    @GetMapping("/candidates")
    public String showCandidates(Model model) {
        model.addAttribute("applications", jobApplicationRepository.findAll());
        return "hr/candidates";  // View: templates/hr/candidates.html
    }
    
    @PostMapping("/manager/leaves/review/{id}")
    public String approveLeaveFromForm(@PathVariable Long id,
                                       @RequestParam("comment") String comment,
                                       @RequestParam("status") LeaveStatus status,
                                       RedirectAttributes redirectAttributes) {
        leaveService.reviewLeave(id, status, comment);
        redirectAttributes.addFlashAttribute("successMessage",
                "Leave ID " + id + " has been " + status.name().toLowerCase() + ".");
        return "redirect:/hr/approvals"; // or wherever your HR dashboard is
    }
    
    @PostMapping("/approvals/personal/review/{id}")
    public String reviewPersonalDetail(@PathVariable Long id,
                                       @RequestParam("comment") String comment,
                                       @RequestParam("status") String status,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes) {
        if ("APPROVED".equalsIgnoreCase(status)) {
            userService.approveRequest(id, principal.getName(), comment);
            redirectAttributes.addFlashAttribute("successMessage", "Personal details approved.");
        } else if ("REJECTED".equalsIgnoreCase(status)) {
            userService.rejectRequest(id, principal.getName(), comment);
            redirectAttributes.addFlashAttribute("successMessage", "Personal details rejected.");
        }
        return "redirect:/hr/approvals";
    }

    @GetMapping("users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userDetails", new UserDetails());
        model.addAttribute("roles", Role.values()); // for dropdown
        return "hr/add-user"; // Thymeleaf page (e.g., add-user.html)
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
		
		return "redirect:/hr/users";
	 }
    
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
        	userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
        }

        return "redirect:/hr/users";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserAndDetails(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
        }
        return "redirect:/hr/users";
    }

}
