package com.cg.WebTest;

import com.cg.controller.PhysicianController;
import com.cg.dto.PhysicianDTO;
import com.cg.entity.Physician;
import com.cg.exception.ValidationException;
import com.cg.service.PhysicianService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhysicianControllerTest {

    @Mock
    private PhysicianService service;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PhysicianController controller;

    private Physician physician;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. Smith");
        physician.setPosition("Cardiologist");
        physician.setSsn(1111L);
    }

    // ✅ getAll
    @Test
    void testGetAll() {
        when(service.getAll()).thenReturn(List.of(physician));

        ResponseEntity<List<PhysicianDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getAll();
    }

    // ✅ getById
    @Test
    void testGetById() {
        when(service.getById(1)).thenReturn(physician);

        ResponseEntity<PhysicianDTO> response = controller.getById(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dr. Smith", response.getBody().getName());
        verify(service).getById(1);
    }

    // ✅ create SUCCESS
    @Test
    void testCreate_Success() {

        PhysicianDTO dto = new PhysicianDTO(null, "Dr. Smith", "Cardiologist", 1111L);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(service.save(any(Physician.class))).thenReturn(physician);

        ResponseEntity<PhysicianDTO> response = controller.create(dto, bindingResult);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Dr. Smith", response.getBody().getName());

        verify(service).save(any(Physician.class));
    }

    // ❌ create VALIDATION ERROR
    @Test
    void testCreate_ValidationError() {

        PhysicianDTO dto = new PhysicianDTO(null, "Dr. Smith", "Cardio", 1111L);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> controller.create(dto, bindingResult));

        verify(service, never()).save(any());
    }

    // ✅ update
    @Test
    void testUpdate() {

        PhysicianDTO dto = new PhysicianDTO(null, "Dr. John", "Neuro", 2222L);

        when(service.getById(1)).thenReturn(physician);
        when(service.save(any(Physician.class))).thenReturn(physician);

        ResponseEntity<PhysicianDTO> response =
                controller.update(1, dto);

        assertEquals(200, response.getStatusCode().value());
        verify(service).save(any(Physician.class));
    }

    // ✅ getByPosition
    @Test
    void testGetByPosition() {

        when(service.getByPosition("Cardiologist"))
                .thenReturn(List.of(physician));

        ResponseEntity<List<PhysicianDTO>> response =
                controller.getByPosition("Cardiologist");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // ✅ delete
    @Test
    void testDelete() {

        doNothing().when(service).delete(1);

        ResponseEntity<Void> response = controller.delete(1);

        assertEquals(204, response.getStatusCode().value());
        verify(service).delete(1);
    }
}