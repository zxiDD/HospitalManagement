package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.dto.TrainedInDTO;
import com.cg.entity.Physician;
import com.cg.entity.Procedures;
import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
import com.cg.service.TrainedInService;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class TrainedInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainedInService service;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTraining_success() throws Exception {

        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2027, 1, 1)
        );

        Physician physician = new Physician();
        physician.setEmployeeId(100);

        Procedures procedures = new Procedures();
        procedures.setCode(101);

        TrainedInId id = new TrainedInId(100, 101);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setId(id);
        trainedIn.setPhysician(physician);
        trainedIn.setTreatment(procedures);
        trainedIn.setCertificationDate(LocalDateTime.now().minusMonths(6));
        trainedIn.setCertificationExpires(LocalDateTime.now().plusMonths(6));

        when(service.save(any())).thenReturn(trainedIn);

        mockMvc.perform(post("/admin/trainedin/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.physician").value(100))
                .andExpect(jsonPath("$.id.treatment").value(101));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTraining_invalidDate() throws Exception {

        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2023, 1, 1)
        );

        mockMvc.perform(post("/admin/trainedin/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllTraining_success() throws Exception {

        Physician physician = new Physician();
        physician.setEmployeeId(100);

        Procedures procedures = new Procedures();
        procedures.setCode(101);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setId(new TrainedInId(100, 101));
        trainedIn.setPhysician(physician);
        trainedIn.setTreatment(procedures);
        trainedIn.setCertificationDate(LocalDateTime.now());
        trainedIn.setCertificationExpires(LocalDateTime.now().plusMonths(6));

        when(service.getAll()).thenReturn(List.of(trainedIn));

        mockMvc.perform(get("/trainedin"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByPhysician_success() throws Exception {

        Physician physician = new Physician();
        physician.setEmployeeId(100);

        Procedures procedures = new Procedures();
        procedures.setCode(101);

        TrainedIn trainedIn = new TrainedIn();
        trainedIn.setId(new TrainedInId(100, 101));
        trainedIn.setPhysician(physician);
        trainedIn.setTreatment(procedures);
        trainedIn.setCertificationDate(LocalDateTime.now());
        trainedIn.setCertificationExpires(LocalDateTime.now().plusMonths(6));

        when(service.getByPhysicianId(100)).thenReturn(List.of(trainedIn));

        mockMvc.perform(get("/trainedin/physician/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].physicianId").value(100))
                .andExpect(jsonPath("$[0].treatmentId").value(101));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByPhysician_empty() throws Exception {

        when(service.getByPhysicianId(100)).thenReturn(List.of());

        mockMvc.perform(get("/trainedin/physician/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTraining_verifyServiceCall() throws Exception {

        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2027, 1, 1)
        );

        when(service.save(any())).thenReturn(new TrainedIn());

        mockMvc.perform(post("/admin/trainedin/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).save(any());
    }
}