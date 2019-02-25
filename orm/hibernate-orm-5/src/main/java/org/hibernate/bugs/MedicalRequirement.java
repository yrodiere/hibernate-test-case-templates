package org.hibernate.bugs;

import javax.persistence.*;

@Entity
public class MedicalRequirement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "medicalRequirementId", nullable = false)
	Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "applicationId")
	private Application application;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MedicalRequirementType type;

	@Column(nullable = false)
	private String source;

	public MedicalRequirement( Application application, MedicalRequirementType type, String source ) {
		this.application = application;
		this.type = type;
		this.source = source;
	}

	public MedicalRequirement() {
		// for JPA
	}
}
