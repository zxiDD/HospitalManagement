package com.cg.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Procedures")
public class Procedures {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "code")
	    private Integer code;

	    @Column(name = "name", nullable = false, length = 255)
	    private String name;

	    @Column(name = "cost", nullable = false, precision = 10, scale = 2)
	    private BigDecimal cost;

	    // Constructors
	    public Procedures() {
	    }

	    public Procedures(Integer code, String name, BigDecimal cost) {
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
