package org.hibernate.bugs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
class Application {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "applicationId" )
	private Long id;

	@Enumerated( EnumType.STRING )
	private ApplicationType applicationType = ApplicationType.UNI5;

	@OneToMany( mappedBy = "application", cascade = { CascadeType.ALL }, orphanRemoval = true )
	private List<MedicalRequirement> requirements;

	public Application( ApplicationType applicationType, MedicalRequirementType... requirements ) {
		this.applicationType = applicationType;
		this.requirements = new ArrayList<>(  );

		for ( MedicalRequirementType requirementType : requirements )
			addRequirement( requirementType );
	}

	Application() {
		// for persistence
	}

	public void addRequirement( MedicalRequirementType type ) {
		this.requirements.add( new MedicalRequirement( this, type, "G" ) );
	}
}
