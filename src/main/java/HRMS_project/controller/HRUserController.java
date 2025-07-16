package HRMS_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import HRMS_project.entity.UserDetails;
import HRMS_project.service.UserDetailsService;
import HRMS_project.service.UserService;

@Controller
@RequestMapping("/hr")
@PreAuthorize("hasRole('HR_MANAGER')")
public class HRUserController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/users/view")
    public String listUsers(Model model) {
        List<UserDetails> users = userDetailsService.getAllUsersExceptHR();
        model.addAttribute("users", users);
        return "hr/userdetails";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserDetails userDetails = userDetailsService.getUserDetailsById(id);
        if (userDetails == null) {
            return "redirect:/hr/users?error=UserNotFound";
        }
        model.addAttribute("userDetails", userDetails);
        return "hr/edit-user";
    }

    @PostMapping("/user/update")
    public String updateUser(@RequestParam("userId") Long userId,
                             @ModelAttribute("userDetails") UserDetails userDetails) {
        userDetailsService.saveUserDetailsWithUser(userId, userDetails);
        return "redirect:/hr/users";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userDetailsService.deleteUserDetails(id);
        return "redirect:/hr/users";
    }

    @GetMapping("/user/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDetails", new UserDetails());
        model.addAttribute("allUsers", userService.getAllUsersExceptHR()); // for dropdown selection
        return "hr/add-user";
    }

    @PostMapping("/user/add")
    public String addUser(@RequestParam("userId") Long userId,
                          @ModelAttribute("userDetails") UserDetails userDetails) {
        userDetailsService.saveUserDetailsWithUser(userId, userDetails);
        return "redirect:/hr/users";
    }
}
