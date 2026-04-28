package com.cg.controller;

import com.cg.dto.StayDTO;
import com.cg.service.StayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<StayDTO> list = stayService.getAll();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<StayDTO> createStay(@RequestBody StayDTO dto) {
        StayDTO created = stayService.create(dto);

        if (created != null) {
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StayDTO> getById(@PathVariable Integer id) {
        StayDTO dto = stayService.getById(id);

        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/patient/{ssn}")
    public ResponseEntity<List<StayDTO>> getByPatientSsn(@PathVariable Long ssn) {
        List<StayDTO> list = stayService.getByPatientSsn(ssn);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/room/{roomNumber}")
    public ResponseEntity<List<StayDTO>> getByRoomNumber(@PathVariable Integer roomNumber) {
        List<StayDTO> list = stayService.getByRoomNumber(roomNumber);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/after")
    public ResponseEntity<List<StayDTO>> getStaysAfter(@RequestParam String dateTime) {
        LocalDateTime date = LocalDateTime.parse(dateTime);
        List<StayDTO> list = stayService.getStaysAfter(date);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<StayDTO>> getActiveStays() {
        List<StayDTO> list = stayService.getActiveStays();

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/history/{ssn}")
    public ResponseEntity<List<StayDTO>> getPatientStayHistory(@PathVariable Long ssn) {
        List<StayDTO> list = stayService.getPatientStayHistory(ssn);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(204).body(null);
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Integer id) {
        boolean exists = stayService.exists(id);

        if (exists) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = stayService.count();

        if (count > 0) {
            return ResponseEntity.ok(count);
        } else {
            return new ResponseEntity<>(0L, HttpStatus.NO_CONTENT);
        }
    }
}