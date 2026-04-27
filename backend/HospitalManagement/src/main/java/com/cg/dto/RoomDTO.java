package com.cg.dto;

public class RoomDTO {

	private Integer roomNumber;

	private String roomType;

	private Boolean unavailable;

	// Block reference (flattened from Block entity)
	private Integer blockFloor;
	private Integer blockCode;

	// Default constructor
	public RoomDTO() {
	}

	// Parameterized constructor
	public RoomDTO(Integer roomNumber, String roomType, Boolean unavailable, Integer blockFloor, Integer blockCode) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.unavailable = unavailable;
		this.blockFloor = blockFloor;
		this.blockCode = blockCode;
	}

	// Getters and Setters
	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public Boolean getUnavailable() {
		return unavailable;
	}

	public void setUnavailable(Boolean unavailable) {
		this.unavailable = unavailable;
	}

	public Integer getBlockFloor() {
		return blockFloor;
	}

	public void setBlockFloor(Integer blockFloor) {
		this.blockFloor = blockFloor;
	}

	public Integer getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(Integer blockCode) {
		this.blockCode = blockCode;
	}
}