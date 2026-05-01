package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.StayDTO;
import com.cg.dto.UndergoesDTO;

import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.entity.Procedures;
import com.cg.entity.Room;
import com.cg.entity.Stay;
import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;
import com.cg.exception.ResourceNotFoundException;

import com.cg.service.NurseService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.ProceduresService;
import com.cg.service.RoomService;
import com.cg.service.StayService;
import com.cg.service.UndergoesService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UndergoesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UndergoesService undergoesService;

	@MockitoBean
	private PatientService patientService;

	@MockitoBean
	private ProceduresService proceduresService;

	@MockitoBean
	private StayService stayService;

	@MockitoBean
	private NurseService nurseService;

	@MockitoBean
	private RoomService roomService;

	@MockitoBean
	private PhysicianService physicianService;

	@Autowired
	private ObjectMapper objectMapper;

	private Undergoes createTestUndergoes() {

		Patient patient = new Patient();

		patient.setSsn(12345L);

		Procedures procedure = new Procedures();

		procedure.setCode(101);

		Room room = new Room();

		room.setRoomNumber(201);

		Stay stay = new Stay();

		stay.setStayId(1);
		stay.setPatient(patient);
		stay.setRoom(room);

		Physician physician = new Physician();

		physician.setEmployeeId(100);

		Nurse nurse = new Nurse();

		nurse.setEmployeeId(50);

		UndergoesId id = new UndergoesId(12345L, 101, 1, LocalDateTime.now());

		Undergoes undergoes = new Undergoes();

		undergoes.setId(id);
		undergoes.setPatient(patient);
		undergoes.setProcedures(procedure);
		undergoes.setStay(stay);
		undergoes.setPhysician(physician);
		undergoes.setAssistingNurse(nurse);

		return undergoes;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllUndergoes_success() throws Exception {

		Undergoes undergoes = createTestUndergoes();

		when(undergoesService.getAllUndergoes()).thenReturn(List.of(undergoes));

		mockMvc.perform(get("/undergoes")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByPatient_success() throws Exception {

		Undergoes undergoes = createTestUndergoes();

		when(undergoesService.getUndergoesByPatient(12345L)).thenReturn(List.of(undergoes));

		mockMvc.perform(get("/undergoes/patient/12345")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByProcedure_success() throws Exception {

		when(undergoesService.getUndergoesByProcedure(101)).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/procedure/101")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByStay_success() throws Exception {

		when(undergoesService.getUndergoesByStay(1)).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/stay/1")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByPhysician_success() throws Exception {

		when(undergoesService.getUndergoesByPhysician(100)).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/physician/100")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByNurse_success() throws Exception {

		when(undergoesService.getUndergoesByAssistingNurse(50)).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/nurse/50")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByDate_success() throws Exception {

		when(undergoesService.getUndergoesByDate(any())).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/date").param("date", "2024-01-01T10:00:00")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getBetweenDates_success() throws Exception {

		when(undergoesService.getUndergoesBetweenDates(any(), any())).thenReturn(List.of(createTestUndergoes()));

		mockMvc.perform(get("/undergoes/date-range").param("startDate", "2024-01-01T10:00:00").param("endDate",
				"2024-01-10T10:00:00")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createUndergoes_success() throws Exception {

		UndergoesDTO dto = new UndergoesDTO(12345L, 101, 1, LocalDateTime.now(), 100, 50);

		Patient patient = new Patient();

		patient.setSsn(12345L);

		Procedures procedure = new Procedures();

		procedure.setCode(101);

		StayDTO stayDTO = new StayDTO();

		stayDTO.setStayId(1);
		stayDTO.setRoomNumber(201);

		Room room = new Room();

		room.setRoomNumber(201);

		Physician physician = new Physician();

		physician.setEmployeeId(100);

		Nurse nurse = new Nurse();

		nurse.setEmployeeId(50);

		Undergoes saved = createTestUndergoes();

		saved.setPatient(patient);
		saved.setProcedures(procedure);

		when(patientService.getById(12345L)).thenReturn(patient);

		when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedure));

		when(stayService.getById(1)).thenReturn(stayDTO);

		when(roomService.getRoomById(201)).thenReturn(Optional.of(room));

		when(physicianService.getById(100)).thenReturn(physician);

		when(nurseService.getById(50)).thenReturn(nurse);

		when(undergoesService.saveUndergoes(any())).thenReturn(saved);

		mockMvc.perform(post("/admin/undergoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createUndergoes_procedureNotFound_shouldFail() throws Exception {

		UndergoesDTO dto = new UndergoesDTO(12345L, 999, 1, LocalDateTime.now(), 100, 50);

		Patient patient = new Patient();

		patient.setSsn(12345L);

		when(patientService.getById(12345L)).thenReturn(patient);

		when(proceduresService.getProcedureById(999)).thenThrow(new ResourceNotFoundException("Procedure not found"));

		mockMvc.perform(post("/admin/undergoes").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errMsg").value("Procedure not found"));
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/undergoes")).andExpect(status().isForbidden());
	}
}