package com.cg.dto;

import java.time.LocalDateTime;

public class AppointmentDTO {

    private int appointmentID;

    private int patientId;
    private int physicianId;
    private int nurseId;

    private LocalDateTime starto;
    private LocalDateTime endo;
    private String examinationRoom;

    public AppointmentDTO() {
    }

    public AppointmentDTO(int appointmentID, int patientId, int physicianId, int nurseId,
                          LocalDateTime starto, LocalDateTime endo, String examinationRoom) {
        this.appointmentID = appointmentID;
        this.patientId = patientId;
        this.physicianId = physicianId;
        this.nurseId = nurseId;
        this.starto = starto;
        this.endo = endo;
        this.examinationRoom = examinationRoom;
    }


    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(int physicianId) {
        this.physicianId = physicianId;
    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public LocalDateTime getStarto() {
        return starto;
    }

    public void setStarto(LocalDateTime starto) {
        this.starto = starto;
    }

    public LocalDateTime getEndo() {
        return endo;
    }

    public void setEndo(LocalDateTime endo) {
        this.endo = endo;
    }

    public String getExaminationRoom() {
        return examinationRoom;
    }

    public void setExaminationRoom(String examinationRoom) {
        this.examinationRoom = examinationRoom;
    }
}