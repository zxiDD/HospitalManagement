package com.cg.dto;

import java.time.LocalDateTime;

public class StayDTO {

    private Integer stayId;

    private Long patientSsn;        // from Patient
    private String patientName;     // optional (useful for UI)

    private Integer roomNumber;     // from Room
    private String roomType;        // optional

    private LocalDateTime stayStart;
    private LocalDateTime stayEnd;

    // Constructors
    public StayDTO() {}

    public StayDTO(Integer stayId, Long patientSsn, String patientName,
                   Integer roomNumber, String roomType,
                   LocalDateTime stayStart, LocalDateTime stayEnd) {
        this.stayId = stayId;
        this.patientSsn = patientSsn;
        this.patientName = patientName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.stayStart = stayStart;
        this.stayEnd = stayEnd;
    }

    // Getters & Setters
    public Integer getStayId() {
        return stayId;
    }

    public void setStayId(Integer stayId) {
        this.stayId = stayId;
    }

    public Long getPatientSsn() {
        return patientSsn;
    }

    public void setPatientSsn(Long patientSsn) {
        this.patientSsn = patientSsn;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

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