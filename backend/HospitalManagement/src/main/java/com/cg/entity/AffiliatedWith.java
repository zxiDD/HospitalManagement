package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "affiliated_with")
public class AffiliatedWith {
	
	    @EmbeddedId
	    private AffiliatedWithId id;

	    @ManyToOne
	    @MapsId("physicianId")
	    @JoinColumn(name = "physician")
	    private Physician physician;

	    @ManyToOne
	    @MapsId("departmentId")
	    @JoinColumn(name = "department")
	    private Department department;
	    
		@Column(name = "primaryaffiliation", nullable = false)
	    private Boolean primaryAffiliation;

	    public AffiliatedWith() {
			super();
		}

		public AffiliatedWith(AffiliatedWithId id, Physician physician, Department department,
				Boolean primaryAffiliation) {
			super();
			this.id = id;
			this.physician = physician;
			this.department = department;
			this.primaryAffiliation = primaryAffiliation;
		}


		public AffiliatedWithId getId() {
			return id;
		}

		public void setId(AffiliatedWithId id) {
			this.id = id;
		}

		public Physician getPhysician() {
			return physician;
		}

		public void setPhysician(Physician physician) {
			this.physician = physician;
		}

		public Department getDepartment() {
			return department;
		}

		public void setDepartment(Department department) {
			this.department = department;
		}

		public Boolean getPrimaryAffiliation() {
			return primaryAffiliation;
		}

		public void setPrimaryAffiliation(Boolean primaryAffiliation) {
			this.primaryAffiliation = primaryAffiliation;
		}

	
}
