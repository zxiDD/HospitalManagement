package com.cg.serviceTest;

import com.cg.entity.Patient;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.service.PatientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PatientServiceTest {

    @MockitoBean
    private PatientRepository repo;

    @Autowired
    private PatientServiceImpl service;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setSsn(1L);
        patient.setName("Alice");
        patient.setAddress("Delhi");
        patient.setPhone("9999999999");
        patient.setInsuranceId(100);
        patient.setIsActive(true);
    }

    @Test
    void testGetAll() {
        when(repo.findByIsActiveTrue()).thenReturn(List.of(patient));

        List<Patient> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findByIsActiveTrue();
    }

    @Test
    void testGetById_Success() {
        when(repo.findBySsnAndIsActiveTrue(1L)).thenReturn(Optional.of(patient));

        Patient result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetById_NotFound() {
        when(repo.findBySsnAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void testGetByName() {
        when(repo.findByNameAndIsActiveTrue("Alice")).thenReturn(List.of(patient));

        List<Patient> result = service.getByName("Alice");

        assertEquals(1, result.size());
        verify(repo).findByNameAndIsActiveTrue("Alice");
    }

    @Test
    void testGetByNameAndAddress() {
        when(repo.findByNameAndAddressAndIsActiveTrue("Alice", "Delhi"))
                .thenReturn(List.of(patient));

        List<Patient> result = service.getByNameAndAddress("Alice", "Delhi");

        assertEquals(1, result.size());
        verify(repo).findByNameAndAddressAndIsActiveTrue("Alice", "Delhi");
    }

    @Test
    void testSave_Success() {
        when(repo.existsById(1L)).thenReturn(false);
        when(repo.save(patient)).thenReturn(patient);

        Patient result = service.save(patient);

        assertNotNull(result);
        verify(repo).save(patient);
    }

    @Test
    void testSave_AlreadyExists() {
        when(repo.existsById(1L)).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> service.save(patient));

        verify(repo).existsById(1L);
        verify(repo, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        when(repo.findBySsnAndIsActiveTrue(1L)).thenReturn(Optional.of(patient));

        service.delete(1L);

        assertFalse(patient.getIsActive());
        verify(repo).save(patient);
    }

    @Test
    void testDelete_NotFound() {
        when(repo.findBySsnAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.delete(1L));
    }

    @Test
    void testGetByPhone_Success() {
        when(repo.findByPhoneAndIsActiveTrue("9999999999"))
                .thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getByPhone("9999999999");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testGetByPhone_NotFound() {
        when(repo.findByPhoneAndIsActiveTrue("9999999999"))
                .thenReturn(Optional.empty());

        Optional<Patient> result = service.getByPhone("9999999999");

        assertFalse(result.isPresent());
    }
}