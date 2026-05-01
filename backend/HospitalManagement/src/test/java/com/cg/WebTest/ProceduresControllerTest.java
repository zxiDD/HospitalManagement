package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
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

import com.cg.dto.ProceduresDTO;
import com.cg.entity.Procedures;
import com.cg.service.ProceduresService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProceduresControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProceduresService proceduresService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllProcedures_success() throws Exception {

		Procedures p = new Procedures();
		p.setCode(101);
		p.setName("Cardiac Surgery");
		p.setCost(new BigDecimal("5000.00"));

		when(proceduresService.getAllProcedures()).thenReturn(List.of(p));

		mockMvc.perform(get("/procedures")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getProcedureById_success() throws Exception {

		Procedures p = new Procedures();
		p.setCode(101);
		p.setName("Cardiac Surgery");
		p.setCost(new BigDecimal("5000.00"));

		when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(p));

		mockMvc.perform(get("/procedures/101")).andExpect(status().isOk()).andExpect(jsonPath("$.code").value(101))
				.andExpect(jsonPath("$.name").value("Cardiac Surgery"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getProcedureById_notFound() throws Exception {

		when(proceduresService.getProcedureById(999)).thenReturn(Optional.empty());

		mockMvc.perform(get("/procedures/999")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByName_success() throws Exception {

		Procedures p = new Procedures();
		p.setCode(101);
		p.setName("Cardiac Surgery");
		p.setCost(new BigDecimal("5000.00"));

		when(proceduresService.getProceduresByName("Cardiac Surgery")).thenReturn(List.of(p));

		mockMvc.perform(get("/procedures/name/Cardiac Surgery")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getByCost_success() throws Exception {

		Procedures procedure = new Procedures();

		procedure.setCode(101);
		procedure.setName("MRI Scan");
		procedure.setCost(new BigDecimal("5000.00"));

		when(proceduresService.getProceduresByCost(new BigDecimal("5000.00"))).thenReturn(List.of(procedure));

		mockMvc.perform(get("/procedures/cost").param("value", "5000.00")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].code").value(101)).andExpect(jsonPath("$[0].name").value("MRI Scan"))
				.andExpect(jsonPath("$[0].cost").value(5000.00));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getCostBetween_success() throws Exception {

		Procedures procedure = new Procedures();

		procedure.setCode(101);
		procedure.setName("MRI Scan");
		procedure.setCost(new BigDecimal("5000.00"));

		when(proceduresService.getProceduresByCostBetween(any(), any())).thenReturn(List.of(procedure));

		mockMvc.perform(get("/procedures/cost/range").param("min", "1000").param("max", "10000"))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].code").value(101))
				.andExpect(jsonPath("$[0].name").value("MRI Scan")).andExpect(jsonPath("$[0].cost").value(5000.00));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createProcedure_success() throws Exception {

		ProceduresDTO dto = new ProceduresDTO(101, "Cardiac Surgery", new BigDecimal("5000.00"));

		Procedures p = new Procedures();
		p.setCode(101);
		p.setName("Cardiac Surgery");
		p.setCost(new BigDecimal("5000.00"));

		when(proceduresService.saveProcedures(any())).thenReturn(p);

		mockMvc.perform(post("/admin/procedures").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value(101));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void getAllProcedures_empty() throws Exception {

		when(proceduresService.getAllProcedures()).thenReturn(List.of());

		mockMvc.perform(get("/procedures")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void createProcedure_verifyServiceCall() throws Exception {

		ProceduresDTO dto = new ProceduresDTO(101, "Cardiac Surgery", new BigDecimal("5000.00"));

		when(proceduresService.saveProcedures(any())).thenReturn(new Procedures());

		mockMvc.perform(post("/admin/procedures").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated());

		verify(proceduresService, times(1)).saveProcedures(any());
	}
}