package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

import com.cg.dto.StayDTO;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.StayService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class StayControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private StayService stayService;

	@Autowired
	private ObjectMapper objectMapper;

	private StayDTO createStayDTO() {

		return new StayDTO(1, 123L, "John", 101, "ICU", LocalDateTime.now(), null);
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getAllStays_success() throws Exception {

		when(stayService.getAll()).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays")).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].patientName").value("John"));

		verify(stayService).getAll();
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getStayById_success() throws Exception {

		when(stayService.getById(1)).thenReturn(createStayDTO());

		mockMvc.perform(get("/stays/1")).andExpect(status().isOk()).andExpect(jsonPath("$.patientName").value("John"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getStayById_notFound() throws Exception {

		when(stayService.getById(1)).thenThrow(new ResourceNotFoundException("Stay not found"));

		mockMvc.perform(get("/stays/1")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errMsg").value("Stay not found"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getByPatient_success() throws Exception {

		when(stayService.getByPatientSsn(123L)).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays/patient/123")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].patientName").value("John"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getByRoom_success() throws Exception {

		when(stayService.getByRoomNumber(101)).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays/room/101")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getAfter_success() throws Exception {

		when(stayService.getStaysAfter(any())).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays/after").param("dateTime", "2025-01-01T10:00:00")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getActiveStays_success() throws Exception {

		when(stayService.getActiveStays()).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays/active")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getHistory_success() throws Exception {

		when(stayService.getPatientStayHistory(123L)).thenReturn(List.of(createStayDTO()));

		mockMvc.perform(get("/stays/history/123")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createStay_success() throws Exception {

		StayDTO input = new StayDTO(null, 123L, null, 101, null, LocalDateTime.now(), null);

		when(stayService.create(any())).thenReturn(createStayDTO());

		mockMvc.perform(post("/admin/stays").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.stayId").value(1));

		verify(stayService).create(any());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createStay_validationFailure() throws Exception {

		StayDTO invalid = new StayDTO();

		mockMvc.perform(post("/admin/stays").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errMsg").value("Validation failed"));

		verify(stayService, never()).create(any());
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void isPatientActive_true() throws Exception {

		when(stayService.isPatientActive(123L)).thenReturn(true);

		mockMvc.perform(get("/stays/active/patient/123")).andExpect(status().isOk())
				.andExpect(content().string("true"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void isPatientActive_false() throws Exception {

		when(stayService.isPatientActive(123L)).thenReturn(false);

		mockMvc.perform(get("/stays/active/patient/123")).andExpect(status().isOk())
				.andExpect(content().string("false"));
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/stays")).andExpect(status().isForbidden());
	}
}