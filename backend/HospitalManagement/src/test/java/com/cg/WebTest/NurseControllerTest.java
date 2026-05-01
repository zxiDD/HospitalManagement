package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import com.cg.dto.NurseDTO;
import com.cg.entity.Nurse;
import com.cg.service.NurseService;
import com.cg.service.OnCallService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class NurseControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private NurseService nurseService;

    @MockitoBean
    private OnCallService onCallService; 

	@Autowired
	private ObjectMapper objectMapper;

	private Nurse nurse;

	@BeforeEach
	void setUp() {

		nurse = new Nurse();
		nurse.setEmployeeId(1);
		nurse.setName("John");
		nurse.setPosition("Staff");
		nurse.setRegistered(true);
		nurse.setSsn(12345L);
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllNurses_success() throws Exception {

		when(nurseService.getAll()).thenReturn(List.of(nurse));

		mockMvc.perform(get("/nurses")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$[0].name").value("John"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getNurseById_success() throws Exception {

		when(nurseService.getById(1)).thenReturn(nurse);

		mockMvc.perform(get("/nurses/id/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("John"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createNurse_success() throws Exception {

		NurseDTO dto = new NurseDTO(1, "John", "Staff", true, 12345L);

		when(nurseService.save(any(Nurse.class))).thenReturn(nurse);

		mockMvc.perform(post("/admin/nurses").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("John"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void updateNurse_success() throws Exception {

		NurseDTO dto = new NurseDTO(1, "Updated", "Senior", true, 99999L);

		when(nurseService.getById(1)).thenReturn(nurse);

		when(nurseService.save(any(Nurse.class))).thenReturn(nurse);

		mockMvc.perform(put("/admin/nurses/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void deleteNurse_success() throws Exception {

		doNothing().when(nurseService).delete(1);

		mockMvc.perform(delete("/admin/nurses/1")).andExpect(status().isNoContent());
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/nurses")).andExpect(status().isForbidden());
	}
}