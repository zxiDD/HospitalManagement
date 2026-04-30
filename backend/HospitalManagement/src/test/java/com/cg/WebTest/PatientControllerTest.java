package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatientService patientService;

	@MockitoBean
	private PhysicianService physicianService;

	@Autowired
	private ObjectMapper objectMapper;

	private Patient patient;
	private Physician physician;

	@BeforeEach
	void setUp() {

		physician = new Physician();
		physician.setEmployeeId(10);

		patient = new Patient();
		patient.setSsn(1L);
		patient.setName("Alice");
		patient.setAddress("Delhi");
		patient.setPhone("9999999999");
		patient.setInsuranceId(100);
		patient.setPhysician(physician);
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllPatients_success() throws Exception {

		when(patientService.getAll()).thenReturn(List.of(patient));

		mockMvc.perform(get("/patients")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$[0].name").value("Alice"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getPatientById_success() throws Exception {

		when(patientService.getById(1L)).thenReturn(patient);

		mockMvc.perform(get("/patients/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Alice"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPatient_success() throws Exception {

		PatientDTO dto = new PatientDTO(1L, "Alice", "Delhi", "9999999999", 100, 10);

		when(physicianService.getById(10)).thenReturn(physician);

		when(patientService.save(any(Patient.class))).thenReturn(patient);

		mockMvc.perform(
				post("/patients").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Alice"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPatient_validationFailure() throws Exception {

		PatientDTO invalid = new PatientDTO(null, "", "", "123", null, null);

		mockMvc.perform(post("/patients").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void updatePatient_success() throws Exception {

		PatientDTO dto = new PatientDTO(1L, "Bob", "Mumbai", "8888888888", 200, 10);

		when(patientService.getById(1L)).thenReturn(patient);

		when(physicianService.getById(10)).thenReturn(physician);

		when(patientService.save(any(Patient.class))).thenReturn(patient);

		mockMvc.perform(put("/patients/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void deletePatient_success() throws Exception {

		mockMvc.perform(delete("/patients/1")).andExpect(status().isNoContent());
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/patients")).andExpect(status().isForbidden());
	}
}