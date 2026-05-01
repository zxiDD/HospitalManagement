package com.cg.serviceTest;

import com.cg.entity.Room;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.RoomRepository;
import com.cg.service.RoomService;
import com.cg.service.RoomServiceImpl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@MockitoBean
    private RoomRepository roomRepository;

	  @Autowired
    private RoomServiceImpl roomService;

    private Room room;

    @BeforeEach
    void setup() {
        room = new Room();
        room.setRoomNumber(101);
        room.setRoomType("ICU");
        room.setUnavailable(false);
    }

    // ================= GET ALL =================
    @Test
    void testGetAllRooms() {

        Mockito.when(roomRepository.findAll())
                .thenReturn(List.of(room));

        List<Room> result = roomService.getAllRooms();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(roomRepository).findAll();
    }

    // ================= GET BY ID =================
    @Test
    void testGetRoomById_found() {

        Mockito.when(roomRepository.findById(101))
                .thenReturn(Optional.of(room));

        Optional<Room> result = roomService.getRoomById(101);

        Assertions.assertTrue(result.isPresent());
        Mockito.verify(roomRepository).findById(101);
    }

    @Test
    void testGetRoomById_notFound() {

        Mockito.when(roomRepository.findById(101))
                .thenReturn(Optional.empty());

        Optional<Room> result = roomService.getRoomById(101);

        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(roomRepository).findById(101);
    }

    // ================= GET BY TYPE =================
    @Test
    void testGetRoomsByType() {

        Mockito.when(roomRepository.findByRoomType("ICU"))
                .thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByRoomType("ICU");

        Assertions.assertEquals(1, result.size());
        Mockito.verify(roomRepository).findByRoomType("ICU");
    }

    // ================= GET BY AVAILABILITY =================
    @Test
    void testGetRoomsByAvailability() {

        Mockito.when(roomRepository.findByUnavailable(false))
                .thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByAvailability(false);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(roomRepository).findByUnavailable(false);
    }

    // ================= GET BY BLOCK FLOOR =================
    @Test
    void testGetRoomsByBlockFloor() {

        Mockito.when(roomRepository.findByBlock_Id_BlockFloor(1))
                .thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByBlockFloor(1);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(roomRepository).findByBlock_Id_BlockFloor(1);
    }

    // ================= GET BY BLOCK CODE =================
    @Test
    void testGetRoomsByBlockCode() {

        Mockito.when(roomRepository.findByBlock_Id_BlockCode(2))
                .thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByBlockCode(2);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(roomRepository).findByBlock_Id_BlockCode(2);
    }

    // ================= GET BY BLOCK (FLOOR + CODE) =================
    @Test
    void testGetRoomsByBlock() {

        Mockito.when(roomRepository
                .findByBlock_Id_BlockFloorAndBlock_Id_BlockCode(1, 2))
                .thenReturn(List.of(room));

        List<Room> result = roomService.getRoomsByBlock(1, 2);

        Assertions.assertEquals(1, result.size());

        Mockito.verify(roomRepository)
                .findByBlock_Id_BlockFloorAndBlock_Id_BlockCode(1, 2);
    }

    // ================= MARK ROOM UNAVAILABLE =================

    // ✔ SUCCESS
    @Test
    void testMarkRoomUnavailable_success() {

        Mockito.when(roomRepository.findById(101))
                .thenReturn(Optional.of(room));

        Mockito.when(roomRepository.save(Mockito.any()))
                .thenReturn(room);

        Room result = roomService.markRoomUnavailable(101);

        Assertions.assertTrue(result.getUnavailable());

        Mockito.verify(roomRepository).findById(101);
        Mockito.verify(roomRepository).save(Mockito.any(Room.class));
    }

    // ❌ NOT FOUND
    @Test
    void testMarkRoomUnavailable_notFound() {

        Mockito.when(roomRepository.findById(101))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> roomService.markRoomUnavailable(101));

        Mockito.verify(roomRepository).findById(101);

        Mockito.verify(roomRepository, Mockito.never())
                .save(Mockito.any());
    }

    // ❌ ALREADY UNAVAILABLE
    @Test
    void testMarkRoomUnavailable_alreadyUnavailable() {

        room.setUnavailable(true);

        Mockito.when(roomRepository.findById(101))
                .thenReturn(Optional.of(room));

        Assertions.assertThrows(BadRequestException.class,
                () -> roomService.markRoomUnavailable(101));

        Mockito.verify(roomRepository).findById(101);

        Mockito.verify(roomRepository, Mockito.never())
                .save(Mockito.any());
    }
    
    @Test
    void testGetAllRooms_empty() {

        Mockito.when(roomRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Room> result = roomService.getAllRooms();

        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(roomRepository).findAll();
    }
    
}