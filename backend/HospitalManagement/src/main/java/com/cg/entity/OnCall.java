package com.cg.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "OnCall")
public class OnCall {

    @EmbeddedId
    private OnCallId id;

    @ManyToOne
    @MapsId("nurse")
    @JoinColumn(name = "nurse", referencedColumnName = "employeeID")
    private Nurse nurse;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "blockFloor", referencedColumnName = "floor"),
        @JoinColumn(name = "blockCode", referencedColumnName = "code")
    })
    private Block block;

    @Column(name = "OnCallStart")
    private LocalDateTime onCallStart;

    @Column(name = "OnCallEnd")
    private LocalDateTime onCallEnd;

    public OnCall() {
    }

    public OnCall(OnCallId id, Nurse nurse, Block block,
                  LocalDateTime onCallStart, LocalDateTime onCallEnd) {
        this.id = id;
        this.nurse = nurse;
        this.block = block;
        this.onCallStart = onCallStart;
        this.onCallEnd = onCallEnd;
    }


    public OnCallId getId() {
        return id;
    }

    public void setId(OnCallId id) {
        this.id = id;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public LocalDateTime getOnCallStart() {
        return onCallStart;
    }

    public void setOnCallStart(LocalDateTime onCallStart) {
        this.onCallStart = onCallStart;
    }

    public LocalDateTime getOnCallEnd() {
        return onCallEnd;
    }

    public void setOnCallEnd(LocalDateTime onCallEnd) {
        this.onCallEnd = onCallEnd;
    }
}