package com.cg.controller;

import com.cg.dto.RoomDTO;
import com.cg.entity.Room;
import com.cg.service.RoomService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(
                room.getRoomNumber(),
                room.getRoomType(),
                room.getUnavailable(),
                room.getBlock().getId().getBlockFloor(),
                room.getBlock().getId().getBlockCode()
        );
    }
    @PutMapping("/{roomNumber}/unavailable")
    public ResponseEntity<RoomDTO> markRoomUnavailable(@PathVariable Integer roomNumber) {

        Room room = roomService.markRoomUnavailable(roomNumber);

        return ResponseEntity.ok(convertToDTO(room));
    }
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {

        List<RoomDTO> roomDTOs = roomService.getAllRooms()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roomDTOs);
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomDTO> getRoomById(
            @PathVariable Integer roomNumber
    ) {
        Optional<Room> room = roomService.getRoomById(roomNumber);

        return room.map(value ->
                        ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<RoomDTO>> getRoomsByRoomType(
            @PathVariable String roomType
    ) {
        List<RoomDTO> rooms = roomService.getRoomsByRoomType(roomType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/availability")
    public ResponseEntity<List<RoomDTO>> getRoomsByAvailability(
            @RequestParam Boolean unavailable
    ) {
        List<RoomDTO> rooms = roomService.getRoomsByAvailability(unavailable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/block/floor/{blockFloor}")
    public ResponseEntity<List<RoomDTO>> getRoomsByBlockFloor(
            @PathVariable Integer blockFloor
    ) {
        List<RoomDTO> rooms = roomService.getRoomsByBlockFloor(blockFloor)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/block/code/{blockCode}")
    public ResponseEntity<List<RoomDTO>> getRoomsByBlockCode(
            @PathVariable Integer blockCode
    ) {
        List<RoomDTO> rooms = roomService.getRoomsByBlockCode(blockCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/block/{blockFloor}/{blockCode}")
    public ResponseEntity<List<RoomDTO>> getRoomsByBlock(
            @PathVariable Integer blockFloor,
            @PathVariable Integer blockCode
    ) {
        List<RoomDTO> rooms = roomService
                .getRoomsByBlock(blockFloor, blockCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }
}