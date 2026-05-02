package com.cg.serviceTest;

import com.cg.dto.MedicationDTO;
import com.cg.entity.Medication;
import com.cg.exception.BadRequestException;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.MedicationRepository;
import com.cg.service.MedicationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MedicationServiceTest {

	@MockitoBean
    private MedicationRepository medicationRepository;

    @Autowired
    private MedicationServiceImpl medicationService;

    private Medication med;

    @BeforeEach
    void setup() {
        med = new Medication();
        med.setCode(1);
        med.setName("Paracetamol");
        med.setBrand("Cipla");
        med.setDescription("Fever medicine");
    }

    // ===================== getById =====================

    @Test
    void testGetById_success() {

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(med));

        MedicationDTO result = medicationService.getById(1);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());

        Mockito.verify(medicationRepository).findById(1);
    }

    @Test
    void testGetById_notFound() {

        Mockito.when(medicationRepository.findById(2))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medicationService.getById(2));

        Mockito.verify(medicationRepository).findById(2);
    }

    // ===================== getAll =====================

    @Test
    void testGetAll_withData() {

        Mockito.when(medicationRepository.findAll())
                .thenReturn(List.of(med));

        List<MedicationDTO> result = medicationService.getAll();

        assertEquals(1, result.size());

        Mockito.verify(medicationRepository).findAll();
    }

    @Test
    void testGetAll_empty() {

        Mockito.when(medicationRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<MedicationDTO> result = medicationService.getAll();

        assertTrue(result.isEmpty());

        Mockito.verify(medicationRepository).findAll();
    }

    // ===================== getByName =====================

    @Test
    void testGetByName_success() {

        Mockito.when(medicationRepository.findByName("Paracetamol"))
                .thenReturn(Optional.of(med));

        MedicationDTO result = medicationService.getByName("Paracetamol");

        assertEquals("Paracetamol", result.getName());

        Mockito.verify(medicationRepository).findByName("Paracetamol");
    }

    @Test
    void testGetByName_notFound() {

        Mockito.when(medicationRepository.findByName("XYZ"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medicationService.getByName("XYZ"));

        Mockito.verify(medicationRepository).findByName("XYZ");
    }

    // ===================== getByBrand =====================

    @Test
    void testGetByBrand_withData() {

        Mockito.when(medicationRepository.findByBrand("Cipla"))
                .thenReturn(List.of(med));

        List<MedicationDTO> result = medicationService.getByBrand("Cipla");

        assertEquals(1, result.size());

        Mockito.verify(medicationRepository).findByBrand("Cipla");
    }

    @Test
    void testGetByBrand_empty() {

        Mockito.when(medicationRepository.findByBrand("XYZ"))
                .thenReturn(Collections.emptyList());

        List<MedicationDTO> result = medicationService.getByBrand("XYZ");

        assertTrue(result.isEmpty());

        Mockito.verify(medicationRepository).findByBrand("XYZ");
    }

    // ===================== getByNameAndBrand =====================

    @Test
    void testGetByNameAndBrand_success() {

        Mockito.when(medicationRepository
                .findByNameAndBrand("Paracetamol", "Cipla"))
                .thenReturn(Optional.of(med));

        MedicationDTO result =
                medicationService.getByNameAndBrand("Paracetamol", "Cipla");

        assertEquals("Paracetamol", result.getName());

        Mockito.verify(medicationRepository)
                .findByNameAndBrand("Paracetamol", "Cipla");
    }

    @Test
    void testGetByNameAndBrand_notFound() {

        Mockito.when(medicationRepository
                .findByNameAndBrand("A", "B"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> medicationService.getByNameAndBrand("A", "B"));

        Mockito.verify(medicationRepository)
                .findByNameAndBrand("A", "B");
    }

    // ===================== create =====================

    @Test
    void testCreate_success() {

        MedicationDTO input =
                new MedicationDTO(null, "Paracetamol", "Cipla", "Fever");

        // ✅ ADD THIS (VERY IMPORTANT)
        Mockito.when(medicationRepository
                .findByNameAndBrand("Paracetamol", "Cipla"))
                .thenReturn(Optional.empty());

        Mockito.when(medicationRepository.save(Mockito.any(Medication.class)))
                .thenReturn(med);

        MedicationDTO result = medicationService.create(input);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());

        Mockito.verify(medicationRepository)
                .findByNameAndBrand("Paracetamol", "Cipla"); // optional but good
        Mockito.verify(medicationRepository)
                .save(Mockito.any(Medication.class));
    }
    
    @Test
    void testCreate_duplicate() {

        MedicationDTO input =
            new MedicationDTO(null, "Paracetamol", "Cipla", "Fever");

        Mockito.when(medicationRepository
                .findByNameAndBrand("Paracetamol", "Cipla"))
                .thenReturn(Optional.of(med));

        assertThrows(DuplicateResourceException.class,
                () -> medicationService.create(input));

        Mockito.verify(medicationRepository)
                .findByNameAndBrand("Paracetamol", "Cipla");
    }
    @Test
    void testGetById_invalid() {

        assertThrows(BadRequestException.class,
                () -> medicationService.getById(0));
    }
    
}