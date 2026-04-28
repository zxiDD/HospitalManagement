package com.cg.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UndergoesId implements Serializable {
	@Column(name = "patient")
	private Long patient;

	@Column(name = "procedures")
	private Integer procedures;

	@Column(name = "stay")
	private Integer stay;

	@Column(name = "dateundergoes")
	private LocalDateTime dateUndergoes;

	public UndergoesId() {

	}

	public UndergoesId(Long patient, Integer procedures, Integer stay, LocalDateTime dateUndergoes) {
		super();
		this.patient = patient;
		this.procedures = procedures;
		this.stay = stay;
		this.dateUndergoes = dateUndergoes;
	}

	public Long getPatient() {
		return patient;
	}

	public void setPatient(Long patient) {
		this.patient = patient;
	}

	public Integer getProcedures() {
		return procedures;
	}

	public void setProcedures(Integer procedures) {
		this.procedures = procedures;
	}

	public Integer getStay() {
		return stay;
	}

	public void setStay(Integer stay) {
		this.stay = stay;
	}

	public LocalDateTime getDateUndergoes() {
		return dateUndergoes;
	}

	public void setDateUndergoes(LocalDateTime dateUndergoes) {
		this.dateUndergoes = dateUndergoes;
	}
}
