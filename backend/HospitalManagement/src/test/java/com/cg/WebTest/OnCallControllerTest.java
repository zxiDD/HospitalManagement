package com.cg.WebTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

import com.cg.dto.OnCallDTO;
import com.cg.entity.Nurse;
import com.cg.entity.OnCall;
import com.cg.entity.OnCallId;
import com.cg.service.OnCallService;

import tools.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class OnCallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OnCallService service;

    @Autowired
   private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createOnCall_success() throws Exception {

        OnCallDTO input = new OnCallDTO(
                1, 2, 3,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 1, 18, 0)
        );

        OnCallId id = new OnCallId(1, 2, 3);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);

        OnCall onCall = new OnCall();
        onCall.setId(id);
        onCall.setNurse(nurse);
        onCall.setOnCallStart(LocalDateTime.of(2024, 1, 1, 10, 0));
        onCall.setOnCallEnd(LocalDateTime.of(2024, 1, 1, 18, 0));

        when(service.save(any())).thenReturn(onCall);

        mockMvc.perform(post("/admin/oncall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nurseId").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createOnCall_validationFailure() throws Exception {

        OnCallDTO invalid = new OnCallDTO(null, null, null, null, null);

        mockMvc.perform(post("/admin/oncall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllOnCalls_success() throws Exception {

        OnCallId id = new OnCallId(1, 2, 3);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);

        OnCall onCall = new OnCall();
        onCall.setId(id);
        onCall.setNurse(nurse);
        onCall.setOnCallStart(LocalDateTime.now());
        onCall.setOnCallEnd(LocalDateTime.now().plusHours(2));

        when(service.getAll()).thenReturn(List.of(onCall));

        mockMvc.perform(get("/oncall"))
                .andExpect(status().isOk());
    }

 
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByNurse_success() throws Exception {

        OnCallId id = new OnCallId(1, 2, 3);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);

        OnCall onCall = new OnCall();
        onCall.setId(id);
        onCall.setNurse(nurse);
        onCall.setOnCallStart(LocalDateTime.now());
        onCall.setOnCallEnd(LocalDateTime.now().plusHours(2));

        when(service.getByNurseId(1)).thenReturn(List.of(onCall));

        mockMvc.perform(get("/oncall/nurse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nurseId").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createOnCall_invalidTime() throws Exception {

        OnCallDTO dto = new OnCallDTO(
                1, 2, 3,
                LocalDateTime.of(2024, 1, 2, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        mockMvc.perform(post("/admin/oncall")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteOnCall_success() throws Exception {

        doNothing().when(service).delete(any());

        mockMvc.perform(delete("/admin/oncall/1/2/3"))
                .andExpect(status().isNoContent());
    }
}