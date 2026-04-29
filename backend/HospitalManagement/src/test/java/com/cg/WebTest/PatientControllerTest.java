package com.cg.WebTest;

import com.cg.controller.PatientController;
import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.service.PatientService;
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

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService service;

    @Mock
    private PhysicianService physicianService;

    @InjectMocks
    private PatientController controller;

    private Patient patient;
    private Physician physician;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(10);

        patient = new Patient();
        patient.setSsn(1L);
        patient.setName("Alice");
        patient.setAddress("Delhi");
        patient.setPhone("9999999999");
        patient.setInsuranceId(100);
        patient.setPhysician(physician);
    }

    @Test
    void testGetAll() {
        Mockito.when(service.getAll()).thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response = controller.getAll();

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getAll();
    }

    @Test
    void testGetBySsn() {
        Mockito.when(service.getById(1L)).thenReturn(patient);

        ResponseEntity<PatientDTO> response = controller.getBySsn(1L);

        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Alice", response.getBody().getName());
        Mockito.verify(service).getById(1L);
    }

    @Test
    void testGetByName() {
        Mockito.when(service.getByName("Alice"))
                .thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response = controller.getByName("Alice");

        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getByName("Alice");
    }

    @Test
    void testGetByNameAndAddress() {
        Mockito.when(service.getByNameAndAddress("Alice", "Delhi"))
                .thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response =
                controller.getByNameAndAddress("Alice", "Delhi");

        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getByNameAndAddress("Alice", "Delhi");
    }

    @Test
    void testCreate_Success() {
        PatientDTO dto = new PatientDTO(
                1L, "Alice", "Delhi", "9999999999", 100, 10
        );

        Mockito.when(physicianService.getById(10)).thenReturn(physician);
        Mockito.when(service.save(Mockito.any(Patient.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = controller.create(dto);

        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Alice", response.getBody().getName());

        Mockito.verify(service).save(Mockito.any(Patient.class));
        Mockito.verify(physicianService).getById(10);
    }

    @Test
    void testCreate_Fail_NoPhysician() {
        PatientDTO dto = new PatientDTO(
                1L, "Alice", "Delhi", "9999999999", 100, null
        );

        Assertions.assertThrows(BadRequestException.class,
                () -> controller.create(dto));
    }

    @Test
    void testUpdate() {
        PatientDTO dto = new PatientDTO(
                1L, "Updated", "Noida", "8888888888", 200, 10
        );

        Mockito.when(service.getById(1L)).thenReturn(patient);
        Mockito.when(physicianService.getById(10)).thenReturn(physician);
        Mockito.when(service.save(Mockito.any(Patient.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = controller.update(1L, dto);

        Assertions.assertEquals(200, response.getStatusCode().value());
        Mockito.verify(service).save(Mockito.any(Patient.class));
    }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = controller.delete(1L);

        Assertions.assertEquals(204, response.getStatusCode().value());
        Mockito.verify(service).delete(1L);
    }

    @Test
    void testGetByPhone() {
        Mockito.when(service.getByPhone("9999999999"))
                .thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response =
                controller.getByPhone("9999999999");

        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getByPhone("9999999999");
    }
}