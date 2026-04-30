package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.cg.dto.AppointmentDTO;
import com.cg.dto.NurseDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.service.AppointmentService;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AppointmentService appointmentService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAll() throws Exception {
		Appointment appointment = createTestAppointment();
		when(appointmentService.getAllAppointments()).thenReturn(List.of(appointment));

		mockMvc.perform(get("/appointments")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].appointmentID").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetById_Found() throws Exception {
		Appointment appointment = createTestAppointment();
		when(appointmentService.getAppointmentById(1)).thenReturn(appointment);

		mockMvc.perform(get("/appointments/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.appointmentID").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetById_NotFound() throws Exception {
		when(appointmentService.getAppointmentById(999)).thenReturn(null);

		mockMvc.perform(get("/appointments/999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetPatientsByPhysician() throws Exception {
		Patient patient = createTestPatient();
		when(appointmentService.getPatientsByPhysician(100)).thenReturn(List.of(patient));

		mockMvc.perform(get("/appointments/physician/100/patients")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].ssn").value(12345));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAppointmentsByPhysician() throws Exception {
		Appointment appointment = createTestAppointment();
		when(appointmentService.getByPhysicianId(100)).thenReturn(List.of(appointment));

		mockMvc.perform(get("/appointments/physician/100/appointments")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].appointmentID").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testAddAppointment_Success() throws Exception {
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, 50, LocalDateTime.now().plusHours(1),
				LocalDateTime.now().plusHours(2), "Room101");

		Appointment appointment = createTestAppointment();
		when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);

		mockMvc.perform(post("/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.appointmentID").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testAddAppointment_InvalidTime() throws Exception {
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, 50, LocalDateTime.now().plusHours(3),
				LocalDateTime.now().plusHours(1), "Room101");

		mockMvc.perform(post("/appointments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testReschedule_Success() throws Exception {
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, 50, LocalDateTime.now().plusDays(1),
				LocalDateTime.now().plusDays(1).plusHours(1), "Room101");

		Appointment appointment = createTestAppointment();
		when(appointmentService.getAppointmentById(1)).thenReturn(appointment);
		when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);

		mockMvc.perform(post("/appointments/reschedule").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testReschedule_InvalidTimeRange() throws Exception {
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, 50, LocalDateTime.now().plusDays(1).plusHours(2),
				LocalDateTime.now().plusDays(1), "Room101");

		Appointment appointment = createTestAppointment();
		when(appointmentService.getAppointmentById(1)).thenReturn(appointment);

		mockMvc.perform(post("/appointments/reschedule").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testReschedule_PastTime() throws Exception {
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, 50, LocalDateTime.now().minusHours(1),
				LocalDateTime.now(), "Room101");

		Appointment appointment = createTestAppointment();
		when(appointmentService.getAppointmentById(1)).thenReturn(appointment);

		mockMvc.perform(post("/appointments/reschedule").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAppointmentsForPatient() throws Exception {
		Appointment appointment = createTestAppointment();
		when(appointmentService.getByPatientId(12345L)).thenReturn(List.of(appointment));

		mockMvc.perform(get("/appointments/patients/12345/appointments")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].appointmentID").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testAssignPrepNurse() throws Exception {
		NurseDTO nurseDTO = new NurseDTO();
		nurseDTO.setEmployeeId(75);

		Appointment appointment = createTestAppointment();
		when(appointmentService.assignPrepNurse(1, 75)).thenReturn(appointment);

		mockMvc.perform(patch("/appointments/appointments/1/nurse").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nurseDTO))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAll_Empty() throws Exception {
		when(appointmentService.getAllAppointments()).thenReturn(List.of());

		mockMvc.perform(get("/appointments")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAppointmentsForPatient_Empty() throws Exception {
		when(appointmentService.getByPatientId(999L)).thenReturn(List.of());

		mockMvc.perform(get("/appointments/patients/999/appointments")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetPatientsByPhysician_Empty() throws Exception {
		when(appointmentService.getPatientsByPhysician(999)).thenReturn(List.of());

		mockMvc.perform(get("/appointments/physician/999/patients")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());
	}

	private Appointment createTestAppointment() {
		Patient patient = createTestPatient();
		Physician physician = new Physician();
		physician.setEmployeeId(100);
		physician.setName("Dr. Smith");

		Nurse nurse = new Nurse();
		nurse.setEmployeeId(50);
		nurse.setName("Nurse Joy");

		Appointment appointment = new Appointment();
		appointment.setAppointmentID(1);
		appointment.setPatient(patient);
		appointment.setPhysician(physician);
		appointment.setPrepNurse(nurse);
		appointment.setStarto(LocalDateTime.now().plusHours(1));
		appointment.setEndo(LocalDateTime.now().plusHours(2));
		appointment.setExaminationRoom("Room101");

		return appointment;
	}

	private Patient createTestPatient() {
		Patient patient = new Patient();
		patient.setSsn(12345L);
		patient.setName("John Doe");
		patient.setAddress("123 Main St");
		patient.setPhone("555-1234");
		patient.setInsuranceId(100);
		return patient;
	}
}