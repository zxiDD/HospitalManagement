package com.cg.controller;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
import com.cg.service.TrainedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainedin")
public class TrainedInController {

    @Autowired
    private TrainedInService service;

    @GetMapping
    public List<TrainedIn> getAll() {
        return service.getAll();
    }

    @GetMapping("/id")
    public TrainedIn getById(@RequestParam Integer physicianId,
                            @RequestParam Integer treatmentId) {

        TrainedInId id = new TrainedInId(physicianId, treatmentId);
        return service.getById(id);
    }

    @GetMapping("/physician/{physicianId}")
    public List<TrainedIn> getByPhysician(@PathVariable Integer physicianId) {
        return service.getByPhysicianId(physicianId);
    }

    @GetMapping("/treatment/{treatmentId}")
    public List<TrainedIn> getByTreatment(@PathVariable Integer treatmentId) {
        return service.getByTreatmentId(treatmentId);
    }
}