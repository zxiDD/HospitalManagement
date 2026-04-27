package com.cg.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Procedures;

public interface ProceduresRepository extends JpaRepository<Procedures, Integer> {
	List<Procedures> findByName(String name);

	List<Procedures> findByCost(BigDecimal cost);
	
	List<Procedures> findByCostLessThan(BigDecimal cost);
	
	List<Procedures> findByCostGreaterThan(BigDecimal cost);
	
	List<Procedures> findByCostBetween(BigDecimal mincost, BigDecimal maxcost);
}
