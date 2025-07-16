package HRMS_project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.JobPosting;
import HRMS_project.entity.PersonalDetailsRequest;
import HRMS_project.entity.User;
import HRMS_project.entity.UserDetails;
import HRMS_project.entity.Role.RequestStatus;
import HRMS_project.repository.JobPostingRepository;
import HRMS_project.repository.PersonalDetailsRequestRepository;
import HRMS_project.repository.UserDetailsRepository;
import HRMS_project.repository.UserRepository;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobPostingRepository jobPostingRepository;
    
    @Autowired
    private PersonalDetailsRequestRepository requestrepo;

    public List<UserDetails> getAllUserDetails() {
        return userDetailsRepository.findAll();
    }

    public List<UserDetails> getAllUsersExceptHR() {
        return userDetailsRepository.getAllUsersExceptHR();
    }
    public List<UserDetails> getAllUsersExceptAdmin() {
        return userDetailsRepository.getAllUsersExceptAdmin();
    }
    
    public List<UserDetails> getAllEmployees() {
        return userDetailsRepository.getAllEmployees();
    }

    public UserDetails getUserDetailsById(Long id) {
        return userDetailsRepository.findById(id).orElse(null);
    }
    
    public PersonalDetailsRequest getUserDetailById(Long id) {
        return requestrepo.findById(id).orElse(null);
    }
    
    public UserDetails findByEmail(String email) {
        return userDetailsRepository.findByUserEmail(email).orElse(null);
    }
//    public UserDetails getUserDetailsByUser(User user) {
//        return userDetailsRepository.findByUser(user);
//    }

    public UserDetails saveUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }
    
    public PersonalDetailsRequest saveUserDetails(PersonalDetailsRequest userDetails) {
        return requestrepo.save(userDetails);
    }

    public UserDetails saveUserDetailsWithUser(Long userId, UserDetails userDetails) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        userDetails.setUser(user);
        return userDetailsRepository.save(userDetails);
    }

    public void deleteUserDetails(Long id) {
        userDetailsRepository.deleteById(id);
    }

    public List<PersonalDetailsRequest> getPendingRequests() {
        return requestrepo.findByStatus(RequestStatus.PENDING);
    }
    
//    public void approveRequest(Long id,  String reviewerEmail, String comment) {
//    	PersonalDetailsRequest details = requestrepo.findById(id)
//            .orElseThrow(() -> new RuntimeException("PersonalDetailsRequest not found with ID: " + id));
//    	userDetailsRepository.deleteById(id);
//    	UserDetails update = new UserDetails(details);
//    	update.setStatus(RequestStatus.APPROVED);
//        details.setReviewedAt(LocalDateTime.now());
//        details.setReviewedBy(reviewerEmail);
//        details.setComment(comment); // Assuming you have a 'comment' field in UserDetails
//        userDetailsRepository.save(update);
//        requestrepo.deleteById(id);
//    }
    
    public void approveRequest(Long requestId, String reviewerEmail, String comment) {
        PersonalDetailsRequest request = requestrepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("PersonalDetailsRequest not found with ID: " + requestId));

        // Find existing UserDetails for the user
        User user = request.getUser();
        UserDetails userDetails = userDetailsRepository.findByUser(user);
        if (userDetails == null) {
            userDetails = new UserDetails(); // First time approval
            userDetails.setUser(user);
        }

        // Update UserDetails with approved data
        userDetails.setMobileNumber(request.getMobileNumber());
        userDetails.setAddress(request.getAddress());
        userDetails.setBaseLocation(request.getBaseLocation());
        userDetails.setYearsOfExperience(request.getYearsOfExperience());
        userDetails.setStatus(RequestStatus.APPROVED);
        userDetails.setReviewedAt(LocalDateTime.now());
        userDetails.setReviewedBy(reviewerEmail);
        userDetails.setComment(comment);

        userDetailsRepository.save(userDetails);

        // Cleanup: Delete the request
        requestrepo.deleteById(requestId);
    }


    public void rejectRequest(Long id,  String reviewerEmail, String comment) {
    	PersonalDetailsRequest details = requestrepo.findById(id)
            .orElseThrow(() -> new RuntimeException("PersonalDetailsRequest not found with ID: " + id));
        details.setStatus(RequestStatus.REJECTED);
        details.setReviewedAt(LocalDateTime.now());
        details.setReviewedBy(reviewerEmail);
        details.setComment(comment); // Assuming 'comment' field exists
        requestrepo.save(details);
    }
    
    public void updateUser(User dto) {
        // Fetch and update User entity
        User user = userRepository.findById(dto.getId()).orElseThrow();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setDesignation(dto.getDesignation());
        user.setRole(dto.getRole());
        userRepository.save(user);

        // Fetch UserDetails by userId
        UserDetails details = userDetailsRepository.findByUserId(dto.getId());
        UserDetails updated = dto.getUserDetails();

        // If no UserDetails exists, create new and set fields
        if (details == null) {
            details = new UserDetails();
            details.setUser(user); // Set relationship
            if (updated != null) {
                details.setMobileNumber(updated.getMobileNumber());
                details.setAddress(updated.getAddress());
                details.setBaseLocation(updated.getBaseLocation());
                details.setYearsOfExperience(updated.getYearsOfExperience());
            }
        } else {
            // If UserDetails exists, update the fields
            if (updated != null) {
                details.setMobileNumber(updated.getMobileNumber());
                details.setAddress(updated.getAddress());
                details.setBaseLocation(updated.getBaseLocation());
                details.setYearsOfExperience(updated.getYearsOfExperience());
            }
        }

        // Save UserDetails
        userDetailsRepository.save(details);
    }
    
    public void deleteUserAndDetails(Long userId) {
        // Delete UserDetails first to avoid constraint violations
        UserDetails details = userDetailsRepository.findByUserId(userId);
        if (details != null) {
            userDetailsRepository.delete(details);
        }

        // Then delete the User
        userRepository.deleteById(userId);
    }
    
    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }

    // ✅ Add a new job
    public JobPosting addJob(JobPosting job) {
        return jobPostingRepository.save(job);
    }

    // ✅ Get a job by ID
    public JobPosting getJobById(Long id) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        return jobOpt.orElse(null);
    }

    // ✅ Update a job posting
    public void updateJob(JobPosting job) {
        jobPostingRepository.save(job); // save() handles both insert & update
    }

    // ✅ Delete job by ID
    public void deleteJob(Long id) {
        jobPostingRepository.deleteById(id);
    }
    
    public UserDetails getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email)
                             .map(userDetailsRepository::findByUser)
                             .orElse(null);
    }
}
