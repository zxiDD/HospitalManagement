package com.cg.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.RoomDTO;
import com.cg.entity.Room;
import com.cg.service.RoomService;

@RestController
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	private RoomDTO convertToDTO(Room room) {
		return new RoomDTO(room.getRoomNumber(), room.getRoomType(), room.getUnavailable(),
				room.getBlock().getId().getBlockFloor(), room.getBlock().getId().getBlockCode());
	}

	@PutMapping("/admin/room/{roomNumber}/unavailable")
	public ResponseEntity<RoomDTO> markRoomUnavailable(@PathVariable Integer roomNumber) {

		Room room = roomService.markRoomUnavailable(roomNumber);

		return ResponseEntity.ok(convertToDTO(room));
	}

	@GetMapping("/room")
	public ResponseEntity<List<RoomDTO>> getAllRooms() {

		List<RoomDTO> roomDTOs = roomService.getAllRooms().stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(roomDTOs);
	}

	@GetMapping("/room/{roomNumber}")
	public ResponseEntity<RoomDTO> getRoomById(@PathVariable Integer roomNumber) {
		Optional<Room> room = roomService.getRoomById(roomNumber);

		return room.map(value -> ResponseEntity.ok(convertToDTO(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/room/type/{roomType}")
	public ResponseEntity<List<RoomDTO>> getRoomsByRoomType(@PathVariable String roomType) {
		List<RoomDTO> rooms = roomService.getRoomsByRoomType(roomType).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/room/availability")
	public ResponseEntity<List<RoomDTO>> getRoomsByAvailability(@RequestParam Boolean unavailable) {
		List<RoomDTO> rooms = roomService.getRoomsByAvailability(unavailable).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/room/block/floor/{blockFloor}")
	public ResponseEntity<List<RoomDTO>> getRoomsByBlockFloor(@PathVariable Integer blockFloor) {
		List<RoomDTO> rooms = roomService.getRoomsByBlockFloor(blockFloor).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/room/block/code/{blockCode}")
	public ResponseEntity<List<RoomDTO>> getRoomsByBlockCode(@PathVariable Integer blockCode) {
		List<RoomDTO> rooms = roomService.getRoomsByBlockCode(blockCode).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(rooms);
	}

	@GetMapping("/room/block/{blockFloor}/{blockCode}")
	public ResponseEntity<List<RoomDTO>> getRoomsByBlock(@PathVariable Integer blockFloor,
			@PathVariable Integer blockCode) {
		List<RoomDTO> rooms = roomService.getRoomsByBlock(blockFloor, blockCode).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(rooms);
	}
}