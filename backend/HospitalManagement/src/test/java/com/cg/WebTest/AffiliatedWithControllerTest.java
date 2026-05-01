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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.AffiliatedWithDTO;
import com.cg.dto.DepartmentDTO;
import com.cg.entity.AffiliatedWithId;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.AffiliatedWithService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
public class AffiliatedWithControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AffiliatedWithService service;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // CREATE SUCCESS
    // =========================
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreate_success() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(100, null, 10, null, true);

        when(service.create(org.mockito.Mockito.any()))
                .thenReturn(new AffiliatedWithDTO(100, "Dr. Smith", 10, "Cardiology", true));

        mockMvc.perform(post("/admin/affiliations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.physicianId").value(100))
                .andExpect(jsonPath("$.departmentName").value("Cardiology"));
    }

    // =========================
    // CREATE VALIDATION ERROR
    // =========================
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreate_validationError() throws Exception {

        AffiliatedWithDTO dto =
                new AffiliatedWithDTO(null, null, null, null, null);
        when(service.create(any())).thenReturn(null);

        mockMvc.perform(post("/admin/affiliations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg").value("Validation failed"));
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetAll_success() throws Exception {

        when(service.getAll()).thenReturn(List.of(
                new AffiliatedWithDTO(100, "Dr. Smith", 10, "Cardiology", true)
        ));

        mockMvc.perform(get("/affiliations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].physicianName").value("Dr. Smith"));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetById_success() throws Exception {

        when(service.getById(org.mockito.Mockito.any(AffiliatedWithId.class)))
                .thenReturn(new AffiliatedWithDTO(100, "Dr. Smith", 10, "Cardiology", true));

        mockMvc.perform(get("/affiliations/100/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Cardiology"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetById_notFound() throws Exception {

        when(service.getById(org.mockito.Mockito.any(AffiliatedWithId.class)))
                .thenThrow(new ResourceNotFoundException("Affiliation not found"));

        mockMvc.perform(get("/affiliations/100/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Affiliation not found"));
    }

    // =========================
    // GET PRIMARY DEPARTMENT
    // =========================
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetPrimaryDepartment_success() throws Exception {

        DepartmentDTO dept = new DepartmentDTO(10, "Cardiology", 100, "Dr. Smith");

        when(service.getPrimaryDepartment(100)).thenReturn(dept);

        mockMvc.perform(get("/affilliations/primary/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetPrimaryDepartment_notFound() throws Exception {

        when(service.getPrimaryDepartment(100))
                .thenThrow(new ResourceNotFoundException("Primary affiliation not found"));

        mockMvc.perform(get("/affilliations/primary/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Primary affiliation not found"));
    }
}