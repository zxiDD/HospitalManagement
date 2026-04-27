package com.cg.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne
    @MapsId("nurse")
    @JoinColumn(name = "nurse")
    private Nurse nurse;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "blockFloor"),
        @JoinColumn(name = "blockCode")
    })
    @MapsId("blockFloor")   // maps both fields via composite
    private Block block;

    private LocalDateTime onCallStart;
    private LocalDateTime onCallEnd;
}