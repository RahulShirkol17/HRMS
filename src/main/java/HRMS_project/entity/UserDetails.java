package HRMS_project.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import HRMS_project.entity.Role.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_details")
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "details_seq")
	@SequenceGenerator(name = "details_seq", sequenceName = "details_sequence")
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

    private String mobileNumber;
    private String address;
    private String baseLocation;
    private int yearsOfExperience;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    @Column(name = "\"comment\"")
    private String comment;
    
    public UserDetails(PersonalDetailsRequest request) {
        this.user = request.getUser();
        this.mobileNumber = request.getMobileNumber();
        this.address = request.getAddress();
        this.baseLocation = request.getBaseLocation();
        this.yearsOfExperience = request.getYearsOfExperience();
        this.status = RequestStatus.APPROVED;
        this.reviewedAt = LocalDateTime.now();
        this.reviewedBy = request.getReviewedBy();
        this.comment = request.getComment();
    }
    
    public UserDetails() {
    }

	public UserDetails orElse(UserDetails userDetails) {
		// TODO Auto-generated method stub
		return null;
	}
}
