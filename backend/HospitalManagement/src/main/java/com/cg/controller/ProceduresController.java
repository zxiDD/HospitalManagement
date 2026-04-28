package com.cg.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.ProceduresDTO;
import com.cg.entity.Procedures;
import com.cg.service.ProceduresService;

@RestController
@RequestMapping("/procedures")
public class ProceduresController {

	@Autowired
	private ProceduresService proceduresService;

	private ProceduresDTO convertToDTO(Procedures procedure) {
		return new ProceduresDTO(procedure.getCode(), procedure.getName(), procedure.getCost());
	}

	@GetMapping
	public ResponseEntity<List<ProceduresDTO>> getAllProcedures() {

		List<ProceduresDTO> procedureDTOs = proceduresService.getAllProcedures().stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(procedureDTOs);
	}

	@GetMapping("/{code}")
	public ResponseEntity<ProceduresDTO> getProcedureById(@PathVariable Integer code) {
		Optional<Procedures> procedure = proceduresService.getProcedureById(code);

		return procedure.map(value -> ResponseEntity.ok(convertToDTO(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<ProceduresDTO>> getProceduresByName(@PathVariable String name) {
		List<ProceduresDTO> procedures = proceduresService.getProceduresByName(name).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(procedures);
	}

	@GetMapping("/cost")
	public ResponseEntity<List<ProceduresDTO>> getProceduresByCost(@RequestParam BigDecimal value) {
		List<ProceduresDTO> procedures = proceduresService.getProceduresByCost(value).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(procedures);
	}

	@GetMapping("/cost/less-than")
	public ResponseEntity<List<ProceduresDTO>> getProceduresByCostLessThan(@RequestParam BigDecimal value) {
		List<ProceduresDTO> procedures = proceduresService.getProceduresByCostLessThan(value).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(procedures);
	}

	@GetMapping("/cost/greater-than")
	public ResponseEntity<List<ProceduresDTO>> getProceduresByCostGreaterThan(@RequestParam BigDecimal value) {
		List<ProceduresDTO> procedures = proceduresService.getProceduresByCostGreaterThan(value).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(procedures);
	}

	@GetMapping("/cost/range")
	public ResponseEntity<List<ProceduresDTO>> getProceduresByCostBetween(@RequestParam BigDecimal min,
			@RequestParam BigDecimal max) {
		List<ProceduresDTO> procedures = proceduresService.getProceduresByCostBetween(min, max).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(procedures);
	}
}