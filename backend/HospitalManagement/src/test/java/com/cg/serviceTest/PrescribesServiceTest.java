package com.cg.serviceTest;

import com.cg.entity.*;
import com.cg.repo.PrescribesRepository;
import com.cg.service.PrescribesServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PrescribesServiceTest {

    @MockitoBean
    private PrescribesRepository repo;

    @Autowired
    private PrescribesServiceImpl service;

    private Prescribes prescribes;
    private PrescribesId id;
    private Optional<Prescribes> optPrescribes1, optPrescribes2;

    @BeforeEach
    void setUp() {

        id = new PrescribesId();
        id.setPhysician(1);
        id.setPatient(100L);
        id.setMedication(10);
        id.setDate(LocalDateTime.of(2024, 1, 1, 10, 0));

        prescribes = new Prescribes();
        prescribes.setId(id);

        // ✅ create related entities
        Physician physician = new Physician();
        physician.setEmployeeId(1);

        Patient patient = new Patient();
        patient.setSsn(100L);

        Medication medication = new Medication();
        medication.setCode(10);

        // ✅ set relationships
        prescribes.setPhysician(physician);
        prescribes.setPatient(patient);
        prescribes.setMedication(medication);
        prescribes.setDose("2 times a day");

        optPrescribes1 = Optional.of(prescribes);
        optPrescribes2 = Optional.empty();
    }

    @Test
    void testGetAll() {
        Mockito.when(repo.findAll()).thenReturn(List.of(prescribes));

        List<Prescribes> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findAll();
    }

    @Test
    void testGetById_Success() {
        Mockito.when(repo.findById(id)).thenReturn(optPrescribes1);

        Prescribes result = service.getById(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(10, result.getMedication().getCode()); // ✅ FIXED
        Mockito.verify(repo).findById(id);
    }

    @Test
    void testGetById_NotFound() {
        Mockito.when(repo.findById(id)).thenReturn(optPrescribes2);

        Assertions.assertThrows(RuntimeException.class,
                () -> service.getById(id));

        Mockito.verify(repo).findById(id);
    }

    @Test
    void testGetAllSorted() {
        Mockito.when(repo.findAll(Sort.by("id.date")))
                .thenReturn(List.of(prescribes));

        List<Prescribes> result = service.getAllSorted();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(repo).findAll(Sort.by("id.date"));
    }

    @Test
    void testSave() {
        Mockito.when(repo.save(prescribes)).thenReturn(prescribes);

        Prescribes saved = service.save(prescribes);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(10, saved.getMedication().getCode()); 
        Mockito.verify(repo).save(prescribes);
    }
}