package com.cg.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "authorities")
public class Role {

	@EmbeddedId
	private RolePk key;

	public RolePk getKey() {
		return key;
	}

	public void setKey(RolePk key) {
		this.key = key;
	}

}