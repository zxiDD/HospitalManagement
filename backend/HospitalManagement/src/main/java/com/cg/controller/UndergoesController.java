package com.cg.controller;

import com.cg.dto.StayDTO;
import com.cg.dto.UndergoesDTO;
import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.entity.Procedures;
import com.cg.entity.Room;
import com.cg.entity.Stay;
import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;
import com.cg.service.NurseService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.ProceduresService;
import com.cg.service.RoomService;
import com.cg.service.StayService;
import com.cg.service.UndergoesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/undergoes")
public class UndergoesController {

	@Autowired
	private UndergoesService undergoesService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private ProceduresService proceduresService;

	@Autowired
	private StayService stayService;

	@Autowired
	private NurseService nurseService;

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private PhysicianService physicianService;

	private UndergoesDTO convertToDTO(Undergoes undergoes) {
		return new UndergoesDTO(undergoes.getPatient().getSsn(), undergoes.getProcedures().getCode(),
				undergoes.getStay().getStayId(), undergoes.getId().getDateUndergoes(),
				undergoes.getPhysician().getEmployeeId(),
				undergoes.getAssistingNurse() != null ? undergoes.getAssistingNurse().getEmployeeId() : null);
	}

	@GetMapping
	public ResponseEntity<List<UndergoesDTO>> getAllUndergoes() {

		List<UndergoesDTO> undergoesDTOs = undergoesService.getAllUndergoes().stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(undergoesDTOs);
	}

	@GetMapping("/id")
	public ResponseEntity<UndergoesDTO> getUndergoesById(@RequestParam Long patientId,
			@RequestParam Integer procedureCode, @RequestParam Integer stayId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
		UndergoesId id = new UndergoesId(patientId, procedureCode, stayId, date);

		Optional<Undergoes> undergoes = undergoesService.getUndergoesById(id);

		return undergoes.map(value -> ResponseEntity.ok(convertToDTO(value)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByPatient(@PathVariable Long patientId) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByPatient(patientId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/procedure/{procedureCode}")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByProcedure(@PathVariable Integer procedureCode) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByProcedure(procedureCode).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/stay/{stayId}")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByStay(@PathVariable Integer stayId) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByStay(stayId).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/physician/{physicianId}")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByPhysician(@PathVariable Integer physicianId) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByPhysician(physicianId).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/nurse/{nurseId}")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByAssistingNurse(@PathVariable Integer nurseId) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByAssistingNurse(nurseId).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/date")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
		List<UndergoesDTO> records = undergoesService.getUndergoesByDate(date).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}

	@GetMapping("/date-range")
	public ResponseEntity<List<UndergoesDTO>> getUndergoesBetweenDates(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<UndergoesDTO> records = undergoesService.getUndergoesBetweenDates(startDate, endDate).stream()
				.map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(records);
	}
	
	@GetMapping("/patient/{patientId}/procedures")
	public ResponseEntity<List<String>> getProceduresByPatient(@PathVariable Long patientId) {
	    return ResponseEntity.ok(undergoesService.getProceduresByPatient(patientId));
	}

	@PostMapping
	public ResponseEntity<UndergoesDTO> createUndergoes(@RequestBody UndergoesDTO dto) {
		UndergoesId id = new UndergoesId(dto.getPatientId(), dto.getProcedureCode(), dto.getStayId(),
				dto.getDateUndergoes());

		Patient patient = patientService.getById(dto.getPatientId());

		Procedures procedure = proceduresService.getProcedureById(dto.getProcedureCode())
				.orElseThrow(() -> new RuntimeException("Procedure not found"));

		StayDTO stayDTO = stayService.getById(dto.getStayId());
		
		Room room = roomService.getRoomById(stayDTO.getRoomNumber()).orElseThrow(() -> new RuntimeException("Room not found"));

		Stay stay = new Stay(stayDTO.getStayId(), patient, room, stayDTO.getStayStart(), stayDTO.getStayEnd());

		Physician physician = physicianService.getById(dto.getPhysicianId());

		Nurse nurse = null;
		if (dto.getAssistingNurseId() != null) {
			nurse = nurseService.getById(dto.getAssistingNurseId());
		}

		Undergoes undergoes = new Undergoes();
		undergoes.setId(id);
		undergoes.setPatient(patient);
		undergoes.setProcedures(procedure);
		undergoes.setStay(stay);
		undergoes.setPhysician(physician);
		undergoes.setAssistingNurse(nurse);

		Undergoes saved = undergoesService.saveUndergoes(undergoes);

		UndergoesDTO response = new UndergoesDTO(saved.getPatient().getSsn(), saved.getProcedures().getCode(),
				saved.getStay().getStayId(), saved.getId().getDateUndergoes(), saved.getPhysician().getEmployeeId(),
				saved.getAssistingNurse() != null ? saved.getAssistingNurse().getEmployeeId() : null);

		return ResponseEntity.ok(response);
	}
}