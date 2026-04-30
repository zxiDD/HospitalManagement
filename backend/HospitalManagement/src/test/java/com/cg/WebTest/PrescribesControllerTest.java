package com.cg.WebTest;

import com.cg.controller.PrescribesController;
import com.cg.dto.PrescribesDTO;
import com.cg.entity.*;
import com.cg.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescribesControllerTest {

    @Mock
    private PrescribesService service;

    @Mock
    private PhysicianService physicianService;

    @Mock
    private PatientService patientService;

    @Mock
    private MedicationService medicationService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private PrescribesController controller;

    private Prescribes prescribes;
    private PrescribesId id;

    @BeforeEach
    void setUp() {

        id = new PrescribesId(1, 100L, 10,
                LocalDateTime.of(2024, 1, 1, 10, 0));

        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Patient patient = new Patient();
        patient.setSsn(100L);

        Medication medication = new Medication();
        medication.setCode(10);

        Appointment appointment = new Appointment();
        appointment.setAppointmentID(500);

        prescribes = new Prescribes();
        prescribes.setId(id);
        prescribes.setPhysician(physician);
        prescribes.setPatient(patient);
        prescribes.setMedication(medication);
        prescribes.setDose("2 times a day");
        prescribes.setAppointment(appointment);
    }

    // ✅ getAll()
    @Test
    void testGetAll() {
        when(service.getAll()).thenReturn(List.of(prescribes));

        ResponseEntity<List<PrescribesDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getAll();
    }

    // ✅ getById()
    @Test
    void testGetById() {
        when(service.getById(id)).thenReturn(prescribes);

        ResponseEntity<PrescribesDTO> response =
                controller.getById(1, 100L, 10, "2024-01-01T10:00:00");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getPhysicianId());
        verify(service).getById(any(PrescribesId.class));
    }

    // ✅ getByPhysician()
    @Test
    void testGetByPhysician() {
        when(service.getAll()).thenReturn(List.of(prescribes));

        ResponseEntity<List<PrescribesDTO>> response =
                controller.getByPhysician(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // ✅ getByPatient()
    @Test
    void testGetByPatient() {
        when(service.getAll()).thenReturn(List.of(prescribes));

        ResponseEntity<List<PrescribesDTO>> response =
                controller.getByPatient(100L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // ✅ create()
    @Test
    void testCreate_Success() {

        PrescribesDTO dto = new PrescribesDTO(
                1,
                100L,
                10,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "2 times a day",
                500
        );

        when(service.save(any(Prescribes.class))).thenReturn(prescribes);

        ResponseEntity<PrescribesDTO> response =
                controller.create(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(1, response.getBody().getPhysicianId());

        verify(service).save(any(Prescribes.class));
    }

    // ❌ create() bad request
    @Test
    void testCreate_BadRequest() {

        PrescribesDTO dto = new PrescribesDTO(
                null, null, null, null, null, null
        );

        ResponseEntity<PrescribesDTO> response =
                controller.create(dto);

        assertEquals(400, response.getStatusCode().value());
        verify(service, never()).save(any());
    }
}