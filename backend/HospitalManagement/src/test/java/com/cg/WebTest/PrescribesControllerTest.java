package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.controller.PrescribesController;
import com.cg.dto.PrescribesDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Medication;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.entity.Prescribes;
import com.cg.entity.PrescribesId;
import com.cg.service.AppointmentService;
import com.cg.service.MedicationService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.PrescribesService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PrescribesControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PrescribesService prescribesService;

	@MockitoBean
	private PhysicianService physicianService;

	@MockitoBean
	private PatientService patientService;

	@MockitoBean
	private MedicationService medicationService;

	@MockitoBean
	private AppointmentService appointmentService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPrescription_success() throws Exception {

		PrescribesDTO input = new PrescribesDTO(1, 100L, 10, LocalDateTime.of(2024, 1, 1, 10, 0), "2 times a day", 500);

		Prescribes prescription = new Prescribes();

		PrescribesId id = new PrescribesId(1, 100L, 10, LocalDateTime.of(2024, 1, 1, 10, 0));

		Physician physician = new Physician();
		physician.setEmployeeId(1);

		Patient patient = new Patient();
		patient.setSsn(100L);

		Medication medication = new Medication();
		medication.setCode(10);

		Appointment appointment = new Appointment();
		appointment.setAppointmentID(500);

		prescription.setId(id);
		prescription.setPhysician(physician);
		prescription.setPatient(patient);
		prescription.setMedication(medication);
		prescription.setAppointment(appointment);
		prescription.setDose("2 times a day");

		when(prescribesService.save(any())).thenReturn(prescription);

		mockMvc.perform(post("/admin/prescribes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.physicianId").value(1)).andExpect(jsonPath("$.patientSsn").value(100))
				.andExpect(jsonPath("$.medicationId").value(10));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPrescription_validationFailure() throws Exception {

		PrescribesDTO invalid = new PrescribesDTO(null, null, null, null, null, null);

		mockMvc.perform(post("/admin/prescribes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllPrescriptions_success() throws Exception {

		Prescribes prescription = new Prescribes();

		PrescribesId id = new PrescribesId(1, 100L, 10, LocalDateTime.of(2024, 1, 1, 10, 0));

		Physician physician = new Physician();
		physician.setEmployeeId(1);

		Patient patient = new Patient();
		patient.setSsn(100L);

		Medication medication = new Medication();
		medication.setCode(10);

		prescription.setId(id);
		prescription.setPhysician(physician);
		prescription.setPatient(patient);
		prescription.setMedication(medication);
		prescription.setDose("2 times a day");

		when(prescribesService.getAll()).thenReturn(List.of(prescription));

		mockMvc.perform(get("/prescribes")).andExpect(status().isOk());
	}
}