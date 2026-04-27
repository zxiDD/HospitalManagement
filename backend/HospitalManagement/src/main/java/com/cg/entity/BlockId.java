package com.cg.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BlockId implements Serializable {

    @Column(name = "BlockFloor")
    private Integer blockFloor;

    @Column(name = "BlockCode")
    private Integer blockCode;

    public BlockId() {
    }

    public BlockId(Integer blockFloor, Integer blockCode) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockId)) return false;
        BlockId blockId = (BlockId) o;
        return Objects.equals(blockFloor, blockId.blockFloor) &&
               Objects.equals(blockCode, blockId.blockCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockFloor, blockCode);
    }
}