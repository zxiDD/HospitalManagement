package com.cg.WebTest;

import com.cg.controller.NurseController;
import com.cg.dto.NurseDTO;
import com.cg.entity.Nurse;
import com.cg.service.NurseService;
import com.cg.service.OnCallService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
class NurseControllerTest {

    @MockitoBean
    private NurseService service;

    @MockitoBean
    private OnCallService onCallService; 

    @Autowired
    private NurseController controller;

    private Nurse nurse;

    @BeforeEach
    void setUp() {
        nurse = new Nurse();
        nurse.setEmployeeId(1);
        nurse.setName("John");
        nurse.setPosition("Staff");
        nurse.setRegistered(true);
        nurse.setSsn(12345L);
    }

    // ✅ getAll
    @Test
    void testGetAll() {
        Mockito.when(service.getAll()).thenReturn(List.of(nurse));

        ResponseEntity<List<NurseDTO>> response = controller.getAll();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
        Mockito.verify(service).getAll();
    }

    // ✅ getById
    @Test
    void testGetById() {
        Mockito.when(service.getById(1)).thenReturn(nurse);

        ResponseEntity<NurseDTO> response = controller.getById(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("John", response.getBody().getName());
        Mockito.verify(service).getById(1);
    }

    // ✅ create
    @Test
    void testCreate() {
        NurseDTO dto = new NurseDTO(1, "John", "Staff", true, 12345L);

        Mockito.when(service.save(Mockito.any(Nurse.class)))
                .thenReturn(nurse);

        ResponseEntity<NurseDTO> response = controller.create(dto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("John", response.getBody().getName());

        Mockito.verify(service).save(Mockito.any(Nurse.class));
    }

    // ✅ update
    @Test
    void testUpdate() {
        NurseDTO dto = new NurseDTO(1, "Updated", "Senior", true, 99999L);

        Mockito.when(service.getById(1)).thenReturn(nurse);
        Mockito.when(service.save(Mockito.any(Nurse.class)))
                .thenReturn(nurse);

        ResponseEntity<NurseDTO> response = controller.update(1, dto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(service).getById(1);
        Mockito.verify(service).save(Mockito.any(Nurse.class));
    }

    // ✅ delete (soft delete)
    @Test
    void testDelete() {
        ResponseEntity<Void> response = controller.delete(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(service).delete(1);
    }
}