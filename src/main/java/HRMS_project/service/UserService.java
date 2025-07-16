package HRMS_project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.User;
import HRMS_project.entity.Role.Role;
import HRMS_project.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersExceptHR() {
        return userRepository.findAll()
            .stream()
            .filter(user -> user.getRole() != Role.HR_MANAGER)
            .toList();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
