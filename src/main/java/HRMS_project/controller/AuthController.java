package HRMS_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HRMS_project.entity.User;
import HRMS_project.repository.UserRepository;
import lombok.var;

@RestController
@RequestMapping("/auth")
//@PreAuthorize("hasRole('ADMIN')")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
    	var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully");
    }
}
