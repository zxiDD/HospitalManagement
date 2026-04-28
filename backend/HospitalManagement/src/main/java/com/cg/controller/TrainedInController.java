package com.cg.controller;

import com.cg.dto.TrainedInDTO;
import com.cg.entity.TrainedIn;
import com.cg.service.TrainedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainedin")
public class TrainedInController {

    @Autowired
    private TrainedInService service;

    private TrainedInDTO convertToDTO(TrainedIn t) {
        return new TrainedInDTO(
                t.getId().getPhysician(),
                t.getId().getTreatment(),
                t.getCertificationDate().toLocalDate(),
                t.getCertificationExpires().toLocalDate()
        );
    }

    @GetMapping
    public List<TrainedInDTO> getAll() {
        return service.getAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/physician/{id}")
    public List<TrainedInDTO> getByPhysician(@PathVariable Integer id) {
        return service.getByPhysicianId(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}