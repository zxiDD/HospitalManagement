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

import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.service.PhysicianService;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class PhysicianControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PhysicianService physicianService;

	@Autowired
	private ObjectMapper objectMapper;

	private Physician physician;

	@BeforeEach
	void setUp() {

		physician = new Physician();
		physician.setEmployeeId(1);
		physician.setName("Smith");
		physician.setPosition("Cardiologist");
		physician.setSsn(1111L);
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllPhysicians_success() throws Exception {

		when(physicianService.getAll()).thenReturn(List.of(physician));

		mockMvc.perform(get("/physicians")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$[0].name").value("Smith"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getPhysicianById_success() throws Exception {

		when(physicianService.getById(1)).thenReturn(physician);

		mockMvc.perform(get("/physicians/id/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Smith"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPhysician_success() throws Exception {

		PhysicianDTO dto = new PhysicianDTO(null, "Smith", "Cardiologist", 1111L);

		when(physicianService.save(any(Physician.class))).thenReturn(physician);

		mockMvc.perform(post("/admin/physicians").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Smith"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createPhysician_validationFailure() throws Exception {

		PhysicianDTO invalid = new PhysicianDTO(null, "", "", null);

		mockMvc.perform(post("/admin/physicians").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalid))).andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void updatePhysician_success() throws Exception {

		PhysicianDTO dto = new PhysicianDTO(null, "John", "Neurologist", 2222L);

		when(physicianService.getById(1)).thenReturn(physician);

		when(physicianService.save(any(Physician.class))).thenReturn(physician);

		mockMvc.perform(put("/admin/physicians/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getPhysicianByPosition_success() throws Exception {

		when(physicianService.getByPosition("Cardiologist")).thenReturn(List.of(physician));

		mockMvc.perform(get("/physicians/position/Cardiologist")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void deletePhysician_success() throws Exception {

		doNothing().when(physicianService).delete(1);

		mockMvc.perform(delete("/admin/physicians/1")).andExpect(status().isNoContent());
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/physicians")).andExpect(status().isForbidden());
	}
}