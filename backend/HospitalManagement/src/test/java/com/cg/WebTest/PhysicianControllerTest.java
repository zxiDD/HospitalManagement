package com.cg.WebTest;

import com.cg.controller.PhysicianController;
import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.service.PhysicianService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PhysicianControllerTest {

    @Mock
    private PhysicianService service;

    @InjectMocks
    private PhysicianController controller;

    private Physician physician;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. Smith");
        physician.setPosition("Cardiologist");
        physician.setSsn(1111L);
    }

    // ✅ getAll
    @Test
    void testGetAll() {
        Mockito.when(service.getAll()).thenReturn(List.of(physician));

        ResponseEntity<List<PhysicianDTO>> response = controller.getAll();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getAll();
    }

    // ✅ getById
    @Test
    void testGetById() {
        Mockito.when(service.getById(1)).thenReturn(physician);

        ResponseEntity<PhysicianDTO> response = controller.getById(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Dr. Smith", response.getBody().getName());
        Mockito.verify(service).getById(1);
    }

    // ✅ create
    @Test
    void testCreate() {
        PhysicianDTO dto = new PhysicianDTO(1, "Dr. Smith", "Cardiologist", 1111L);

        Mockito.when(service.save(Mockito.any(Physician.class)))
                .thenReturn(physician);

        ResponseEntity<PhysicianDTO> response = controller.create(dto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Dr. Smith", response.getBody().getName());

        Mockito.verify(service).save(Mockito.any(Physician.class));
    }

    // ✅ update
    @Test
    void testUpdate() {
        PhysicianDTO dto = new PhysicianDTO(1, "Updated", "Neurologist", 2222L);

        Mockito.when(service.getById(1)).thenReturn(physician);
        Mockito.when(service.save(Mockito.any(Physician.class)))
                .thenReturn(physician);

        ResponseEntity<PhysicianDTO> response = controller.update(1, dto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(service).getById(1);
        Mockito.verify(service).save(Mockito.any(Physician.class));
    }

    // ✅ getByPosition
    @Test
    void testGetByPosition() {
        Mockito.when(service.getByPosition("Cardiologist"))
                .thenReturn(List.of(physician));

        ResponseEntity<List<PhysicianDTO>> response =
                controller.getByPosition("Cardiologist");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getByPosition("Cardiologist");
    }

    // ✅ delete
    @Test
    void testDelete() {
        ResponseEntity<Void> response = controller.delete(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(service).delete(1);
    }
}