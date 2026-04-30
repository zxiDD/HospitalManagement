package com.cg.WebTest;

import com.cg.controller.StayController;
import com.cg.dto.StayDTO;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.StayService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StayController.class)
@Import(GlobalExceptionHandler.class)
public class StayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StayService stayService;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    // =========================
    // GET ALL
    // =========================
    @Test
    void testGetAll_success() throws Exception {

        Mockito.when(stayService.getAll())
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName").value("John"));

        Mockito.verify(stayService).getAll();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void testGetById_success() throws Exception {

        StayDTO dto = new StayDTO(1, 123L, "John", 101, "ICU",
                LocalDateTime.now(), null);

        Mockito.when(stayService.getById(1)).thenReturn(dto);

        mockMvc.perform(get("/stays/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName").value("John"));

        Mockito.verify(stayService).getById(1);
    }

    @Test
    void testGetById_notFound() throws Exception {

        Mockito.when(stayService.getById(1))
                .thenThrow(new ResourceNotFoundException("Stay not found"));

        mockMvc.perform(get("/stays/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errMsg").value("Stay not found"));

        Mockito.verify(stayService).getById(1);
    }

    // =========================
    // GET BY PATIENT
    // =========================
    @Test
    void testGetByPatient_success() throws Exception {

        Mockito.when(stayService.getByPatientSsn(123L))
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays/patient/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName").value("John"));

        Mockito.verify(stayService).getByPatientSsn(123L);
    }

    // =========================
    // GET BY ROOM
    // =========================
    @Test
    void testGetByRoom_success() throws Exception {

        Mockito.when(stayService.getByRoomNumber(101))
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays/room/101"))
                .andExpect(status().isOk());

        Mockito.verify(stayService).getByRoomNumber(101);
    }

    // =========================
    // GET AFTER DATE
    // =========================
    @Test
    void testGetAfter_success() throws Exception {

        Mockito.when(stayService.getStaysAfter(Mockito.any()))
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays/after")
                        .param("dateTime", "2025-01-01T10:00:00"))
                .andExpect(status().isOk());

        Mockito.verify(stayService).getStaysAfter(Mockito.any());
    }

    // =========================
    // GET ACTIVE
    // =========================
    @Test
    void testGetActive_success() throws Exception {

        Mockito.when(stayService.getActiveStays())
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays/active"))
                .andExpect(status().isOk());

        Mockito.verify(stayService).getActiveStays();
    }

    // =========================
    // GET HISTORY
    // =========================
    @Test
    void testGetHistory_success() throws Exception {

        Mockito.when(stayService.getPatientStayHistory(123L))
                .thenReturn(List.of(
                        new StayDTO(1, 123L, "John", 101, "ICU",
                                LocalDateTime.now(), null)
                ));

        mockMvc.perform(get("/stays/history/123"))
                .andExpect(status().isOk());

        Mockito.verify(stayService).getPatientStayHistory(123L);
    }

    // =========================
    // CREATE SUCCESS
    // =========================
    @Test
    void testCreate_success() throws Exception {

        StayDTO input = new StayDTO(null, 123L, null, 101, null,
                LocalDateTime.now(), null);

        StayDTO output = new StayDTO(1, 123L, "John", 101, "ICU",
                LocalDateTime.now(), null);

        Mockito.when(stayService.create(Mockito.any())).thenReturn(output);

        mockMvc.perform(post("/stays")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stayId").value(1));

        Mockito.verify(stayService).create(Mockito.any());
    }

    // =========================
    // CREATE VALIDATION ERROR
    // =========================
    @Test
    void testCreate_validationError() throws Exception {

        StayDTO invalid = new StayDTO();

        mockMvc.perform(post("/stays")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errMsg").value("Validation failed"));

        Mockito.verify(stayService, Mockito.never()).create(Mockito.any());
    }

    // =========================
    // IS PATIENT ACTIVE
    // =========================
    @Test
    void testIsPatientActive_true() throws Exception {

        Mockito.when(stayService.isPatientActive(123L)).thenReturn(true);

        mockMvc.perform(get("/stays/active/patient/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        Mockito.verify(stayService).isPatientActive(123L);
    }

    @Test
    void testIsPatientActive_false() throws Exception {

        Mockito.when(stayService.isPatientActive(123L)).thenReturn(false);

        mockMvc.perform(get("/stays/active/patient/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        Mockito.verify(stayService).isPatientActive(123L);
    }
}