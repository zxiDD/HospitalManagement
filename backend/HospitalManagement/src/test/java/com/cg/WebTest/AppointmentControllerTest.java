package com.cg.WebTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cg.controller.AppointmentController;
import com.cg.dto.AppointmentDTO;
import com.cg.dto.NurseDTO;
import com.cg.dto.PatientDTO;
import com.cg.entity.Appointment;
import com.cg.entity.Nurse;
import com.cg.entity.Patient;
import com.cg.entity.Physician;
import com.cg.exception.BadRequestException;
import com.cg.exception.IllegalOperationException;
import com.cg.service.AppointmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController controller;

    @Mock
    private AppointmentService service;

    private Appointment appointment;
    private Patient patient;
    private Physician physician;
    private Nurse nurse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        patient = new Patient();
        patient.setSsn(12345L);
        patient.setName("John Doe");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        patient.setInsuranceId(100);

        physician = new Physician();
        physician.setEmployeeId(100);
        physician.setName("Dr. Smith");

        nurse = new Nurse();
        nurse.setEmployeeId(50);
        nurse.setName("Nurse Joy");

        appointment = new Appointment();
        appointment.setAppointmentID(1);
        appointment.setPatient(patient);
        appointment.setPhysician(physician);
        appointment.setPrepNurse(nurse);
        appointment.setStarto(LocalDateTime.now().plusHours(1));
        appointment.setEndo(LocalDateTime.now().plusHours(2));
        appointment.setExaminationRoom("Room101");
    }

    @Test
    void testGetAll() {
        when(service.getAllAppointments()).thenReturn(List.of(appointment));

        ResponseEntity<List<AppointmentDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }


    @Test
    void testGetById_Found() {
        when(service.getAppointmentById(1)).thenReturn(appointment);

        ResponseEntity<AppointmentDTO> response = controller.getById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getAppointmentID());
    }


    @Test
    void testGetById_NotFound() {
        when(service.getAppointmentById(999)).thenReturn(null);

        ResponseEntity<AppointmentDTO> response = controller.getById(999);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetPatientsByPhysician() {
        when(service.getPatientsByPhysician(100)).thenReturn(List.of(patient));

        ResponseEntity<List<PatientDTO>> response = controller.getPatientsByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAppointmentsByPhysician() {
        when(service.getByPhysicianId(100)).thenReturn(List.of(appointment));

        ResponseEntity<List<AppointmentDTO>> response = controller.getAppointmentsByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAddAppointment_Success() {
        AppointmentDTO dto = new AppointmentDTO(
                1, 12345, 100, 50,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                "Room101");

        when(service.save(any(Appointment.class))).thenReturn(appointment);

        ResponseEntity<?> response = controller.addAppointment(dto);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddAppointment_InvalidTime() {
        AppointmentDTO dto = new AppointmentDTO(
                1, 12345, 100, 50,
                LocalDateTime.now().plusHours(3),
                LocalDateTime.now().plusHours(1),
                "Room101");

        assertThrows(BadRequestException.class, () -> controller.addAppointment(dto));
    }


    @Test
    void testReschedule_Success() {
        AppointmentDTO dto = new AppointmentDTO(
                1, 12345, 100, 50,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "Room101");

        when(service.getAppointmentById(1)).thenReturn(appointment);
        when(service.save(any(Appointment.class))).thenReturn(appointment);

        ResponseEntity<?> response = controller.reschedule(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testReschedule_InvalidTimeRange() {
        AppointmentDTO dto = new AppointmentDTO(
                1, 12345, 100, 50,
                LocalDateTime.now().plusDays(1).plusHours(2),
                LocalDateTime.now().plusDays(1),
                "Room101");

        when(service.getAppointmentById(1)).thenReturn(appointment);

        assertThrows(IllegalOperationException.class, () -> controller.reschedule(dto));
    }

    @Test
    void testReschedule_PastTime() {
        AppointmentDTO dto = new AppointmentDTO(
                1, 12345, 100, 50,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now(),
                "Room101");

        when(service.getAppointmentById(1)).thenReturn(appointment);

        assertThrows(IllegalOperationException.class, () -> controller.reschedule(dto));
    }


    @Test
    void testGetAppointmentsForPatient() {
        when(service.getByPatientId(12345L)).thenReturn(List.of(appointment));

        ResponseEntity<List<AppointmentDTO>> response = controller.getAppointmentsForPatient(12345L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testAssignPrepNurse() {
        NurseDTO nurseDTO = new NurseDTO();
        nurseDTO.setEmployeeId(75);

        when(service.assignPrepNurse(1, 75)).thenReturn(appointment);

        ResponseEntity<AppointmentDTO> response = controller.assignPrepNurse(1, nurseDTO);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetAll_Empty() {
        when(service.getAllAppointments()).thenReturn(List.of());

        ResponseEntity<List<AppointmentDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAppointmentsForPatient_Empty() {
        when(service.getByPatientId(999L)).thenReturn(List.of());

        ResponseEntity<List<AppointmentDTO>> response = controller.getAppointmentsForPatient(999L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }


    @Test
    void testGetPatientsByPhysician_Empty() {
        when(service.getPatientsByPhysician(999)).thenReturn(List.of());

        ResponseEntity<List<PatientDTO>> response = controller.getPatientsByPhysician(999);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testReschedule_AppointmentNotFound() {
        AppointmentDTO dto = new AppointmentDTO(
                999, 12345, 100, 50,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "Room101");

        when(service.getAppointmentById(999)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> controller.reschedule(dto));
    }
}