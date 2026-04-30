package com.cg.WebTest;

import com.cg.controller.DepartmentController;
import com.cg.dto.DepartmentDTO;
import com.cg.exception.DuplicateResourceException;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.DepartmentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    // ================= GET ALL =================
    @Test
    void testGetAll_success() throws Exception {

        List<DepartmentDTO> list = List.of(
                new DepartmentDTO(1, "Cardiology", 101, "Dr. John")
        );

        Mockito.when(departmentService.getAll()).thenReturn(list);

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cardiology"));

        Mockito.verify(departmentService).getAll();
    }

    // ================= GET BY ID =================
    @Test
    void testGetById_success() throws Exception {

        DepartmentDTO dto = new DepartmentDTO(1, "Cardiology", 101, "Dr. John");

        Mockito.when(departmentService.getById(1)).thenReturn(dto);

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));

        Mockito.verify(departmentService).getById(1);
    }

    @Test
    void testGetById_notFound() throws Exception {

        Mockito.when(departmentService.getById(1))
                .thenThrow(new ResourceNotFoundException("Department not found with id: 1"));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg")
                        .value("Department not found with id: 1"));

        Mockito.verify(departmentService).getById(1);
    }

    // ================= GET BY NAME =================
    @Test
    void testGetByName_success() throws Exception {

        DepartmentDTO dto = new DepartmentDTO(1, "Cardiology", 101, "Dr. John");

        Mockito.when(departmentService.getByName("Cardiology"))
                .thenReturn(dto);

        mockMvc.perform(get("/departments/name/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cardiology"));

        Mockito.verify(departmentService).getByName("Cardiology");
    }

    // ================= GET BY HEAD =================
    @Test
    void testGetByHeadId_success() throws Exception {

        List<DepartmentDTO> list = List.of(
                new DepartmentDTO(1, "Cardiology", 101, "Dr. John")
        );

        Mockito.when(departmentService.getByHeadId(101))
                .thenReturn(list);

        mockMvc.perform(get("/departments/head/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].headId").value(101));

        Mockito.verify(departmentService).getByHeadId(101);
    }

    // ================= CREATE =================
    @Test
    void testCreate_success() throws Exception {

        String requestJson = """
        {
            "name": "Cardiology",
            "headId": 101
        }
        """;

        DepartmentDTO response =
                new DepartmentDTO(1, "Cardiology", 101, "Dr. John");

        Mockito.when(departmentService.create(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cardiology"));

        Mockito.verify(departmentService).create(Mockito.any());
    }

    // ================= VALIDATION ERROR =================
    @Test
    void testCreate_validationError() throws Exception {

        String invalidJson = """
        {
            "name": "",
            "headId": null
        }
        """;

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg").value("Validation failed"))
                .andExpect(jsonPath("$.errMap.name").exists())
                .andExpect(jsonPath("$.errMap.headId").exists());

        Mockito.verify(departmentService, Mockito.never())
                .create(Mockito.any());
    }

    // ================= DUPLICATE ERROR =================
    @Test
    void testCreate_duplicate() throws Exception {

        String requestJson = """
        {
            "name": "Cardiology",
            "headId": 101
        }
        """;

        Mockito.when(departmentService.create(Mockito.any()))
                .thenThrow(new DuplicateResourceException(
                        "Department already exists with name: Cardiology"));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errMsg")
                        .value("Department already exists with name: Cardiology"));

        Mockito.verify(departmentService).create(Mockito.any());
    }

    // ================= PHYSICIAN NOT FOUND =================
    @Test
    void testCreate_headNotFound() throws Exception {

        String requestJson = """
        {
            "name": "Cardiology",
            "headId": 999
        }
        """;

        Mockito.when(departmentService.create(Mockito.any()))
                .thenThrow(new ResourceNotFoundException(
                        "Physician not found with id: 999"));

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg")
                        .value("Physician not found with id: 999"));

        Mockito.verify(departmentService).create(Mockito.any());
    }
}