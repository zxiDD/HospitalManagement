package com.cg.WebTest;

import com.cg.controller.PatientController;
import com.cg.dto.PatientDTO;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(service.getAll()).thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getAll();
    }

    @Test
    void testGetById() {
        when(service.getById(1L)).thenReturn(patient);

        ResponseEntity<PatientDTO> response = controller.getBySsn(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Alice", response.getBody().getName());
        verify(service).getById(1L);
    }

    @Test
    void testGetByName() {
        when(service.getByName("Alice")).thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response = controller.getByName("Alice");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getByName("Alice");
    }

    // ✅ getByNameAndAddress
    @Test
    void testGetByNameAndAddress() {
        when(service.getByNameAndAddress("Alice", "Delhi"))
                .thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response =
                controller.getByNameAndAddress("Alice", "Delhi");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreate_Success() {

        PatientDTO dto = new PatientDTO(
                1L, "Alice", "Delhi", "9999999999", 100, 10
        );

        when(physicianService.getById(10)).thenReturn(physician);
        when(service.save(any(Patient.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = controller.create(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Alice", response.getBody().getName());

        verify(service).save(any(Patient.class));
    }

    @Test
    void testCreate_BadRequest() {

        PatientDTO dto = new PatientDTO(
                1L, "Alice", "Delhi", "9999999999", 100, null
        );

        assertThrows(BadRequestException.class,
                () -> controller.create(dto));

        verify(service, never()).save(any());
    }

    @Test
    void testUpdate() {

        PatientDTO dto = new PatientDTO(
                1L, "Bob", "Mumbai", "8888888888", 200, 10
        );

        when(service.getById(1L)).thenReturn(patient);
        when(physicianService.getById(10)).thenReturn(physician);
        when(service.save(any(Patient.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response =
                controller.update(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        verify(service).save(any(Patient.class));
    }

    @Test
    void testDelete() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(service).delete(1L);
    }

    @Test
    void testGetByPhone_Success() {
        when(service.getByPhone("9999999999"))
                .thenReturn(Optional.of(patient));

        ResponseEntity<PatientDTO> response =
                controller.getByPhone("9999999999");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Alice", response.getBody().getName());
    }

    @Test
    void testGetByPhone_NotFound() {
        when(service.getByPhone("9999999999"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> controller.getByPhone("9999999999"));
    }
}