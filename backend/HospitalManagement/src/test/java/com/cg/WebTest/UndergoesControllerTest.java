package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.StayDTO;
import com.cg.dto.UndergoesDTO;
import com.cg.entity.*;
import com.cg.service.*;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class UndergoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UndergoesService undergoesService;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private ProceduresService proceduresService;

    @MockitoBean
    private StayService stayService;

    @MockitoBean
    private NurseService nurseService;

    @MockitoBean
    private RoomService roomService;

    @MockitoBean
    private PhysicianService physicianService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUndergoes_success() throws Exception {

        Undergoes u = new Undergoes();
        when(undergoesService.getAllUndergoes()).thenReturn(List.of(u));

        mockMvc.perform(get("/undergoes"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByPatient_success() throws Exception {

        Undergoes u = new Undergoes();
        when(undergoesService.getUndergoesByPatient(12345L)).thenReturn(List.of(u));

        mockMvc.perform(get("/undergoes/patient/12345"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByProcedure_success() throws Exception {

        when(undergoesService.getUndergoesByProcedure(101)).thenReturn(List.of(new Undergoes()));

        mockMvc.perform(get("/undergoes/procedure/101"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByPhysician_success() throws Exception {

        when(undergoesService.getUndergoesByPhysician(100)).thenReturn(List.of(new Undergoes()));

        mockMvc.perform(get("/undergoes/physician/100"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByNurse_success() throws Exception {

        when(undergoesService.getUndergoesByAssistingNurse(50)).thenReturn(List.of(new Undergoes()));

        mockMvc.perform(get("/undergoes/nurse/50"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getBetweenDates_success() throws Exception {

        when(undergoesService.getUndergoesBetweenDates(any(), any()))
                .thenReturn(List.of(new Undergoes()));

        mockMvc.perform(get("/undergoes/between")
                .param("start", "2024-01-01T10:00:00")
                .param("end", "2024-01-10T10:00:00"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUndergoes_success() throws Exception {

        UndergoesDTO dto = new UndergoesDTO(
                12345L, 101, 1,
                LocalDateTime.now(),
                100, 50
        );

        Patient patient = new Patient();
        patient.setSsn(12345L);

        Procedures procedures = new Procedures();
        procedures.setCode(101);

        StayDTO stayDTO = new StayDTO();
        stayDTO.setStayId(1);
        stayDTO.setRoomNumber(201);

        Room room = new Room();
        room.setRoomNumber(201);

        Physician physician = new Physician();
        physician.setEmployeeId(100);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(50);

        when(patientService.getById(12345L)).thenReturn(patient);
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedures));
        when(stayService.getById(1)).thenReturn(stayDTO);
        when(roomService.getRoomById(201)).thenReturn(Optional.of(room));
        when(physicianService.getById(100)).thenReturn(physician);
        when(nurseService.getById(50)).thenReturn(nurse);
        when(undergoesService.saveUndergoes(any())).thenReturn(new Undergoes());

        mockMvc.perform(post("/admin/undergoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUndergoes_procedureNotFound() throws Exception {

        UndergoesDTO dto = new UndergoesDTO(
                12345L, 999, 1,
                LocalDateTime.now(),
                100, 50
        );

        when(patientService.getById(12345L)).thenReturn(new Patient());
        when(proceduresService.getProcedureById(999)).thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/undergoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}