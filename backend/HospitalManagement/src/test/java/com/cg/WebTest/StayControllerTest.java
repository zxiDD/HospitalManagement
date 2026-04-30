package com.cg.WebTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cg.controller.StayController;
import com.cg.dto.StayDTO;
import com.cg.entity.Patient;
import com.cg.entity.Room;
import com.cg.entity.Stay;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.ValidationException;
import com.cg.service.StayService;

@SpringBootTest
public class StayControllerTest {

    @Autowired
    private StayController controller;

    @MockitoBean
    private StayService stayService;

    private Stay stay1;
    private Stay stay2;
    private StayDTO stayDTO1;
    private StayDTO stayDTO2;
    private Patient patient;
    private Room room;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setSsn(12345L);
        patient.setName("John Doe");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        patient.setInsuranceId(100);

        room = new Room();
        room.setRoomNumber(101);
        room.setRoomType("ICU");
        room.setUnavailable(false);

        stay1 = new Stay();
        stay1.setStayId(1);
        stay1.setPatient(patient);
        stay1.setRoom(room);
        stay1.setStayStart(LocalDateTime.now().minusDays(2));
        stay1.setStayEnd(null);

        stay2 = new Stay();
        stay2.setStayId(2);
        stay2.setPatient(patient);
        stay2.setRoom(room);
        stay2.setStayStart(LocalDateTime.now().minusDays(5));
        stay2.setStayEnd(LocalDateTime.now().minusDays(3));

        stayDTO1 = new StayDTO(1, 12345L, "John Doe", 101, "ICU", 
                LocalDateTime.now().minusDays(2), null);
        stayDTO2 = new StayDTO(2, 12345L, "John Doe", 101, "ICU",
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(3));
    }

    @Test
    void testGetAll_Success() {
        when(stayService.getAll()).thenReturn(List.of(stayDTO1, stayDTO2));

        ResponseEntity<List<StayDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getPatientName());
    }

    @Test
    void testGetAll_Empty() {
        when(stayService.getAll()).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetById_Found() {
        when(stayService.getById(1)).thenReturn(stayDTO1);

        ResponseEntity<StayDTO> response = controller.getById(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getStayId());
        assertEquals("John Doe", response.getBody().getPatientName());
    }

    @Test
    void testGetById_NotFound() {
        when(stayService.getById(999)).thenThrow(new ResourceNotFoundException("Stay not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getById(999));
    }

    @Test
    void testCreateStay_Success() {
        StayDTO dto = new StayDTO(null, 12345L, null, 101, null,
                LocalDateTime.now().plusDays(1), null);
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);

        when(stayService.create(any(StayDTO.class))).thenReturn(
                new StayDTO(3, 12345L, "John Doe", 101, "ICU", 
                        LocalDateTime.now().plusDays(1), null));

        ResponseEntity<StayDTO> response = controller.createStay(dto, br);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getStayId());
    }

    @Test
    void testCreateStay_InvalidPatientSsn() {
        StayDTO dto = new StayDTO(null, null, null, 101, null,
                LocalDateTime.now(), null);
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("stayDTO", "patientSsn", "Patient SSN is required"));
        when(br.getFieldErrors()).thenReturn(errors);

        when(stayService.create(any(StayDTO.class))).thenThrow(new ValidationException(errors));

        assertThrows(ValidationException.class, () -> controller.createStay(dto, br));
    }

    @Test
    void testCreateStay_InvalidRoomNumber() {
        StayDTO dto = new StayDTO(null, 12345L, null, null, null,
                LocalDateTime.now(), null);
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("stayDTO", "roomNumber", "Room number is required"));
        when(br.getFieldErrors()).thenReturn(errors);

        when(stayService.create(any(StayDTO.class))).thenThrow(new ValidationException(errors));

        assertThrows(ValidationException.class, () -> controller.createStay(dto, br));
    }

    @Test
    void testGetByPatientSsn_Success() {
        when(stayService.getByPatientSsn(12345L)).thenReturn(List.of(stayDTO1, stayDTO2));

        ResponseEntity<List<StayDTO>> response = controller.getByPatientSsn(12345L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetByPatientSsn_Empty() {
        when(stayService.getByPatientSsn(999L)).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getByPatientSsn(999L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetByRoomNumber_Success() {
        when(stayService.getByRoomNumber(101)).thenReturn(List.of(stayDTO1));

        ResponseEntity<List<StayDTO>> response = controller.getByRoomNumber(101);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getRoomNumber());
    }

    @Test
    void testGetByRoomNumber_Empty() {
        when(stayService.getByRoomNumber(999)).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getByRoomNumber(999);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetStaysAfter_Success() {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(10);
        when(stayService.getStaysAfter(any(LocalDateTime.class))).thenReturn(List.of(stayDTO1, stayDTO2));

        ResponseEntity<List<StayDTO>> response = controller.getStaysAfter("2026-04-20T10:00:00");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetStaysAfter_Empty() {
        when(stayService.getStaysAfter(any(LocalDateTime.class))).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getStaysAfter("2026-05-30T10:00:00");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetActiveStays_Success() {
        when(stayService.getActiveStays()).thenReturn(List.of(stayDTO1));

        ResponseEntity<List<StayDTO>> response = controller.getActiveStays();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertNull(response.getBody().get(0).getStayEnd());
    }

    @Test
    void testGetActiveStays_Empty() {
        when(stayService.getActiveStays()).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getActiveStays();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetPatientStayHistory_Success() {
        when(stayService.getPatientStayHistory(12345L)).thenReturn(List.of(stayDTO1, stayDTO2));

        ResponseEntity<List<StayDTO>> response = controller.getPatientStayHistory(12345L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetPatientStayHistory_Empty() {
        when(stayService.getPatientStayHistory(999L)).thenReturn(List.of());

        ResponseEntity<List<StayDTO>> response = controller.getPatientStayHistory(999L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}