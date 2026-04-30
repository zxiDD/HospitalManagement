package com.cg.WebTest;

import com.cg.controller.AffiliatedWithController;
import com.cg.dto.AffiliatedWithDTO;
import com.cg.dto.DepartmentDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.AffiliatedWithService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AffiliatedWithController.class)
@Import(GlobalExceptionHandler.class)
public class AffiliatedWithControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AffiliatedWithService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // =========================
    // GET ALL
    // =========================
    @Test
    void testGetAll_success() throws Exception {

        List<AffiliatedWithDTO> list = List.of(
                new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true)
        );

        Mockito.when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/affiliations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].physicianName").value("Dr A"));

        Mockito.verify(service).getAll();
    }

    // =========================
    // GET BY ID (Composite)
    // =========================
    @Test
    void testGetById_success() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true);

        Mockito.when(service.getById(Mockito.any(AffiliatedWithId.class)))
                .thenReturn(dto);

        mockMvc.perform(get("/affiliations/1/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Cardio"));

        Mockito.verify(service).getById(Mockito.any(AffiliatedWithId.class));
    }

    @Test
    void testGetById_notFound() throws Exception {

        Mockito.when(service.getById(Mockito.any(AffiliatedWithId.class)))
                .thenThrow(new ResourceNotFoundException("Affiliation not found"));

        mockMvc.perform(get("/affiliations/1/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Affiliation not found"));

        Mockito.verify(service).getById(Mockito.any(AffiliatedWithId.class));
    }

    // =========================
    // GET BY PHYSICIAN
    // =========================
    @Test
    void testGetByPhysician_success() throws Exception {

        Mockito.when(service.getByPhysicianId(1))
                .thenReturn(List.of(
                        new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true)
                ));

        mockMvc.perform(get("/affiliations/physician/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].physicianName").value("Dr A"));

        Mockito.verify(service).getByPhysicianId(1);
    }

    // =========================
    // GET BY DEPARTMENT
    // =========================
    @Test
    void testGetByDepartment_success() throws Exception {

        Mockito.when(service.getByDepartmentId(10))
                .thenReturn(List.of(
                        new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true)
                ));

        mockMvc.perform(get("/affiliations/department/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departmentName").value("Cardio"));

        Mockito.verify(service).getByDepartmentId(10);
    }

    // =========================
    // GET PRIMARY AFFILIATIONS
    // =========================
    @Test
    void testGetPrimaryAffiliations_success() throws Exception {

        Mockito.when(service.getPrimaryAffiliations())
                .thenReturn(List.of(
                        new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true)
                ));

        mockMvc.perform(get("/affiliations/primary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].primaryAffiliation").value(true));

        Mockito.verify(service).getPrimaryAffiliations();
    }

    // =========================
    // GET PRIMARY DEPARTMENT
    // =========================
    @Test
    void testGetPrimaryDepartment_success() throws Exception {

        DepartmentDTO dept =
                new DepartmentDTO(10, "Cardio", 1, "Dr A");

        Mockito.when(service.getPrimaryDepartment(1))
                .thenReturn(dept);

        mockMvc.perform(get("/affiliations/primary/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardio"));

        Mockito.verify(service).getPrimaryDepartment(1);
    }

    @Test
    void testGetPrimaryDepartment_notFound() throws Exception {

        Mockito.when(service.getPrimaryDepartment(1))
                .thenThrow(new ResourceNotFoundException("Primary affiliation not found"));

        mockMvc.perform(get("/affiliations/primary/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Primary affiliation not found"));

        Mockito.verify(service).getPrimaryDepartment(1);
    }

    // =========================
    // CREATE SUCCESS
    // =========================
    @Test
    void testCreate_success() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(1, null, 10, null, true);

        Mockito.when(service.create(Mockito.any()))
                .thenReturn(new AffiliatedWithDTO(1, "Dr A", 10, "Cardio", true));

        mockMvc.perform(post("/affiliations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departmentName").value("Cardio"));

        Mockito.verify(service).create(Mockito.any());
    }

    // =========================
    // CREATE DUPLICATE
    // =========================
    @Test
    void testCreate_duplicate() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(1, null, 10, null, true);

        Mockito.when(service.create(Mockito.any()))
                .thenThrow(new DuplicateResourceException("Primary affiliation already exists"));

        mockMvc.perform(post("/affiliations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errMsg").value("Primary affiliation already exists"));

        Mockito.verify(service).create(Mockito.any());
    }

    // =========================
    // CREATE VALIDATION ERROR
    // =========================
    @Test
    void testCreate_validationError() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(null, null, null, null, null);

        mockMvc.perform(post("/affiliations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg").value("Validation failed"));

        Mockito.verify(service, Mockito.never()).create(Mockito.any());
    }
}