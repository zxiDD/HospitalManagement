package com.cg.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "undergoes")
public class Undergoes {
	@EmbeddedId
	private UndergoesId id;

	@ManyToOne
	@MapsId("patient")
	@JoinColumn(name = "patient", nullable = false)
	private Patient patient;

	@ManyToOne
	@MapsId("procedures")
	@JoinColumn(name = "procedures", nullable = false)
	private Procedures procedures;

	@ManyToOne
	@MapsId("stay")
	@JoinColumn(name = "stay", nullable = false)
	private Stay stay;

	@ManyToOne
	@JoinColumn(name = "physician")
	private Physician physician;

	@ManyToOne
	@JoinColumn(name = "assistingnurse")
	private Nurse assistingNurse;

	public Undergoes() {

	}

	public Undergoes(UndergoesId id, Patient patient, Procedures procedures, Stay stay, Physician physician,
			Nurse assistingNurse) {
		super();
		this.id = id;
		this.patient = patient;
		this.procedures = procedures;
		this.stay = stay;
		this.physician = physician;
		this.assistingNurse = assistingNurse;
	}

	public UndergoesId getId() {
		return id;
	}

	public void setId(UndergoesId id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Procedures getProcedures() {
		return procedures;
	}

	public void setProcedures(Procedures procedures) {
		this.procedures = procedures;
	}

	public Stay getStay() {
		return stay;
	}

	public void setStay(Stay stay) {
		this.stay = stay;
	}

	public Physician getPhysician() {
		return physician;
	}

	public void setPhysician(Physician physician) {
		this.physician = physician;
	}

	public Nurse getAssistingNurse() {
		return assistingNurse;
	}

	public void setAssistingNurse(Nurse assistingNurse) {
		this.assistingNurse = assistingNurse;
	}
}
