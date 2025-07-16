package HRMS_project.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import HRMS_project.entity.PersonalDetailsRequest;
import HRMS_project.entity.Role.RequestStatus;
import HRMS_project.repository.PersonalDetailsRequestRepository;

@Service
public class PersonalDetailService {

    @Autowired
    private PersonalDetailsRequestRepository requestRepository;

    public List<PersonalDetailsRequest> getPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    public void approveRequest(Long id) {
        PersonalDetailsRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedAt(LocalDateTime.now());
        requestRepository.save(request);
    }

    public void rejectRequest(Long id, String comments) {
        PersonalDetailsRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        request.setComment(comments);
        request.setReviewedAt(LocalDateTime.now());
        requestRepository.save(request);
    }
}
