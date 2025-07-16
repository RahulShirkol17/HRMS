package HRMS_project.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import HRMS_project.entity.Role.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "personal_details_request")
public class PersonalDetailsRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personal_seq")
	@SequenceGenerator(name = "personal_seq", sequenceName = "personal_sequence")
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

    private String mobileNumber;
    private String address;
    private String baseLocation;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int yearsOfExperience;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    @Column(name = "\"comment\"")
    private String comment;
}
