package com.cg.WebTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cg.controller.MedicationController;
import com.cg.dto.MedicationDTO;
import com.cg.entity.Medication;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.ValidationException;
import com.cg.service.MedicationService;

@SpringBootTest
public class MedicationControllerTest {

    @Autowired
    private MedicationController controller;

    @MockitoBean
    private MedicationService medicationService;

    private Medication medication1;
    private Medication medication2;
    private MedicationDTO medicationDTO1;
    private MedicationDTO medicationDTO2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        medication1 = new Medication();
        medication1.setCode(1);
        medication1.setName("Paracetamol");
        medication1.setBrand("Cipla");
        medication1.setDescription("Fever");

        medication2 = new Medication();
        medication2.setCode(2);
        medication2.setName("Aspirin");
        medication2.setBrand("Bayer");
        medication2.setDescription("Pain");

        medicationDTO1 = new MedicationDTO(1, "Paracetamol", "Cipla", "Fever");
        medicationDTO2 = new MedicationDTO(2, "Aspirin", "Bayer", "Pain");
    }

    @Test
    void testGetAll_Success() {
        when(medicationService.getAll()).thenReturn(List.of(medicationDTO1, medicationDTO2));

        ResponseEntity<List<MedicationDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Paracetamol", response.getBody().get(0).getName());
        assertEquals("Bayer", response.getBody().get(1).getBrand());
    }

    @Test
    void testGetAll_Empty() {
        when(medicationService.getAll()).thenReturn(List.of());

        ResponseEntity<List<MedicationDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetById_Found() {
        when(medicationService.getById(1)).thenReturn(medicationDTO1);

        ResponseEntity<MedicationDTO> response = controller.getById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getCode());
        assertEquals("Paracetamol", response.getBody().getName());
    }

    @Test
    void testGetById_NotFound() {
        when(medicationService.getById(999)).thenThrow(new ResourceNotFoundException("Medication not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getById(999));
    }

    @Test
    void testGetByName_Found() {
        when(medicationService.getByName("Paracetamol")).thenReturn(medicationDTO1);

        ResponseEntity<MedicationDTO> response = controller.getByName("Paracetamol");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Paracetamol", response.getBody().getName());
        assertEquals("Cipla", response.getBody().getBrand());
    }

    @Test
    void testGetByName_NotFound() {
        when(medicationService.getByName("NonExistent")).thenThrow(new ResourceNotFoundException("Medication not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getByName("NonExistent"));
    }

    @Test
    void testGetByBrand_Success() {
        when(medicationService.getByBrand("Cipla")).thenReturn(List.of(medicationDTO1));

        ResponseEntity<List<MedicationDTO>> response = controller.getByBrand("Cipla");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Cipla", response.getBody().get(0).getBrand());
    }

    @Test
    void testGetByBrand_Empty() {
        when(medicationService.getByBrand("UnknownBrand")).thenReturn(List.of());

        ResponseEntity<List<MedicationDTO>> response = controller.getByBrand("UnknownBrand");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetByNameAndBrand_Found() {
        when(medicationService.getByNameAndBrand("Paracetamol", "Cipla")).thenReturn(medicationDTO1);

        ResponseEntity<MedicationDTO> response = controller.getByNameAndBrand("Paracetamol", "Cipla");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Paracetamol", response.getBody().getName());
        assertEquals("Cipla", response.getBody().getBrand());
    }

    @Test
    void testGetByNameAndBrand_NotFound() {
        when(medicationService.getByNameAndBrand("Invalid", "Invalid")).thenThrow(new ResourceNotFoundException("Medication not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getByNameAndBrand("Invalid", "Invalid"));
    }

    @Test
    void testCreateMedication_Success() {
        MedicationDTO dto = new MedicationDTO(null, "Ibuprofen", "Pfizer", "Pain");
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);

        when(medicationService.create(any(MedicationDTO.class))).thenReturn(new MedicationDTO(3, "Ibuprofen", "Pfizer", "Pain"));

        ResponseEntity<MedicationDTO> response = controller.createMedication(dto, br);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Ibuprofen", response.getBody().getName());
        assertEquals("Pfizer", response.getBody().getBrand());
    }

    @Test
    void testCreateMedication_InvalidName() {
        MedicationDTO dto = new MedicationDTO(null, "", "Pfizer", "Pain");
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("medicationDTO", "name", "Medication name is required"));
        when(br.getFieldErrors()).thenReturn(errors);

        when(medicationService.create(any(MedicationDTO.class))).thenThrow(new ValidationException(errors));

        assertThrows(ValidationException.class, () -> controller.createMedication(dto, br));
    }

    @Test
    void testCreateMedication_InvalidBrand() {
        MedicationDTO dto = new MedicationDTO(null, "Ibuprofen", "", "Pain");
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("medicationDTO", "brand", "Brand is required"));
        when(br.getFieldErrors()).thenReturn(errors);

        when(medicationService.create(any(MedicationDTO.class))).thenThrow(new ValidationException(errors));

        assertThrows(ValidationException.class, () -> controller.createMedication(dto, br));
    }

    @Test
    void testCreateMedication_InvalidDescription() {
        MedicationDTO dto = new MedicationDTO(null, "Ibuprofen", "Pfizer", "");
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("medicationDTO", "description", "Description is required"));
        when(br.getFieldErrors()).thenReturn(errors);

        when(medicationService.create(any(MedicationDTO.class))).thenThrow(new ValidationException(errors));

        assertThrows(ValidationException.class, () -> controller.createMedication(dto, br));
    }
}