package com.cg.controller;

import com.cg.dto.StayDTO;
import com.cg.service.StayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stays")
public class StayController {

    @Autowired
    private StayService stayService;

    @GetMapping
    public List<StayDTO> getAll() {
        return stayService.getAll();
    }

    @PostMapping
    public StayDTO createStay(@RequestBody StayDTO dto) {
        return stayService.create(dto);
    }
    
    @GetMapping("/{id}")
    public StayDTO getById(@PathVariable Integer id) {
        return stayService.getById(id);
    }

    @GetMapping("/patient/{ssn}")
    public List<StayDTO> getByPatientSsn(@PathVariable Long ssn) {
        return stayService.getByPatientSsn(ssn);
    }

    @GetMapping("/room/{roomNumber}")
    public List<StayDTO> getByRoomNumber(@PathVariable Integer roomNumber) {
        return stayService.getByRoomNumber(roomNumber);
    }

    @GetMapping("/after")
    public List<StayDTO> getStaysAfter(@RequestParam String dateTime) {
        LocalDateTime date = LocalDateTime.parse(dateTime);
        return stayService.getStaysAfter(date);
    }

    @GetMapping("/active")
    public List<StayDTO> getActiveStays() {
        return stayService.getActiveStays();
    }

    @GetMapping("/history/{ssn}")
    public List<StayDTO> getPatientStayHistory(@PathVariable Long ssn) {
        return stayService.getPatientStayHistory(ssn);
    }

    @GetMapping("/exists/{id}")
    public boolean exists(@PathVariable Integer id) {
        return stayService.exists(id);
    }

    @GetMapping("/count")
    public long count() {
        return stayService.count();
    }
}