package HRMS_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import HRMS_project.entity.User;
import HRMS_project.entity.UserDetails;
import HRMS_project.entity.Role.RequestStatus;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    List<UserDetails> findByStatus(RequestStatus status); // ✅ corrected

    UserDetails findByUser(User user);
    
    UserDetails findByUserEmail(String email);
    

    @Query("SELECT ud FROM UserDetails ud WHERE ud.user.role = 'MANAGER' OR ud.user.role = 'EMPLOYEE'")
    List<UserDetails> getAllUsersExceptHR(); // ✅ corrected
    
    @Query("SELECT ud FROM UserDetails ud WHERE ud.user.role = 'MANAGER' OR ud.user.role = 'EMPLOYEE' OR ud.user.role = 'HR_MANAGER'")
    List<UserDetails> getAllUsersExceptAdmin();
    
    @Query("SELECT ud FROM UserDetails ud WHERE ud.user.role = HRMS_project.entity.Role.Role.EMPLOYEE")
    List<UserDetails> getAllEmployees();
    
    @Query("SELECT ud FROM UserDetails ud WHERE ud.user.id = :userId")
    UserDetails findByUserId(Long userId);

}
