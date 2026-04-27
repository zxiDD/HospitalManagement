package com.cg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stay")
public class Stay {

    @Id
    @Column(name = "stayid")
    private Integer stayId;

    @ManyToOne
    @JoinColumn(name = "patient", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "room", nullable = false)
    private Room room;

    public Stay() {
		super();
	}

	public Stay(Integer stayId, Patient patient, Room room, LocalDateTime stayStart, LocalDateTime stayEnd) {
		super();
		this.stayId = stayId;
		this.patient = patient;
		this.room = room;
		this.stayStart = stayStart;
		this.stayEnd = stayEnd;
	}

	@Column(name = "staystart", nullable = false)
    private LocalDateTime stayStart;

    @Column(name = "stayend", nullable = true)
    private LocalDateTime stayEnd;

	public Integer getStayId() {
		return stayId;
	}

	public void setStayId(Integer stayId) {
		this.stayId = stayId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDateTime getStayStart() {
		return stayStart;
	}

	public void setStayStart(LocalDateTime stayStart) {
		this.stayStart = stayStart;
	}

	public LocalDateTime getStayEnd() {
		return stayEnd;
	}

	public void setStayEnd(LocalDateTime stayEnd) {
		this.stayEnd = stayEnd;
	}

}