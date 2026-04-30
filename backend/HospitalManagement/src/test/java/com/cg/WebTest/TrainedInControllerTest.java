package com.cg.WebTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cg.controller.TrainedInController;
import com.cg.dto.TrainedInDTO;
import com.cg.entity.*;
import com.cg.exception.BadRequestException;
import com.cg.service.TrainedInService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class TrainedInControllerTest {

    @InjectMocks
    private TrainedInController controller;

    @Mock
    private TrainedInService service;

    private TrainedIn trainedIn;
    private TrainedInId trainedInId;
    private Physician physician;
    private Procedures procedures;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        physician = new Physician();
        physician.setEmployeeId(100);
        physician.setName("Dr. Smith");

        procedures = new Procedures();
        procedures.setCode(101);
        procedures.setName("Cardiac Surgery");

        trainedInId = new TrainedInId(100, 101);

        trainedIn = new TrainedIn();
        trainedIn.setId(trainedInId);
        trainedIn.setPhysician(physician);
        trainedIn.setTreatment(procedures);
        trainedIn.setCertificationDate(LocalDateTime.now().minusMonths(6));
        trainedIn.setCertificationExpires(LocalDateTime.now().plusMonths(6));
    }


    @Test
    void testGetAll() {
        when(service.getAll()).thenReturn(List.of(trainedIn));

        ResponseEntity<List<TrainedInDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetByPhysician() {
        when(service.getByPhysicianId(100)).thenReturn(List.of(trainedIn));

        ResponseEntity<List<TrainedInDTO>> response = controller.getByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetByPhysician_Empty() {
        when(service.getByPhysicianId(100)).thenReturn(List.of());

        ResponseEntity<List<TrainedInDTO>> response = controller.getByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

 
    @Test
    void testAddTraining_Success() {
        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.now().minusMonths(6),
                LocalDate.now().plusMonths(6));

        when(service.save(any(TrainedIn.class))).thenReturn(trainedIn);

        ResponseEntity<?> response = controller.addTraining(dto);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddTraining_InvalidDate() {
        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.now().plusMonths(6),  
                LocalDate.now().minusMonths(6)); 

        assertThrows(BadRequestException.class, () -> controller.addTraining(dto));
    }


    @Test
    void testAddTraining_PastCertificationDate() {
        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(6));

        when(service.save(any(TrainedIn.class))).thenReturn(trainedIn);

        ResponseEntity<?> response = controller.addTraining(dto);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testGetAll_MultipleRecords() {
        TrainedIn trainedIn2 = new TrainedIn();
        trainedIn2.setId(new TrainedInId(200, 201));
        trainedIn2.setPhysician(physician);
        trainedIn2.setTreatment(procedures);
        trainedIn2.setCertificationDate(LocalDateTime.now().minusMonths(3));
        trainedIn2.setCertificationExpires(LocalDateTime.now().plusMonths(9));

        when(service.getAll()).thenReturn(List.of(trainedIn, trainedIn2));

        ResponseEntity<List<TrainedInDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetByPhysician_VerifyData() {
        when(service.getByPhysicianId(100)).thenReturn(List.of(trainedIn));

        ResponseEntity<List<TrainedInDTO>> response = controller.getByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().get(0).getPhysicianId());
        assertEquals(101, response.getBody().get(0).getTreatmentId());
    }

    @Test
    void testAddTraining_VerifyServiceCall() {
        TrainedInDTO dto = new TrainedInDTO(
                100, 101,
                LocalDate.now().minusMonths(6),
                LocalDate.now().plusMonths(6));

        when(service.save(any(TrainedIn.class))).thenReturn(trainedIn);

        controller.addTraining(dto);

        verify(service, times(1)).save(any(TrainedIn.class));
    }
}