package com.cg.serviceTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.entity.TrainedIn;
import com.cg.entity.TrainedInId;
import com.cg.exception.*;
import com.cg.repo.TrainedInRepository;
import com.cg.service.TrainedInServiceImpl;

@ExtendWith(MockitoExtension.class)
class TestTrainedInService {

    @Mock
    private TrainedInRepository repo;

    @InjectMocks
    private TrainedInServiceImpl service;

    private TrainedIn t;
    private TrainedInId id;

    @BeforeEach
    void setup() {
        id = new TrainedInId();
        t = new TrainedIn();
        t.setId(id);
        t.setCertificationDate(LocalDateTime.now());
        t.setCertificationExpires(LocalDateTime.now().plusDays(5));
    }

    // ✅ GET SUCCESS
    @Test
    void testGetByIdSuccess() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(t));

        Assertions.assertNotNull(service.getById(id));
    }

    // ❌ GET FAIL
    @Test
    void testGetByIdFail() {
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));
    }

    // ✅ SAVE SUCCESS
    @Test
    void testSaveSuccess() {
        Mockito.when(repo.existsById(id)).thenReturn(false);
        Mockito.when(repo.save(t)).thenReturn(t);

        Assertions.assertNotNull(service.save(t));
    }

    // ❌ DUPLICATE
    @Test
    void testDuplicate() {
        Mockito.when(repo.existsById(id)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> service.save(t));
    }

    // ❌ INVALID DATE
    @Test
    void testInvalidDates() {
        t.setCertificationExpires(LocalDateTime.now().minusDays(1));

        Assertions.assertThrows(IllegalOperationException.class,
                () -> service.save(t));
    }
}