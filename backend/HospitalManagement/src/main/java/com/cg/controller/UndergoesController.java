package com.cg.controller;

import com.cg.dto.UndergoesDTO;
import com.cg.entity.Undergoes;
import com.cg.entity.UndergoesId;
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

    private UndergoesDTO convertToDTO(Undergoes undergoes) {
        return new UndergoesDTO(
                undergoes.getPatient().getSsn(),
                undergoes.getProcedures().getCode(),
                undergoes.getStay().getStayId(),
                undergoes.getId().getDateUndergoes(),
                undergoes.getPhysician().getEmployeeId(),
                undergoes.getAssistingNurse() != null
                        ? undergoes.getAssistingNurse().getEmployeeId()
                        : null
        );
    }

    @GetMapping
    public ResponseEntity<List<UndergoesDTO>> getAllUndergoes() {

        List<UndergoesDTO> undergoesDTOs = undergoesService.getAllUndergoes()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(undergoesDTOs);
    }

    @GetMapping("/id")
    public ResponseEntity<UndergoesDTO> getUndergoesById(
            @RequestParam Long patientId,
            @RequestParam Integer procedureCode,
            @RequestParam Integer stayId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date
    ) {
        UndergoesId id = new UndergoesId(
                patientId,
                procedureCode,
                stayId,
                date
        );

        Optional<Undergoes> undergoes =
                undergoesService.getUndergoesById(id);

        return undergoes.map(value ->
                        ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByPatient(
            @PathVariable Long patientId
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByPatient(patientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/procedure/{procedureCode}")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByProcedure(
            @PathVariable Integer procedureCode
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByProcedure(procedureCode)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/stay/{stayId}")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByStay(
            @PathVariable Integer stayId
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByStay(stayId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/physician/{physicianId}")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByPhysician(
            @PathVariable Integer physicianId
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByPhysician(physicianId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByAssistingNurse(
            @PathVariable Integer nurseId
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByAssistingNurse(nurseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/date")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesByDate(date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<UndergoesDTO>> getUndergoesBetweenDates(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate
    ) {
        List<UndergoesDTO> records = undergoesService
                .getUndergoesBetweenDates(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(records);
    }
}