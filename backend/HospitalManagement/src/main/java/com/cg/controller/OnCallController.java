package com.cg.controller;

import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.service.OnCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oncall")
public class OnCallController {

    @Autowired
    private OnCallService service;

    @GetMapping
    public List<OnCall> getAll() {
        return service.getAll();
    }

    @GetMapping("/id")
    public OnCall getById(@RequestParam Integer nurse,
                         @RequestParam Integer blockFloor,
                         @RequestParam Integer blockCode) {

        OnCallId id = new OnCallId(nurse, blockFloor, blockCode);
        return service.getById(id);
    }

    @GetMapping("/nurse/{nurseId}")
    public List<OnCall> getByNurse(@PathVariable Integer nurseId) {
        return service.getByNurseId(nurseId);
    }

    @GetMapping("/block/{blockCode}")
    public List<OnCall> getByBlock(@PathVariable String blockCode) {
        return service.getByBlockCode(blockCode);
    }
}