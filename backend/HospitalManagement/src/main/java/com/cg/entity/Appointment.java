package com.cg.entity;
@Entity
public class Appointment {

    @Id
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "physician")
    private Physician physician;

    @ManyToOne
    @JoinColumn(name = "prepNurse")
    private Nurse prepNurse;

    private LocalDateTime starto;
    private LocalDateTime endo;
    private String examinationRoom;
}