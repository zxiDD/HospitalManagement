// PatientDashboardDTO.java
package com.cg.dto;

import java.util.List;

public class PatientDashboardDTO {

	private PatientDTO patient;

	private List<AppointmentDTO> appointments;

	private StayDTO stay;

	private List<MedicationDTO> medications;

	public PatientDashboardDTO() {
	}

	public PatientDashboardDTO(PatientDTO patient, List<AppointmentDTO> appointments, StayDTO stay,
			List<MedicationDTO> medications) {
		super();
		this.patient = patient;
		this.appointments = appointments;
		this.stay = stay;
		this.medications = medications;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public List<AppointmentDTO> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentDTO> appointments) {
		this.appointments = appointments;
	}

	public StayDTO getStay() {
		return stay;
	}

	public void setStay(StayDTO stay) {
		this.stay = stay;
	}

	public List<MedicationDTO> getMedications() {
		return medications;
	}

	public void setMedications(List<MedicationDTO> medications) {
		this.medications = medications;
	}

}