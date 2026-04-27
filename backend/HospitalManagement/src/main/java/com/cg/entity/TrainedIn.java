package com.cg.entity;
@Entity
public class TrainedIn {

    @EmbeddedId
    private TrainedInId id;

    @ManyToOne
    @MapsId("physician")
    @JoinColumn(name = "physician")
    private Physician physician;

    @ManyToOne
    @MapsId("treatment")
    @JoinColumn(name = "treatment")
    private Procedures treatment;

    private LocalDate certificationDate;
    private LocalDate certificationExpires;
}