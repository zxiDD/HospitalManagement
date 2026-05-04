package com.cg.dto;

import java.time.LocalDateTime;

public class PrescribesDTO {

    private Integer physicianId;
    private String physicianName;

    private Long patientSsn;
    private String patientName;

    private Integer medicationId;
    private String medicationName;

    private LocalDateTime date;
    private String dose;
    private Integer appointmentId;

    public PrescribesDTO() {}

    public PrescribesDTO(Integer physicianId, String physicianName, Long patientSsn, String patientName,
                         Integer medicationId, String medicationName, LocalDateTime date,
                         String dose, Integer appointmentId) {
        this.physicianId = physicianId;
        this.physicianName = physicianName;
        this.patientSsn = patientSsn;
        this.patientName = patientName;
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.date = date;
        this.dose = dose;
        this.appointmentId = appointmentId;
    }

    public Integer getPhysicianId() { return physicianId; }
    public void setPhysicianId(Integer physicianId) { this.physicianId = physicianId; }

    public String getPhysicianName() { return physicianName; }
    public void setPhysicianName(String physicianName) { this.physicianName = physicianName; }

    public Long getPatientSsn() { return patientSsn; }
    public void setPatientSsn(Long patientSsn) { this.patientSsn = patientSsn; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Integer getMedicationId() { return medicationId; }
    public void setMedicationId(Integer medicationId) { this.medicationId = medicationId; }

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
}