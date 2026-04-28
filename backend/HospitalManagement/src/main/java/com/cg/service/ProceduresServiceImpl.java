package com.cg.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Procedures;
import com.cg.repo.ProceduresRepository;

@Service
public class ProceduresServiceImpl implements ProceduresService {

	@Autowired
	private ProceduresRepository proceduresRepository;

	@Override
	public List<Procedures> getAllProcedures() {
		return proceduresRepository.findAll();
	}

	@Override
	public Optional<Procedures> getProcedureById(Integer code) {
		return proceduresRepository.findById(code);
	}

	@Override
	public List<Procedures> getProceduresByName(String name) {
		return proceduresRepository.findByName(name);
	}

	@Override
	public List<Procedures> getProceduresByCost(BigDecimal cost) {
		return proceduresRepository.findByCost(cost);
	}

	@Override
	public List<Procedures> getProceduresByCostLessThan(BigDecimal cost) {
		return proceduresRepository.findByCostLessThan(cost);
	}

	@Override
	public List<Procedures> getProceduresByCostGreaterThan(BigDecimal cost) {
		return proceduresRepository.findByCostGreaterThan(cost);
	}

	@Override
	public List<Procedures> getProceduresByCostBetween(BigDecimal minCost, BigDecimal maxCost) {
		return proceduresRepository.findByCostBetween(minCost, maxCost);
	}

	@Override
	public Procedures saveProcedures(Procedures procedures) {
		return proceduresRepository.save(procedures);
	}
}
