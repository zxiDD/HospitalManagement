package com.cg.serviceTest;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cg.entity.Appointment;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AppointmentRepository;
import com.cg.repo.NurseRepository;
import com.cg.repo.PatientRepository;
import com.cg.repo.PhysicianRepository;
import com.cg.service.AppointmentServiceImpl;
@SpringBootTest
class TestAppointmentService {

    @MockitoBean private AppointmentRepository repo;
    @MockitoBean private NurseRepository nurseRepo;
    @MockitoBean private PatientRepository patientRepo;
    @MockitoBean private PhysicianRepository physicianRepo;

    @Autowired private AppointmentServiceImpl service;

    Appointment a;
    Optional<Appointment> opt;

    @BeforeEach
    void setup() {
        a = new Appointment();
        a.setAppointmentID(1);
        a.setStarto(LocalDateTime.now());
        a.setEndo(LocalDateTime.now().plusHours(1));
        opt = Optional.of(a);
    }

    @Test
    void testGetSuccess() {
        Mockito.when(repo.findById(1)).thenReturn(opt);
        Assertions.assertNotNull(service.getAppointmentById(1));
    }

    @Test
    void testGetFail() {
        Mockito.when(repo.findById(2)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getAppointmentById(2));
    }

    @Test
    void testSaveSuccess() {
        Mockito.when(repo.existsById(1)).thenReturn(false);
        Mockito.when(repo.save(a)).thenReturn(a);
        Assertions.assertNotNull(service.save(a));
    }

    @Test
    void testDuplicate() {
        Mockito.when(repo.existsById(1)).thenReturn(true);
        Assertions.assertThrows(DuplicateResourceException.class,
                () -> service.save(a));
    }

    @Test
    void testSoftDelete() {
        Mockito.when(repo.findById(1)).thenReturn(opt);
        service.cancelAppointment(1);
        Assertions.assertFalse(a.isActive());
    }
}