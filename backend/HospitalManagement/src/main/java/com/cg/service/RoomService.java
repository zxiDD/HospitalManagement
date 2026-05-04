package com.cg.service;

import java.util.List;
import java.util.Optional;

import com.cg.entity.Room;

public interface RoomService {
	public List<Room> getAllRooms();

	// Get room by room number
	public Optional<Room> getRoomById(Integer roomNumber);

	// Get rooms by room type
	public List<Room> getRoomsByRoomType(String roomType);

	// Get rooms by availability
	public List<Room> getRoomsByAvailability(Boolean unavailable);

	// Get rooms by block floor
	public List<Room> getRoomsByBlockFloor(Integer blockFloor);

	// Get rooms by block code
	public List<Room> getRoomsByBlockCode(Integer blockCode);

	// Get rooms by block floor and block code
	public List<Room> getRoomsByBlock(Integer blockFloor, Integer blockCode);
	
	Room toggleRoomAvailability(Integer roomNumber);
}
