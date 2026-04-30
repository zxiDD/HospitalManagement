package com.cg.WebTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cg.controller.UndergoesController;
import com.cg.dto.StayDTO;
import com.cg.dto.UndergoesDTO;
import com.cg.entity.*;
import com.cg.service.NurseService;
import com.cg.service.PatientService;
import com.cg.service.PhysicianService;
import com.cg.service.ProceduresService;
import com.cg.service.RoomService;
import com.cg.service.StayService;
import com.cg.service.UndergoesService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class UndergoesControllerTest {

    @InjectMocks
    private UndergoesController controller;

    @Mock
    private UndergoesService undergoesService;

    @Mock
    private PatientService patientService;

    @Mock
    private ProceduresService proceduresService;

    @Mock
    private StayService stayService;

    @Mock
    private NurseService nurseService;

    @Mock
    private RoomService roomService;

    @Mock
    private PhysicianService physicianService;

    private Undergoes undergoes;
    private UndergoesId undergoesId;
    private Patient patient;
    private Procedures procedures;
    private Stay stay;
    private Physician physician;
    private Nurse nurse;
    private Room room;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Create test data
        patient = new Patient();
        patient.setSsn(12345L);
        patient.setName("John Doe");

        procedures = new Procedures();
        procedures.setCode(101);
        procedures.setName("Surgery");

        room = new Room();
        room.setRoomNumber(201);

        stay = new Stay();
        stay.setStayId(1);
        stay.setPatient(patient);
        stay.setRoom(room);
        stay.setStayStart(LocalDateTime.now());
        stay.setStayEnd(LocalDateTime.now().plusDays(5));

        physician = new Physician();
        physician.setEmployeeId(100);
        physician.setName("Dr. Smith");

        nurse = new Nurse();
        nurse.setEmployeeId(50);
        nurse.setName("Nurse Joy");

        undergoesId = new UndergoesId(12345L, 101, 1, LocalDateTime.now());

        undergoes = new Undergoes();
        undergoes.setId(undergoesId);
        undergoes.setPatient(patient);
        undergoes.setProcedures(procedures);
        undergoes.setStay(stay);
        undergoes.setPhysician(physician);
        undergoes.setAssistingNurse(nurse);
    }

 
    @Test
    void testGetAllUndergoes() {
        when(undergoesService.getAllUndergoes()).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getAllUndergoes();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }



    @Test
    void testGetUndergoesById_NotFound() {
        when(undergoesService.getUndergoesById(undergoesId)).thenReturn(Optional.empty());

        ResponseEntity<UndergoesDTO> response = controller.getUndergoesById(
                12345L, 101, 1, undergoesId.getDateUndergoes());

        assertEquals(404, response.getStatusCode().value());
    }


    @Test
    void testGetUndergoesByPatient() {
        when(undergoesService.getUndergoesByPatient(12345L)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByPatient(12345L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetUndergoesByProcedure() {
        when(undergoesService.getUndergoesByProcedure(101)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByProcedure(101);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetUndergoesByStay() {
        when(undergoesService.getUndergoesByStay(1)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByStay(1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUndergoesByPhysician() {
        when(undergoesService.getUndergoesByPhysician(100)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByPhysician(100);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetUndergoesByAssistingNurse() {
        when(undergoesService.getUndergoesByAssistingNurse(50)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByAssistingNurse(50);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUndergoesByDate() {
        LocalDateTime testDate = LocalDateTime.now();
        when(undergoesService.getUndergoesByDate(testDate)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesByDate(testDate);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetUndergoesBetweenDates() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        when(undergoesService.getUndergoesBetweenDates(startDate, endDate)).thenReturn(List.of(undergoes));

        ResponseEntity<List<UndergoesDTO>> response = controller.getUndergoesBetweenDates(startDate, endDate);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

 
    @Test
    void testCreateUndergoes_Success() {
        UndergoesDTO dto = new UndergoesDTO(
                12345L, 101, 1,
                LocalDateTime.now(),
                100, 50);

        StayDTO stayDTO = new StayDTO();
        stayDTO.setStayId(1);
        stayDTO.setRoomNumber(201);
        stayDTO.setStayStart(LocalDateTime.now());
        stayDTO.setStayEnd(LocalDateTime.now().plusDays(5));

        when(patientService.getById(12345L)).thenReturn(patient);
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedures));
        when(stayService.getById(1)).thenReturn(stayDTO);
        when(roomService.getRoomById(201)).thenReturn(Optional.of(room));
        when(physicianService.getById(100)).thenReturn(physician);
        when(nurseService.getById(50)).thenReturn(nurse);
        when(undergoesService.saveUndergoes(any(Undergoes.class))).thenReturn(undergoes);

        ResponseEntity<UndergoesDTO> response = controller.createUndergoes(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testCreateUndergoes_WithoutNurse() {
        UndergoesDTO dto = new UndergoesDTO(
                12345L, 101, 1,
                LocalDateTime.now(),
                100, null);

        StayDTO stayDTO = new StayDTO();
        stayDTO.setStayId(1);
        stayDTO.setRoomNumber(201);
        stayDTO.setStayStart(LocalDateTime.now());
        stayDTO.setStayEnd(LocalDateTime.now().plusDays(5));

        when(patientService.getById(12345L)).thenReturn(patient);
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedures));
        when(stayService.getById(1)).thenReturn(stayDTO);
        when(roomService.getRoomById(201)).thenReturn(Optional.of(room));
        when(physicianService.getById(100)).thenReturn(physician);
        when(undergoesService.saveUndergoes(any(Undergoes.class))).thenReturn(undergoes);

        ResponseEntity<UndergoesDTO> response = controller.createUndergoes(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testCreateUndergoes_ProcedureNotFound() {
        UndergoesDTO dto = new UndergoesDTO(
                12345L, 999, 1,
                LocalDateTime.now(),
                100, 50);

        when(patientService.getById(12345L)).thenReturn(patient);
        when(proceduresService.getProcedureById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> controller.createUndergoes(dto));
    }


    @Test
    void testCreateUndergoes_RoomNotFound() {
        UndergoesDTO dto = new UndergoesDTO(
                12345L, 101, 1,
                LocalDateTime.now(),
                100, 50);

        StayDTO stayDTO = new StayDTO();
        stayDTO.setStayId(1);
        stayDTO.setRoomNumber(999);
        stayDTO.setStayStart(LocalDateTime.now());
        stayDTO.setStayEnd(LocalDateTime.now().plusDays(5));

        when(patientService.getById(12345L)).thenReturn(patient);
        when(proceduresService.getProcedureById(101)).thenReturn(Optional.of(procedures));
        when(stayService.getById(1)).thenReturn(stayDTO);
        when(roomService.getRoomById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> controller.createUndergoes(dto));
    }
}