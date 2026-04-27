package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
	public List<Room> findByRoomType(String roomType);

	// Get rooms by availability
	public List<Room> findByUnavailable(Boolean unavailable);

	// Get rooms by block floor
	public List<Room> findByBlock_Id_BlockFloor(Integer blockFloor);

	// Get rooms by block code
	public List<Room> findByBlock_Id_BlockCode(Integer blockCode);

	// Get rooms by block floor and block code
	public List<Room> findByBlock_Id_BlockFloorAndBlock_Id_BlockCode(Integer blockFloor, Integer blockCode);
}
