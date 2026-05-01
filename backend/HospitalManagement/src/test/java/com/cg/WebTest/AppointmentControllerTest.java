package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
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
import com.cg.entity.OnCall;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.repo.AppointmentRepository;
import com.cg.repo.NurseRepository;
import com.cg.service.AppointmentService;
import com.cg.service.NurseService;
import com.cg.service.OnCallService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AppointmentService appointmentService;

	@MockitoBean
	private NurseService nurseService;

	@MockitoBean
	private OnCallService onCallService;
	
	@MockitoBean
	private PatientService patientService;
	
	@MockitoBean
	private PhysicianService physicianService;

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
		AppointmentDTO dto = new AppointmentDTO(1, 12345, 100, null, LocalDateTime.now().plusHours(1),
				LocalDateTime.now().plusHours(2), "Room101");
		Patient patient = createTestPatient();
		
		when(patientService.getById(12345L)).thenReturn(patient);

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
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(jsonPath("$.errMsg").value("Invalid time range"));
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
		Appointment appointment = createTestAppointment();
		when(appointmentService.assignPrepNurse(1, 75)).thenReturn(appointment);
		when(nurseService.getById(75)).thenReturn(createTestNurse());

		mockMvc.perform(patch("/appointments/1/75")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void assignPrepNurse_onCall_shouldSucceed() {

		Nurse nurse = createTestNurse();

		nurse.setEmployeeId(75);

		Appointment appointment = createTestAppointment();

		appointment.setAppointmentID(1);

		OnCall onCall = new OnCall();

		onCall.setNurse(nurse);
		onCall.setOnCallStart(appointment.getStarto().minusMinutes(1));
		onCall.setOnCallEnd(appointment.getEndo().plusMinutes(1));

		Appointment updatedAppointment = createTestAppointment();

		updatedAppointment.setAppointmentID(1);
		updatedAppointment.setPrepNurse(nurse);

		when(nurseService.save(any(Nurse.class))).thenReturn(nurse);

		when(appointmentService.save(any(Appointment.class))).thenReturn(appointment);

		when(onCallService.save(any(OnCall.class))).thenReturn(onCall);

		when(appointmentService.assignPrepNurse(1, 75)).thenReturn(updatedAppointment);

		Nurse savedNurse = nurseService.save(nurse);

		Appointment savedAppointment = appointmentService.save(appointment);

		onCallService.save(onCall);

		Appointment updated = appointmentService.assignPrepNurse(savedAppointment.getAppointmentID(),
				savedNurse.getEmployeeId());

		Assertions.assertNotNull(updated);

		Assertions.assertEquals(75, updated.getPrepNurse().getEmployeeId());
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
		physician.setName("Smith");

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
		patient.setPhone("9898989898");
		patient.setInsuranceId(100);
		return patient;
	}

	private Nurse createTestNurse() {
		Nurse nurse = new Nurse();
		nurse.setEmployeeId(75);
		nurse.setName("Nurse Mary");
		nurse.setPosition("Nurse");
		nurse.setSsn(12345L);
		return nurse;
	}
}