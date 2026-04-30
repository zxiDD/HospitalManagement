package com.cg.WebTest;

import com.cg.controller.MedicationController;
import com.cg.dto.MedicationDTO;
import com.cg.exception.*;
import com.cg.service.MedicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationController.class)
@Import(GlobalExceptionHandler.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicationService medicationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // =========================
    // ✅ GET ALL
    // =========================
    @Test
    void testGetAll_success() throws Exception {

        Mockito.when(medicationService.getAll())
                .thenReturn(List.of(
                        new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"),
                        new MedicationDTO(2, "Aspirin", "Bayer", "Pain")
                ));

        mockMvc.perform(get("/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Paracetamol"))
                .andExpect(jsonPath("$[1].brand").value("Bayer"));

        Mockito.verify(medicationService).getAll();
    }

    // =========================
    // ✅ GET BY ID
    // =========================
    @Test
    void testGetById_success() throws Exception {

        Mockito.when(medicationService.getById(1))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paracetamol"));

        Mockito.verify(medicationService).getById(1);
    }

    @Test
    void testGetById_notFound() throws Exception {

        Mockito.when(medicationService.getById(1))
                .thenThrow(new ResourceNotFoundException("Medication not found"));

        mockMvc.perform(get("/medications/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Medication not found"));

        Mockito.verify(medicationService).getById(1);
    }

    // =========================
    // ✅ GET BY NAME
    // =========================
    @Test
    void testGetByName_success() throws Exception {

        Mockito.when(medicationService.getByName("Paracetamol"))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/name/Paracetamol"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Cipla"));

        Mockito.verify(medicationService).getByName("Paracetamol");
    }

    @Test
    void testGetByName_notFound() throws Exception {

        Mockito.when(medicationService.getByName("XYZ"))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/medications/name/XYZ"))
                .andExpect(status().isNotFound());

        Mockito.verify(medicationService).getByName("XYZ");
    }

    // =========================
    // ✅ GET BY BRAND
    // =========================
    @Test
    void testGetByBrand_success() throws Exception {

        Mockito.when(medicationService.getByBrand("Cipla"))
                .thenReturn(List.of(
                        new MedicationDTO(1, "Paracetamol", "Cipla", "Fever")
                ));

        mockMvc.perform(get("/medications/brand/Cipla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Paracetamol"));

        Mockito.verify(medicationService).getByBrand("Cipla");
    }

    // =========================
    // ✅ SEARCH (name + brand)
    // =========================
    @Test
    void testSearch_success() throws Exception {

        Mockito.when(medicationService.getByNameAndBrand("Paracetamol", "Cipla"))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/search")
                        .param("name", "Paracetamol")
                        .param("brand", "Cipla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Fever"));

        Mockito.verify(medicationService)
                .getByNameAndBrand("Paracetamol", "Cipla");
    }

    @Test
    void testSearch_notFound() throws Exception {

        Mockito.when(medicationService.getByNameAndBrand("A", "B"))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/medications/search")
                        .param("name", "A")
                        .param("brand", "B"))
                .andExpect(status().isNotFound());

        Mockito.verify(medicationService)
                .getByNameAndBrand("A", "B");
    }

    // =========================
    // ✅ CREATE SUCCESS
    // =========================
    @Test
    void testCreate_success() throws Exception {

        MedicationDTO input = new MedicationDTO(null, "Paracetamol", "Cipla", "Fever");
        MedicationDTO output = new MedicationDTO(1, "Paracetamol", "Cipla", "Fever");

        Mockito.when(medicationService.create(Mockito.any()))
                .thenReturn(output);

        mockMvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(1));

        Mockito.verify(medicationService).create(Mockito.any());
    }

    // =========================
    // ❌ DUPLICATE CASE
    // =========================
    @Test
    void testCreate_duplicate() throws Exception {

        MedicationDTO input = new MedicationDTO(null, "Paracetamol", "Cipla", "Fever");

        Mockito.when(medicationService.create(Mockito.any()))
                .thenThrow(new DuplicateResourceException("Already exists"));

        mockMvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errMsg").value("Already exists"));

        Mockito.verify(medicationService).create(Mockito.any());
    }

    // =========================
    // ❌ VALIDATION ERROR
    // =========================
    @Test
    void testCreate_validationError() throws Exception {

        MedicationDTO input = new MedicationDTO(null, "", "", "");

        mockMvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMap.name").exists())
                .andExpect(jsonPath("$.errMap.brand").exists());

        Mockito.verify(medicationService, Mockito.never()).create(Mockito.any());
    }
}