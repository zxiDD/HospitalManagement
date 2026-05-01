package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.DepartmentDTO;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.DepartmentService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // GET ALL
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll_success() throws Exception {

        when(departmentService.getAll())
                .thenReturn(List.of(
                        new DepartmentDTO(1, "Cardiology", 101, "Dr John")
                ));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cardiology"));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_success() throws Exception {

        when(departmentService.getById(1))
                .thenReturn(new DepartmentDTO(1, "Cardiology", 101, "Dr John"));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById_notFound() throws Exception {

        when(departmentService.getById(1))
                .thenThrow(new ResourceNotFoundException("Department not found with id: 1"));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg")
                .value("Department not found with id: 1"));
    }

    // =========================
    // GET BY NAME
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByName_success() throws Exception {

        when(departmentService.getByName("Cardiology"))
                .thenReturn(new DepartmentDTO(1, "Cardiology", 101, "Dr John"));

        mockMvc.perform(get("/departments/name/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    // =========================
    // GET BY HEAD
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByHead_success() throws Exception {

        when(departmentService.getByHeadId(101))
                .thenReturn(List.of(
                        new DepartmentDTO(1, "Cardiology", 101, "Dr John")
                ));

        mockMvc.perform(get("/departments/head/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].headId").value(101));
    }

    // =========================
    // CREATE SUCCESS
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_success() throws Exception {

        DepartmentDTO input = new DepartmentDTO(null, "Cardiology", 101, null);

        when(departmentService.create(org.mockito.Mockito.any()))
                .thenReturn(new DepartmentDTO(1, "Cardiology", 101, "Dr John"));

        mockMvc.perform(post("/admin/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    // =========================
    // CREATE VALIDATION ERROR
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_validationError() throws Exception {

        DepartmentDTO invalid = new DepartmentDTO(null, "", null, null);
        when(departmentService.create(any())).thenReturn(null);
        mockMvc.perform(post("/admin/departments")
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

        DepartmentDTO input = new DepartmentDTO(null, "Cardiology", 101, null);

        when(departmentService.create(org.mockito.Mockito.any()))
                .thenThrow(new DuplicateResourceException("Department already exists with name: Cardiology"));

        mockMvc.perform(post("/admin/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errMsg")
                .value("Department already exists with name: Cardiology"));
    }

    // =========================
    // CREATE HEAD NOT FOUND
    // =========================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate_headNotFound() throws Exception {

        DepartmentDTO input = new DepartmentDTO(null, "Cardiology", 999, null);

        Mockito.when(departmentService.create(org.mockito.Mockito.any()))
                .thenThrow(new ResourceNotFoundException("Physician not found with id: 999"));

        mockMvc.perform(post("/admin/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg")
                .value("Physician not found with id: 999"));
    }
}