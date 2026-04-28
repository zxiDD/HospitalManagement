package com.cg.controller;

import com.cg.dto.OnCallDTO;
import com.cg.entity.OnCall;
import com.cg.service.OnCallService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<OnCallDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/nurse/{nurseId}")
    public List<OnCallDTO> getByNurse(@PathVariable Integer nurseId) {
        return service.getByNurseId(nurseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}