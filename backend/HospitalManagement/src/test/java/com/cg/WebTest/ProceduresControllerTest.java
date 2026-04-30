package com.cg.WebTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.cg.controller.ProceduresController;
import com.cg.dto.ProceduresDTO;
import com.cg.entity.Procedures;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.ProceduresService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
@SpringBootTest
public class ProceduresControllerTest {

    @Autowired
    private ProceduresController controller;

    @MockitoBean
    private ProceduresService proceduresService;

    private Procedures procedure;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        procedure = new Procedures();
        procedure.setCode(101);
        procedure.setName("Cardiac Surgery");
        procedure.setCost(new BigDecimal("5000.00"));
    }

    @Test
    void testGetAllProcedures() {
        when(proceduresService.getAllProcedures()).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getAllProcedures();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProcedureById_Found() {
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedure));

        ResponseEntity<ProceduresDTO> response = controller.getProcedureById(101);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(101, response.getBody().getCode());
    }


    @Test
    void testGetProcedureById_NotFound() {
        when(proceduresService.getProcedureById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> controller.getProcedureById(999));
    }


    @Test
    void testGetProceduresByName() {
        when(proceduresService.getProceduresByName("Cardiac Surgery")).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByName("Cardiac Surgery");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetProceduresByCost() {
        BigDecimal cost = new BigDecimal("5000.00");
        when(proceduresService.getProceduresByCost(cost)).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByCost(cost);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProceduresByCostLessThan() {
        BigDecimal cost = new BigDecimal("10000.00");
        when(proceduresService.getProceduresByCostLessThan(cost)).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByCostLessThan(cost);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProceduresByCostGreaterThan() {
        BigDecimal cost = new BigDecimal("1000.00");
        when(proceduresService.getProceduresByCostGreaterThan(cost)).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByCostGreaterThan(cost);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetProceduresByCostBetween() {
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("10000.00");
        when(proceduresService.getProceduresByCostBetween(min, max)).thenReturn(List.of(procedure));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByCostBetween(min, max);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateProcedure() {
        ProceduresDTO dto = new ProceduresDTO(101, "Cardiac Surgery", new BigDecimal("5000.00"));

        when(proceduresService.saveProcedures(any(Procedures.class))).thenReturn(procedure);

        ResponseEntity<ProceduresDTO> response = controller.createProcedure(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllProcedures_Empty() {
        when(proceduresService.getAllProcedures()).thenReturn(List.of());

        ResponseEntity<List<ProceduresDTO>> response = controller.getAllProcedures();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
    @Test
    void testGetProceduresByName_NotFound() {
        when(proceduresService.getProceduresByName("Unknown")).thenReturn(List.of());

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByName("Unknown");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetProceduresByCost_Multiple() {
        Procedures procedure2 = new Procedures();
        procedure2.setCode(102);
        procedure2.setName("Appendectomy");
        procedure2.setCost(new BigDecimal("5000.00"));

        BigDecimal cost = new BigDecimal("5000.00");
        when(proceduresService.getProceduresByCost(cost)).thenReturn(List.of(procedure, procedure2));

        ResponseEntity<List<ProceduresDTO>> response = controller.getProceduresByCost(cost);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testVerifyDTOConversion() {
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedure));

        ResponseEntity<ProceduresDTO> response = controller.getProcedureById(101);

        assertEquals("Cardiac Surgery", response.getBody().getName());
        assertEquals(new BigDecimal("5000.00"), response.getBody().getCost());
    }

    @Test
    void testCreateProcedure_VerifySavedData() {
        ProceduresDTO dto = new ProceduresDTO(101, "Cardiac Surgery", new BigDecimal("5000.00"));

        when(proceduresService.saveProcedures(any(Procedures.class))).thenReturn(procedure);

        ResponseEntity<ProceduresDTO> response = controller.createProcedure(dto);

        assertNotNull(response.getBody());
        assertEquals(101, response.getBody().getCode());
        verify(proceduresService, times(1)).saveProcedures(any(Procedures.class));
    }
}