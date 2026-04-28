package com.cg.dto;

public class BlockDTO {
	private Integer blockFloor;

	private Integer blockCode;

	public BlockDTO() {
	}

	public BlockDTO(Integer blockFloor, Integer blockCode) {
		this.blockFloor = blockFloor;
		this.blockCode = blockCode;
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