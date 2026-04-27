package com.cg.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Block")
public class Block {
	@EmbeddedId
	private BlockId id;

	public Block() {

	}

	public Block(BlockId id) {
		super();
		this.id = id;
	}

	public BlockId getId() {
		return id;
	}

	public void setId(BlockId id) {
		this.id = id;
	}
}
