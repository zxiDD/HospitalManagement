package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.MedicationDTO;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.MedicationService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicationService medicationService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // GET ALL
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_success() throws Exception {

        when(medicationService.getAll())
                .thenReturn(List.of(
                        new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"),
                        new MedicationDTO(2, "Aspirin", "Bayer", "Pain")
                ));

        mockMvc.perform(get("/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Paracetamol"))
                .andExpect(jsonPath("$[1].brand").value("Bayer"));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_success() throws Exception {

        when(medicationService.getById(1))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_notFound() throws Exception {

        when(medicationService.getById(1))
                .thenThrow(new ResourceNotFoundException("Medication not found"));

        mockMvc.perform(get("/medications/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Medication not found"));
    }

    // =========================
    // GET BY NAME
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByName_success() throws Exception {

        when(medicationService.getByName("Paracetamol"))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/name/Paracetamol"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Cipla"));
    }

    // =========================
    // GET BY BRAND
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByBrand_success() throws Exception {

        when(medicationService.getByBrand("Cipla"))
                .thenReturn(List.of(
                        new MedicationDTO(1, "Paracetamol", "Cipla", "Fever")
                ));

        mockMvc.perform(get("/medications/brand/Cipla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Cipla"));
    }

    // =========================
    // SEARCH
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearch_success() throws Exception {

        when(medicationService.getByNameAndBrand("Paracetamol", "Cipla"))
                .thenReturn(new MedicationDTO(1, "Paracetamol", "Cipla", "Fever"));

        mockMvc.perform(get("/medications/search")
                        .param("name", "Paracetamol")
                        .param("brand", "Cipla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paracetamol"));
    }

    // =========================
    // CREATE SUCCESS
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_success() throws Exception {

        MedicationDTO input = new MedicationDTO(null, "Ibuprofen", "Pfizer", "Pain");

        when(medicationService.create(any()))
                .thenReturn(new MedicationDTO(3, "Ibuprofen", "Pfizer", "Pain"));

        mockMvc.perform(post("/admin/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ibuprofen"));
    }

    // =========================
    // CREATE VALIDATION
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_validationError() throws Exception {

        MedicationDTO invalid = new MedicationDTO(null, "", "", "");

        when(medicationService.create(any())).thenReturn(null); // workaround

        mockMvc.perform(post("/admin/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg").value("Validation failed"));
    }

    // =========================
    // CREATE DUPLICATE
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_duplicate() throws Exception {

        MedicationDTO input = new MedicationDTO(null, "Ibuprofen", "Pfizer", "Pain");

        when(medicationService.create(any()))
                .thenThrow(new DuplicateResourceException("Medication already exists"));

        mockMvc.perform(post("/admin/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errMsg").value("Medication already exists"));
    }
}