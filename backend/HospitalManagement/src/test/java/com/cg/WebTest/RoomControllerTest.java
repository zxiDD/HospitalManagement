package com.cg.WebTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.entity.Room;

import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;

import com.cg.service.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private RoomService roomService;

	private Room createRoom(Integer id, boolean unavailable) {

		Block block = new Block();

		BlockId blockId = new BlockId();
		blockId.setBlockFloor(1);
		blockId.setBlockCode(101);

		block.setId(blockId);

		Room room = new Room();
		room.setRoomNumber(id);
		room.setRoomType("ICU");
		room.setUnavailable(unavailable);
		room.setBlock(block);

		return room;
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getAllRooms_success() throws Exception {

		when(roomService.getAllRooms()).thenReturn(List.of(createRoom(1, false)));

		mockMvc.perform(get("/room")).andExpect(status().isOk()).andExpect(jsonPath("$[0].roomNumber").value(1));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getRoomById_success() throws Exception {

		when(roomService.getRoomById(1)).thenReturn(Optional.of(createRoom(1, false)));

		mockMvc.perform(get("/room/1")).andExpect(status().isOk()).andExpect(jsonPath("$.roomNumber").value(1));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getRoomById_notFound() throws Exception {

		when(roomService.getRoomById(1)).thenReturn(Optional.empty());

		mockMvc.perform(get("/room/1")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void markRoomUnavailable_success() throws Exception {

		when(roomService.markRoomUnavailable(1)).thenReturn(createRoom(1, true));

		mockMvc.perform(put("/admin/room/1/unavailable")).andExpect(status().isOk())
				.andExpect(jsonPath("$.unavailable").value(true));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void markRoomUnavailable_notFound() throws Exception {

		when(roomService.markRoomUnavailable(1)).thenThrow(new ResourceNotFoundException("Room not found"));

		mockMvc.perform(put("/admin/room/1/unavailable")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errMsg").value("Room not found"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void markRoomUnavailable_alreadyUnavailable() throws Exception {

		when(roomService.markRoomUnavailable(1)).thenThrow(new BadRequestException("Room is already unavailable"));

		mockMvc.perform(put("/admin/room/1/unavailable")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errMsg").value("Room is already unavailable"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getRoomsByType_success() throws Exception {

		when(roomService.getRoomsByRoomType("ICU")).thenReturn(List.of(createRoom(1, false)));

		mockMvc.perform(get("/room/type/ICU")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].roomType").value("ICU"));
	}

	@Test
	@WithMockUser(username = "patient1", roles = { "PATIENT" })
	void getRoomsByAvailability_success() throws Exception {

		when(roomService.getRoomsByAvailability(false)).thenReturn(List.of(createRoom(1, false)));

		mockMvc.perform(get("/room/availability").param("unavailable", "false")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].unavailable").value(false));
	}

	@Test
	void unauthorizedAccess_shouldFail() throws Exception {

		mockMvc.perform(get("/room")).andExpect(status().isForbidden());
	}
}