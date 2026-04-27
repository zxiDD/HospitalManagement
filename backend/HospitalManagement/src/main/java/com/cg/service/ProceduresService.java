package com.cg.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.cg.entity.Procedures;

public interface ProceduresService {

	// Get all procedures
	public List<Procedures> getAllProcedures();

	// Get procedure by code
	public Optional<Procedures> getProcedureById(Integer code);

	// Get procedures by exact name
	public List<Procedures> getProceduresByName(String name);

	// Get procedures by exact cost
	public List<Procedures> getProceduresByCost(BigDecimal cost);

	// Get procedures with cost less than
	public List<Procedures> getProceduresByCostLessThan(BigDecimal cost);

	// Get procedures with cost greater than
	public List<Procedures> getProceduresByCostGreaterThan(BigDecimal cost);

	// Get procedures within cost range
	public List<Procedures> getProceduresByCostBetween(BigDecimal minCost, BigDecimal maxCost);
}
