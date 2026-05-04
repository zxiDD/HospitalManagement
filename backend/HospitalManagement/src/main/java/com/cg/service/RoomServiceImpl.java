package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Room;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public Optional<Room> getRoomById(Integer roomNumber) {
		return roomRepository.findById(roomNumber);
	}

	@Override
	public List<Room> getRoomsByRoomType(String roomType) {
		return roomRepository.findByRoomType(roomType);
	}

	@Override
	public List<Room> getRoomsByAvailability(Boolean unavailable) {
		return roomRepository.findByUnavailable(unavailable);
	}

	@Override
	public List<Room> getRoomsByBlockFloor(Integer blockFloor) {
		return roomRepository.findByBlock_Id_BlockFloor(blockFloor);
	}

	@Override
	public List<Room> getRoomsByBlockCode(Integer blockCode) {
		return roomRepository.findByBlock_Id_BlockCode(blockCode);
	}

	@Override
	public List<Room> getRoomsByBlock(Integer blockFloor, Integer blockCode) {
		return roomRepository.findByBlock_Id_BlockFloorAndBlock_Id_BlockCode(blockFloor, blockCode);
	}
	
	@Override
	public Room toggleRoomAvailability(Integer roomNumber) {

	    Room room = roomRepository.findById(roomNumber)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Room not found with number: " + roomNumber));

	    // 🔴 Toggle status
	    room.setUnavailable(!room.getUnavailable());

	    return roomRepository.save(room);
	}

}
