package com.cg.controller;

import com.cg.dto.OnCallDTO;
import com.cg.entity.*;
import com.cg.service.OnCallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/oncall")
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

    @GetMapping
    public ResponseEntity<List<OnCallDTO>> getAll() {
        List<OnCallDTO> list = service.getAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<OnCallDTO>> getByNurse(@PathVariable Integer nurseId) {
        List<OnCallDTO> list = service.getByNurseId(nurseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<OnCall> addOnCall(@RequestBody OnCallDTO dto) {

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

        return ResponseEntity.status(201).body(saved);
    }
}