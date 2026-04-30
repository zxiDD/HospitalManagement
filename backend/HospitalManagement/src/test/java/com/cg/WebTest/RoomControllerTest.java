package com.cg.WebTest;

import com.cg.controller.RoomController;
import com.cg.entity.Block;
import com.cg.entity.BlockId;
import com.cg.entity.Room;
import com.cg.exception.BadRequestException;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.RoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@Import(GlobalExceptionHandler.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    private Room createRoom(Integer id, boolean unavailable) {

        Block block = new Block();

        BlockId blockId = new BlockId();   // ✅ FIXED
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

    // =========================
    // GET ALL
    // =========================
    @Test
    public void testGetAllRooms() throws Exception {

        Mockito.when(roomService.getAllRooms())
                .thenReturn(List.of(createRoom(1, false)));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value(1));

        Mockito.verify(roomService).getAllRooms();
    }

    // =========================
    // GET BY ID SUCCESS
    // =========================
    @Test
    public void testGetRoomById_success() throws Exception {

        Mockito.when(roomService.getRoomById(1))
                .thenReturn(Optional.of(createRoom(1, false)));

        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomNumber").value(1));

        Mockito.verify(roomService).getRoomById(1);
    }

    // =========================
    // GET BY ID NOT FOUND
    // =========================
    @Test
    public void testGetRoomById_notFound() throws Exception {

        Mockito.when(roomService.getRoomById(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isNotFound());

        Mockito.verify(roomService).getRoomById(1);
    }

    // =========================
    // MARK UNAVAILABLE SUCCESS
    // =========================
    @Test
    public void testMarkRoomUnavailable_success() throws Exception {

        Mockito.when(roomService.markRoomUnavailable(1))
                .thenReturn(createRoom(1, true));

        mockMvc.perform(put("/rooms/1/unavailable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unavailable").value(true));

        Mockito.verify(roomService).markRoomUnavailable(1);
    }

    // =========================
    // ROOM NOT FOUND
    // =========================
    @Test
    public void testMarkRoomUnavailable_notFound() throws Exception {

        Mockito.when(roomService.markRoomUnavailable(1))
                .thenThrow(new ResourceNotFoundException("Room not found"));

        mockMvc.perform(put("/rooms/1/unavailable"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Room not found"));

        Mockito.verify(roomService).markRoomUnavailable(1);
    }

    // =========================
    // ALREADY UNAVAILABLE
    // =========================
    @Test
    public void testMarkRoomUnavailable_alreadyUnavailable() throws Exception {

        Mockito.when(roomService.markRoomUnavailable(1))
                .thenThrow(new BadRequestException("Room is already unavailable"));

        mockMvc.perform(put("/rooms/1/unavailable"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg")
                        .value("Room is already unavailable"));

        Mockito.verify(roomService).markRoomUnavailable(1);
    }

    // =========================
    // FILTER BY TYPE
    // =========================
    @Test
    public void testGetRoomsByType() throws Exception {

        Mockito.when(roomService.getRoomsByRoomType("ICU"))
                .thenReturn(List.of(createRoom(1, false)));

        mockMvc.perform(get("/rooms/type/ICU"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomType").value("ICU"));

        Mockito.verify(roomService).getRoomsByRoomType("ICU");
    }

    // =========================
    // FILTER BY AVAILABILITY
    // =========================
    @Test
    public void testGetRoomsByAvailability() throws Exception {

        Mockito.when(roomService.getRoomsByAvailability(false))
                .thenReturn(List.of(createRoom(1, false)));

        mockMvc.perform(get("/rooms/availability?unavailable=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].unavailable").value(false));

        Mockito.verify(roomService).getRoomsByAvailability(false);
    }
}