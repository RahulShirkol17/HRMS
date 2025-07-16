package HRMS_project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import HRMS_project.entity.PersonalDetailsRequest;
import HRMS_project.entity.Role.RequestStatus;

public interface PersonalDetailsRequestRepository extends JpaRepository<PersonalDetailsRequest, Long> {
    List<PersonalDetailsRequest> findByStatus(RequestStatus status);
}
