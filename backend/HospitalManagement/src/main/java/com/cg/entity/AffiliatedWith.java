package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "affiliated_with")
public class AffiliatedWith {
	
	    @EmbeddedId
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

	
}
