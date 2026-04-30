package com.cg.WebTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import com.cg.controller.OnCallController;
import com.cg.dto.OnCallDTO;
import com.cg.entity.*;
import com.cg.exception.BadRequestException;
import com.cg.service.OnCallService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class OnCallControllerTest {

    @InjectMocks
    private OnCallController controller;

    @Mock
    private OnCallService service;

    private OnCall onCall;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        OnCallId id = new OnCallId(1, 2, 3);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);

        onCall = new OnCall();
        onCall.setId(id);
        onCall.setNurse(nurse);
        onCall.setOnCallStart(LocalDateTime.now());
        onCall.setOnCallEnd(LocalDateTime.now().plusHours(2));
    }


    @Test
    void testGetAll() {

        when(service.getAll()).thenReturn(List.of(onCall));

        ResponseEntity<List<OnCallDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }


    @Test
    void testGetByNurse() {
        // Create a specific OnCall for nurse ID 1
        OnCallId id = new OnCallId(1, 2, 3);
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(1);
        
        OnCall specificOnCall = new OnCall();
        specificOnCall.setId(id);
        specificOnCall.setNurse(nurse);
        specificOnCall.setOnCallStart(LocalDateTime.now());
        specificOnCall.setOnCallEnd(LocalDateTime.now().plusHours(2));

        when(service.getByNurseId(1)).thenReturn(List.of(specificOnCall));

        ResponseEntity<List<OnCallDTO>> response =
                controller.getByNurse(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getNurseId());
        
        verify(service, times(1)).getByNurseId(1);
    }


    @Test
    void testAddOnCall_success() {

        OnCallDTO dto = new OnCallDTO(
                1, 2, 3,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );

        when(service.save(any())).thenReturn(onCall);

        ResponseEntity<?> response = controller.addOnCall(dto);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testAddOnCall_invalidTime() {
        LocalDateTime futureStart = LocalDateTime.now().plusDays(1);
        LocalDateTime laterStart = LocalDateTime.now().plusDays(2);
        
        OnCallDTO dto = new OnCallDTO(
                1, 2, 3,
                laterStart,  
                futureStart 
        );

        assertThrows(BadRequestException.class,
                () -> controller.addOnCall(dto));
    }


    @Test
    void testGetOnCallAt() {

        when(service.getAll()).thenReturn(List.of(onCall));

        ResponseEntity<List<OnCallDTO>> response =
                controller.getOnCallAt(LocalDateTime.now());

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testDeleteOnCall() {

        doNothing().when(service).delete(any());

        ResponseEntity<Void> response =
                controller.deleteOnCall(1, 2, 3);

        assertEquals(204, response.getStatusCode().value());

        verify(service, times(1)).delete(any());
    }
}