package com.cg.controller;

import com.cg.dto.StayDTO;
import com.cg.exception.ValidationException;
import com.cg.service.StayService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stays")
public class StayController {

    @Autowired
    private StayService stayService;

    @GetMapping
    public ResponseEntity<List<StayDTO>> getAll() {
        return ResponseEntity.ok(stayService.getAll());
    }

    @PostMapping
    public ResponseEntity<StayDTO> createStay(
            @Valid @RequestBody StayDTO dto,
            BindingResult br) {

        if (br.hasErrors()) {
            throw new ValidationException(br.getFieldErrors());
        }

        StayDTO created = stayService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StayDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(stayService.getById(id));
    }

    @GetMapping("/patient/{ssn}")
    public ResponseEntity<List<StayDTO>> getByPatientSsn(@PathVariable Long ssn) {
        return ResponseEntity.ok(stayService.getByPatientSsn(ssn));
    }

    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<List<StayDTO>> getByRoomNumber(@PathVariable Integer roomNumber) {
        return ResponseEntity.ok(stayService.getByRoomNumber(roomNumber));
    }

    @GetMapping("/after")
    public ResponseEntity<List<StayDTO>> getStaysAfter(@RequestParam String dateTime) {
        LocalDateTime date = LocalDateTime.parse(dateTime);
        return ResponseEntity.ok(stayService.getStaysAfter(date));
    }

    @GetMapping("/active")
    public ResponseEntity<List<StayDTO>> getActiveStays() {
        return ResponseEntity.ok(stayService.getActiveStays());
    }

    @GetMapping("/history/{ssn}")
    public ResponseEntity<List<StayDTO>> getPatientStayHistory(@PathVariable Long ssn) {
        return ResponseEntity.ok(stayService.getPatientStayHistory(ssn));
    }

//    @GetMapping("/exists/{id}")
//    public ResponseEntity<Boolean> exists(@PathVariable Integer id) {
//        return ResponseEntity.ok(stayService.exists(id));
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Long> count() {
//        return ResponseEntity.ok(stayService.count());
//    }

    @GetMapping("/active/patient/{ssn}")
    public ResponseEntity<Boolean> isPatientActive(@PathVariable Long ssn) {
        return ResponseEntity.ok(stayService.isPatientActive(ssn));
    }
}