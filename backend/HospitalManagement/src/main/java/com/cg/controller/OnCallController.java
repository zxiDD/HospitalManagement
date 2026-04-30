package com.cg.controller;

import com.cg.dto.OnCallDTO;
import com.cg.entity.*;
import com.cg.exception.BadRequestException;
import com.cg.service.OnCallService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/oncall")
@SecurityRequirement(name = "BearerAuth")
public class OnCallController {

    @Autowired
    private OnCallService service;

    private OnCallDTO convertToDTO(OnCall o) {
        return new OnCallDTO(
                o.getId().getNurse(),
                o.getId().getBlockFloor(),
                o.getId().getBlockCode(),
                o.getOnCallStart(),
                o.getOnCallEnd()
        );
    }

    @GetMapping("/oncall")
    public ResponseEntity<List<OnCallDTO>> getAll() {
        List<OnCallDTO> list = service.getAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/oncall/nurse/{nurseId}")
    public ResponseEntity<List<OnCallDTO>> getByNurse(@PathVariable Integer nurseId) {
        List<OnCallDTO> list = service.getByNurseId(nurseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PostMapping("/admin/oncall")
    public ResponseEntity<?> addOnCall(@Valid @RequestBody OnCallDTO dto) {

        if (dto.getOnCallEnd().isBefore(dto.getOnCallStart())) {
            throw new BadRequestException("End time must be after start time");
        }

        OnCall o = new OnCall();

        OnCallId id = new OnCallId(
                dto.getNurseId(),
                dto.getBlockFloor(),
                dto.getBlockCode()
        );

        o.setId(id);

        Nurse n = new Nurse();
        n.setEmployeeId(dto.getNurseId());
        o.setNurse(n);

        o.setOnCallStart(dto.getOnCallStart());
        o.setOnCallEnd(dto.getOnCallEnd());

        OnCall saved = service.save(o);
        return ResponseEntity.status(201).body(convertToDTO(saved));
    }
    
    @GetMapping("/oncall/at")
    public ResponseEntity<List<OnCallDTO>> getOnCallAt(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
            java.time.LocalDateTime time) {

        List<OnCallDTO> list = service.getAll()
                .stream()
                .filter(o -> !time.isBefore(o.getOnCallStart()) &&
                             !time.isAfter(o.getOnCallEnd()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
    
    @DeleteMapping("/admin/oncall")
    public ResponseEntity<Void> deleteOnCall(
            @RequestParam Integer nurseId,
            @RequestParam Integer blockFloor,
            @RequestParam Integer blockCode) {

        OnCallId id = new OnCallId(nurseId, blockFloor, blockCode);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}