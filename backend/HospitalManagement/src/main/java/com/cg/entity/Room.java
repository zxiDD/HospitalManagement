package com.cg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Room")
public class Room {

    @Id
    @Column(name = "RoomNumber")
    private Integer roomNumber;

    @Column(name = "RoomType", nullable = false, length = 255)
    private String roomType;

    @Column(name = "Unavailable", nullable = false)
    private Boolean unavailable;

    // Many rooms can belong to one block/floor
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "BlockFloor", referencedColumnName = "BlockFloor"),
        @JoinColumn(name = "BlockCode", referencedColumnName = "BlockCode")
    })
    private Block block;

    // Constructors
    public Room() {
    }

    public Room(Integer roomNumber, String roomType, Boolean unavailable, Block block) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.unavailable = unavailable;
        this.block = block;
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

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}