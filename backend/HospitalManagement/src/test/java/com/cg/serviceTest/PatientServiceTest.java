package com.cg.serviceTest;

import com.cg.entity.Patient;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.service.PatientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository repo;

    @InjectMocks
    private PatientServiceImpl service;

    private Patient patient;
    private Optional<Patient> optPatient1, optPatient2;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setSsn(1L);
        patient.setName("Alice");
        patient.setAddress("Delhi");
        patient.setPhone("9876543210");
        patient.setInsuranceId(100);
        patient.setIsActive(true);

        optPatient1 = Optional.of(patient);
        optPatient2 = Optional.empty();
    }

    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(patient));

        List<Patient> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findAll();
    }

    @Test
    void testGetById_Success() {
        Mockito.when(repo.findById(1L)).thenReturn(optPatient1);

        Patient result = service.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Alice", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        Mockito.when(repo.findById(1L)).thenReturn(optPatient2);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void testGetByName() {
        Mockito.when(repo.findByName("Alice")).thenReturn(List.of(patient));

        List<Patient> result = service.getByName("Alice");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testGetByNameAndAddress() {
        Mockito.when(repo.findByNameAndAddress("Alice", "Delhi"))
                .thenReturn(List.of(patient));

        List<Patient> result = service.getByNameAndAddress("Alice", "Delhi");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testSave_Success() {
        Mockito.when(repo.existsById(1L)).thenReturn(false);
        Mockito.when(repo.save(patient)).thenReturn(patient);

        Patient saved = service.save(patient);

        Assertions.assertNotNull(saved);
        Mockito.verify(repo).save(patient);
    }

    @Test
    void testSave_AlreadyExists() {
        Mockito.when(repo.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class,
                () -> service.save(patient));
    }

    @Test
    void testDelete_Success() {
        Mockito.when(repo.findById(1L)).thenReturn(optPatient1);

        service.delete(1L);

        Assertions.assertFalse(patient.getIsActive());
        Mockito.verify(repo).save(patient);
    }

    @Test
    void testDelete_NotFound() {
        Mockito.when(repo.findById(1L)).thenReturn(optPatient2);

        Assertions.assertThrows(RuntimeException.class,
                () -> service.delete(1L));
    }

    @Test
    void testGetByPhone() {
        Mockito.when(repo.findByPhone("9876543210"))
                .thenReturn(List.of(patient));

        List<Patient> result = service.getByPhone("9876543210");

        Assertions.assertEquals(1, result.size());
    }
}