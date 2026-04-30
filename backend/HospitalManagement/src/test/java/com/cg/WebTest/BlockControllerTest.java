package com.cg.WebTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.service.BlockService;

@SpringBootTest
@AutoConfigureMockMvc
class BlockControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BlockService blockService;

	private Block block;
	private BlockId blockId;

	@BeforeEach
	void setUp() {

		blockId = new BlockId(1, 101);

		block = new Block();
		block.setId(blockId);
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getAllBlocks_success() throws Exception {

		when(blockService.getAllBlocks()).thenReturn(List.of(block));

		mockMvc.perform(get("/blocks")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
				.andExpect(jsonPath("$[0].blockFloor").value(1)).andExpect(jsonPath("$[0].blockCode").value(101));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getBlockById_success() throws Exception {

		when(blockService.getBlockById(blockId)).thenReturn(Optional.of(block));

		mockMvc.perform(get("/blocks/id").param("floor", "1").param("code", "101")).andExpect(status().isOk())
				.andExpect(jsonPath("$.blockFloor").value(1)).andExpect(jsonPath("$.blockCode").value(101));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getBlockById_notFound() throws Exception {

		when(blockService.getBlockById(blockId)).thenReturn(Optional.empty());

		mockMvc.perform(get("/blocks/id").param("floor", "1").param("code", "101")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getBlockByFloorAndCode_success() throws Exception {

		when(blockService.getBlockByFloorAndCode(1, 101)).thenReturn(Optional.of(block));

		mockMvc.perform(get("/blocks/1/101")).andExpect(status().isOk()).andExpect(jsonPath("$.blockFloor").value(1))
				.andExpect(jsonPath("$.blockCode").value(101));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getBlockByFloorAndCode_notFound() throws Exception {

		when(blockService.getBlockByFloorAndCode(1, 101)).thenReturn(Optional.empty());

		mockMvc.perform(get("/blocks/1/101")).andExpect(status().isNotFound());
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/blocks")).andExpect(status().isForbidden());
	}
}