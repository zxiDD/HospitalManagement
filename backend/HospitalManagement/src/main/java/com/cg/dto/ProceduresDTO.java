package com.cg.dto;

import java.math.BigDecimal;

public class ProceduresDTO {

	private Integer code;

	private String name;

	private BigDecimal cost;

	// Default constructor
	public ProceduresDTO() {
	}

	// Parameterized constructor
	public ProceduresDTO(Integer code, String name, BigDecimal cost) {
		this.code = code;
		this.name = name;
		this.cost = cost;
	}

	// Getters and Setters
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
}