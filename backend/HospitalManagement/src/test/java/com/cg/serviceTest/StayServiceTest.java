package com.cg.serviceTest;

import com.cg.dto.StayDTO;
import com.cg.entity.*;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PatientRepository;
import com.cg.repo.RoomRepository;
import com.cg.repo.StayRepository;
import com.cg.service.StayService;
import com.cg.service.StayServiceImpl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StayServiceTest {

//    @MockitoBean
	@Mock
    private StayRepository stayRepo;

	@Mock
    private PatientRepository patientRepo;

	@Mock
    private RoomRepository roomRepo;

	@InjectMocks
    private StayServiceImpl service;

    private Stay stay;
    private Patient patient;
    private Room room;

    @BeforeEach
    void setup() {

        patient = new Patient();
        patient.setSsn(1001L);
        patient.setName("John");

        room = new Room();
        room.setRoomNumber(101);
        room.setRoomType("ICU");

        stay = new Stay();
        stay.setStayId(1);
        stay.setPatient(patient);
        stay.setRoom(room);
        stay.setStayStart(LocalDateTime.now().minusDays(1));
        stay.setStayEnd(null);
    }

    // ================= GET METHODS =================

    @Test
    void testGetAll() {

        Mockito.when(stayRepo.findAll())
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getAll();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findAll();
    }

    @Test
    void testGetById_success() {

        Mockito.when(stayRepo.findById(1))
                .thenReturn(Optional.of(stay));

        StayDTO result = service.getById(1);

        Assertions.assertNotNull(result);
        Mockito.verify(stayRepo).findById(1);
    }

    @Test
    void testGetById_notFound() {

        Mockito.when(stayRepo.findById(1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1));

        Mockito.verify(stayRepo).findById(1);
    }

    @Test
    void testGetByPatientSsn() {

        Mockito.when(stayRepo.findByPatient_Ssn(1001L))
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getByPatientSsn(1001L);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findByPatient_Ssn(1001L);
    }

    @Test
    void testGetByRoomNumber() {

        Mockito.when(stayRepo.findByRoom_RoomNumber(101))
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getByRoomNumber(101);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findByRoom_RoomNumber(101);
    }

    @Test
    void testGetStaysAfter() {

        LocalDateTime date = LocalDateTime.now().minusDays(2);

        Mockito.when(stayRepo.findByStayStartAfter(date))
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getStaysAfter(date);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findByStayStartAfter(date);
    }

    @Test
    void testGetActiveStays() {

        Mockito.when(stayRepo.findActiveStays(Mockito.any()))
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getActiveStays();

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findActiveStays(Mockito.any());
    }

    @Test
    void testGetPatientStayHistory() {

        Mockito.when(stayRepo.findByPatientSsnOrderByStayStartDesc(1001L))
                .thenReturn(List.of(stay));

        List<StayDTO> result = service.getPatientStayHistory(1001L);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(stayRepo).findByPatientSsnOrderByStayStartDesc(1001L);
    }

    // ================= CREATE =================

    @Test
    void testCreate_success() {

        StayDTO input = new StayDTO(
                null, 1001L, null,
                101, null,
                LocalDateTime.now(), null
        );

        Mockito.when(patientRepo.findById(1001L))
                .thenReturn(Optional.of(patient));

        Mockito.when(roomRepo.findById(101))
                .thenReturn(Optional.of(room));

        Mockito.when(stayRepo.save(Mockito.any()))
                .thenReturn(stay);

        StayDTO result = service.create(input);

        Assertions.assertNotNull(result);

        Mockito.verify(patientRepo).findById(1001L);
        Mockito.verify(roomRepo).findById(101);
        Mockito.verify(stayRepo).save(Mockito.any());
    }

    @Test
    void testCreate_patientNotFound() {

        StayDTO input = new StayDTO(
                null, 1001L, null,
                101, null,
                LocalDateTime.now(), null
        );

        Mockito.when(patientRepo.findById(1001L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.create(input));

        Mockito.verify(patientRepo).findById(1001L);

        Mockito.verify(stayRepo, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void testCreate_roomNotFound() {

        StayDTO input = new StayDTO(
                null, 1001L, null,
                101, null,
                LocalDateTime.now(), null
        );

        Mockito.when(patientRepo.findById(1001L))
                .thenReturn(Optional.of(patient));

        Mockito.when(roomRepo.findById(101))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.create(input));

        Mockito.verify(patientRepo).findById(1001L);
        Mockito.verify(roomRepo).findById(101);

        Mockito.verify(stayRepo, Mockito.never())
                .save(Mockito.any());
    }

    // ================= BOOLEAN METHOD =================

    @Test
    void testIsPatientActive_true() {

        Mockito.when(stayRepo.existsActiveStayByPatient(1001L))
                .thenReturn(true);

        boolean result = service.isPatientActive(1001L);

        Assertions.assertTrue(result);
        Mockito.verify(stayRepo).existsActiveStayByPatient(1001L);
    }

    @Test
    void testIsPatientActive_false() {

        Mockito.when(stayRepo.existsActiveStayByPatient(1001L))
                .thenReturn(false);

        boolean result = service.isPatientActive(1001L);

        Assertions.assertFalse(result);
        Mockito.verify(stayRepo).existsActiveStayByPatient(1001L);
    }
}